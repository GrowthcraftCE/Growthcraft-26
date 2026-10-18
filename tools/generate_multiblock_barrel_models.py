#!/usr/bin/env python3
"""Generate structure-relative models for rounded multiblock barrels."""

from __future__ import annotations

import argparse
import copy
import json
from pathlib import Path


SCALE = 2
PARTS: dict[tuple[int, int, int], str] = {}

FACINGS = {
    "north": ((1, 0), (0, -1)),
    "south": ((-1, 0), (0, 1)),
    "east": ((0, 1), (1, 0)),
    "west": ((0, -1), (-1, 0)),
}

WOODS = (
    "acacia", "apple", "bamboo", "birch", "cherry", "crimson", "dark_oak",
    "jungle", "mangrove", "oak", "pale_oak", "spruce", "warped",
)

FACE_MAPS = {
    "north": {"north": "south", "south": "north", "east": "east", "west": "west"},
    "south": {"north": "north", "south": "south", "east": "west", "west": "east"},
    "east": {"north": "west", "south": "east", "east": "south", "west": "north"},
    "west": {"north": "east", "south": "west", "east": "north", "west": "south"},
}

# Minecraft FaceInfo vertex order (from/to selectors for x, y, z). Preserve UVs
# at these vertices when baking the structure's rotation/reflection into parts.
FACE_VERTICES = {
    "down": ((0, 0, 1), (0, 0, 0), (1, 0, 0), (1, 0, 1)),
    "up": ((0, 1, 0), (0, 1, 1), (1, 1, 1), (1, 1, 0)),
    "north": ((1, 1, 0), (1, 0, 0), (0, 0, 0), (0, 1, 0)),
    "south": ((0, 1, 1), (0, 0, 1), (1, 0, 1), (1, 1, 1)),
    "west": ((0, 1, 0), (0, 0, 0), (0, 0, 1), (0, 1, 1)),
    "east": ((1, 1, 1), (1, 0, 1), (1, 0, 0), (1, 1, 0)),
}


def face_vertices(element: dict, direction: str) -> list[list[float]]:
    return [[element["to" if end else "from"][axis] for axis, end in enumerate(vertex)]
            for vertex in FACE_VERTICES[direction]]


def vertex_uv(face: dict, vertex: int) -> tuple[float, float]:
    index = (vertex + face.get("rotation", 0) // 90) % 4
    uv = face["uv"]
    return (uv[0 if index in (0, 1) else 2], uv[1 if index in (0, 3) else 3])


def scaled(values: list[float]) -> list[float]:
    return [value * SCALE for value in values]


FACE_AXES = {
    "north": (0, 1),
    "south": (0, 1),
    "east": (2, 1),
    "west": (2, 1),
    "up": (0, 2),
    "down": (0, 2),
}

FACE_PLANES = {
    "west": (0, False),
    "east": (0, True),
    "down": (1, False),
    "up": (1, True),
    "north": (2, False),
    "south": (2, True),
}


def rotated_fraction_bounds(
    s_from: float, s_to: float, t_from: float, t_to: float, rotation: int
) -> tuple[float, float, float, float]:
    corners = []
    for s in (s_from, s_to):
        for t in (t_from, t_to):
            if rotation == 90:
                corners.append((t, 1.0 - s))
            elif rotation == 180:
                corners.append((1.0 - s, 1.0 - t))
            elif rotation == 270:
                corners.append((1.0 - t, s))
            else:
                corners.append((s, t))
    return (
        min(point[0] for point in corners),
        min(point[1] for point in corners),
        max(point[0] for point in corners),
        max(point[1] for point in corners),
    )


def crop_face_uv(face: dict, direction: str, original: dict, clipped: dict) -> dict:
    cropped = copy.deepcopy(face)
    # The original Blockbench model used a placeholder texture variable for
    # several lid/end-cap faces. Point generated faces directly at the top
    # texture; aliases do not reliably survive the parent/child model chain.
    if cropped.get("texture") == "#missing":
        cropped["texture"] = "#3"
    if "uv" not in cropped:
        return cropped

    u_axis, v_axis = FACE_AXES[direction]
    original_size_u = original["to"][u_axis] - original["from"][u_axis]
    original_size_v = original["to"][v_axis] - original["from"][v_axis]
    if original_size_u == 0 or original_size_v == 0:
        return cropped

    s_from = (clipped["from"][u_axis] - original["from"][u_axis]) / original_size_u
    s_to = (clipped["to"][u_axis] - original["from"][u_axis]) / original_size_u
    t_from = (clipped["from"][v_axis] - original["from"][v_axis]) / original_size_v
    t_to = (clipped["to"][v_axis] - original["from"][v_axis]) / original_size_v
    # Face UV axes are not uniformly positive world axes. In particular, side
    # faces run downwards in V, and north/east run backwards in U.
    if direction in ("north", "east"):
        s_from, s_to = 1.0 - s_to, 1.0 - s_from
    if direction != "up":
        t_from, t_to = 1.0 - t_to, 1.0 - t_from
    q_from_u, q_from_v, q_to_u, q_to_v = rotated_fraction_bounds(
        s_from, s_to, t_from, t_to, cropped.get("rotation", 0)
    )
    uv_from_u, uv_from_v, uv_to_u, uv_to_v = cropped["uv"]
    cropped["uv"] = [
        uv_from_u + (uv_to_u - uv_from_u) * q_from_u,
        uv_from_v + (uv_to_v - uv_from_v) * q_from_v,
        uv_from_u + (uv_to_u - uv_from_u) * q_to_u,
        uv_from_v + (uv_to_v - uv_from_v) * q_to_v,
    ]
    return cropped


def clip_element_to_cell(source_element: dict, cell: tuple[int, int, int]) -> dict | None:
    # Cell boundaries are eight source-model units because the final geometry is
    # scaled by two. Clipping here keeps every generated element inside its
    # owning Minecraft block, which is required for correct neighbour lighting.
    cell_size = 16.0 / SCALE
    cell_from = [coordinate * cell_size for coordinate in cell]
    cell_to = [(coordinate + 1) * cell_size for coordinate in cell]
    clipped = copy.deepcopy(source_element)
    clipped["from"] = [max(source_element["from"][axis], cell_from[axis]) for axis in range(3)]
    clipped["to"] = [min(source_element["to"][axis], cell_to[axis]) for axis in range(3)]
    if any(clipped["from"][axis] >= clipped["to"][axis] for axis in range(3)):
        return None

    faces = {}
    for direction, face in source_element.get("faces", {}).items():
        plane_axis, uses_to = FACE_PLANES[direction]
        plane_key = "to" if uses_to else "from"
        if clipped[plane_key][plane_axis] != source_element[plane_key][plane_axis]:
            continue
        faces[direction] = crop_face_uv(face, direction, source_element, clipped)
    clipped["faces"] = faces
    return clipped


def transform_point(point: list[float], facing: str) -> list[float]:
    right, forward = FACINGS[facing]
    anchor_x = 16.0 if right[0] < 0 or forward[0] < 0 else 0.0
    anchor_z = 16.0 if right[1] < 0 or forward[1] < 0 else 0.0
    return [
        anchor_x + right[0] * point[0] + forward[0] * point[2],
        point[1],
        anchor_z + right[1] * point[0] + forward[1] * point[2],
    ]


def part_origin(cell: tuple[int, int, int], facing: str) -> list[float]:
    right, forward = FACINGS[facing]
    return [
        16.0 * (right[0] * cell[0] + forward[0] * cell[2]),
        16.0 * cell[1],
        16.0 * (right[1] * cell[0] + forward[1] * cell[2]),
    ]


def orient_element(source_element: dict, cell: tuple[int, int, int], facing: str) -> dict:
    element = copy.deepcopy(source_element)
    corners = [
        transform_point([x, y, z], facing)
        for x in (source_element["from"][0] * SCALE, source_element["to"][0] * SCALE)
        for y in (source_element["from"][1] * SCALE, source_element["to"][1] * SCALE)
        for z in (source_element["from"][2] * SCALE, source_element["to"][2] * SCALE)
    ]
    origin = part_origin(cell, facing)
    element["from"] = [min(point[axis] for point in corners) - origin[axis] for axis in range(3)]
    element["to"] = [max(point[axis] for point in corners) - origin[axis] for axis in range(3)]

    rotation = element.get("rotation")
    if rotation:
        rotation["origin"] = [
            coordinate - origin[index]
            for index, coordinate in enumerate(transform_point(scaled(rotation["origin"]), facing))
        ]

    mapped_faces = {}
    for face_name, face in element.get("faces", {}).items():
        mapped_face = copy.deepcopy(face)
        mapped_direction = FACE_MAPS[facing].get(face_name, face_name)
        if "uv" in face:
            source_vertices = [
                [value - origin[axis] for axis, value in enumerate(transform_point(scaled(point), facing))]
                for point in face_vertices(source_element, face_name)
            ]
            destination_uvs = [vertex_uv(face, source_vertices.index(point))
                               for point in face_vertices(element, mapped_direction)]
            # A reflected facing also reverses one UV axis; adding a rotation
            # alone cannot preserve the texture. Fit the exact four corners.
            for rotation in (0, 90, 180, 270):
                first = destination_uvs[(-rotation // 90) % 4]
                opposite = destination_uvs[(2 - rotation // 90) % 4]
                mapped_face["uv"] = [*first, *opposite]
                mapped_face["rotation"] = rotation
                if all(vertex_uv(mapped_face, i) == uv for i, uv in enumerate(destination_uvs)):
                    break
            else:
                raise ValueError(f"Cannot preserve {face_name} UVs for {facing}")
            if not mapped_face["rotation"]:
                mapped_face.pop("rotation", None)
        mapped_faces[mapped_direction] = mapped_face
    element["faces"] = mapped_faces
    return element


def build_parts(size: int) -> dict[tuple[int, int, int], str]:
    horizontal = ("left", "right") if size == 2 else ("left", "center", "right")
    depth = ("near", "far") if size == 2 else ("near", "center", "far")
    vertical = ("bottom", "top") if size == 2 else ("bottom", "middle", "top")
    parts = {}
    for y in range(size):
        for z in range(size):
            for x in range(size):
                # Avoid the awkward "center_center" suffix for the exact
                # middle of a 3x3 layer. These names are persisted in worlds.
                if size == 3 and x == 1 and z == 1:
                    parts[(x, y, z)] = f"{vertical[y]}_center"
                else:
                    parts[(x, y, z)] = f"{vertical[y]}_{depth[z]}_{horizontal[x]}"
    return parts


def generate(source_path: Path, output_root: Path, size: int, name: str,
             texture_dir: str, item_parent: str) -> None:
    global SCALE, PARTS
    SCALE = size
    PARTS = build_parts(size)
    source = json.loads(source_path.read_text(encoding="utf-8"))
    geometry_dir = output_root / "models" / "block" / name
    geometry_dir.mkdir(parents=True, exist_ok=True)

    for facing in FACINGS:
        elements_by_part = {name: [] for name in PARTS.values()}
        for source_element in source["elements"]:
            for cell, part_name in PARTS.items():
                clipped = clip_element_to_cell(source_element, cell)
                if clipped is not None:
                    elements_by_part[part_name].append(orient_element(clipped, cell, facing))

        for part_name, elements in elements_by_part.items():
            model_name = f"{facing}_{part_name}"
            model = {
                "credit": "Generated from the Growthcraft rounded fermentation barrel model",
                "ambientocclusion": False,
                "render_type": source.get("render_type", "cutout"),
                "textures": source["textures"],
                "elements": elements,
            }
            (geometry_dir / f"{model_name}.json").write_text(json.dumps(model, indent=2) + "\n", encoding="utf-8")

            for wood in WOODS:
                child = {
                    "parent": f"growthcraft_cellar:block/{name}/{model_name}",
                    "textures": {
                        "0": f"growthcraft_cellar:block/{texture_dir}/{wood}_bottom",
                        "1": f"growthcraft_cellar:block/{texture_dir}/{wood}_side",
                        "2": f"growthcraft_cellar:block/{texture_dir}/{wood}_side_alt",
                        "3": f"growthcraft_cellar:block/{texture_dir}/{wood}_top",
                        "particle": f"growthcraft_cellar:block/{texture_dir}/{wood}_bottom",
                    },
                }
                child_path = output_root / "models" / "block" / f"{name}_{wood}_{model_name}.json"
                child_path.write_text(json.dumps(child, indent=2) + "\n", encoding="utf-8")

    blockstate_dir = output_root / "blockstates"
    blockstate_dir.mkdir(parents=True, exist_ok=True)
    model_item_dir = output_root / "models" / "item"
    model_item_dir.mkdir(parents=True, exist_ok=True)
    item_dir = output_root / "items"
    item_dir.mkdir(parents=True, exist_ok=True)
    for wood in WOODS:
        variants = {}
        for facing in FACINGS:
            for part_name in PARTS.values():
                variants[f"facing={facing},part={part_name}"] = {
                    "model": f"growthcraft_cellar:block/{name}_{wood}_{facing}_{part_name}"
                }
        variant_name = f"{name}_{wood}"
        (blockstate_dir / f"{variant_name}.json").write_text(
            json.dumps({"variants": variants}, indent=2) + "\n", encoding="utf-8"
        )
        (model_item_dir / f"{variant_name}.json").write_text(
            json.dumps({"parent": f"growthcraft_cellar:block/{item_parent}_{wood}"}, indent=2) + "\n",
            encoding="utf-8",
        )
        (item_dir / f"{variant_name}.json").write_text(
            json.dumps({"model": {"type": "minecraft:model", "model": f"growthcraft_cellar:item/{variant_name}"}}, indent=2) + "\n",
            encoding="utf-8",
        )


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--source", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--size", type=int, choices=(2, 3), default=2)
    parser.add_argument("--name", default="large_fermentation_barrel")
    parser.add_argument("--texture-dir", default="barrel_ferment/large")
    parser.add_argument("--item-parent", default="fermentation_barrel")
    args = parser.parse_args()
    generate(args.source, args.output, args.size, args.name, args.texture_dir, args.item_parent)


if __name__ == "__main__":
    main()
