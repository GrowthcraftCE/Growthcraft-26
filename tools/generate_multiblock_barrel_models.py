#!/usr/bin/env python3
"""Generate structure-relative models for the 2x2x2 fermentation barrel."""

from __future__ import annotations

import argparse
import copy
import json
from pathlib import Path


PARTS = {
    (0, 0, 0): "bottom_near_left",
    (1, 0, 0): "bottom_near_right",
    (0, 0, 1): "bottom_far_left",
    (1, 0, 1): "bottom_far_right",
    (0, 1, 0): "top_near_left",
    (1, 1, 0): "top_near_right",
    (0, 1, 1): "top_far_left",
    (1, 1, 1): "top_far_right",
}

FACINGS = {
    "north": ((1, 0), (0, -1)),
    "south": ((-1, 0), (0, 1)),
    "east": ((0, 1), (1, 0)),
    "west": ((0, -1), (-1, 0)),
}

FACE_MAPS = {
    "north": {"north": "south", "south": "north", "east": "east", "west": "west"},
    "south": {"north": "north", "south": "south", "east": "west", "west": "east"},
    "east": {"north": "west", "south": "east", "east": "south", "west": "north"},
    "west": {"north": "east", "south": "west", "east": "north", "west": "south"},
}

# The structure geometry is baked for each horizontal facing rather than rotated
# by the blockstate (blockstate rotation would rotate every quarter around its
# own block centre). Horizontal faces therefore need the equivalent texture
# rotation baked into their face metadata as well. Without this, the lid and
# bottom hardware turn independently from the barrel and break at part seams.
HORIZONTAL_FACE_ROTATIONS = {
    "north": 0,
    "east": 90,
    "south": 180,
    "west": 270,
}


def scaled(values: list[float], factor: float = 2.0) -> list[float]:
    return [value * factor for value in values]


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
    cell_from = [coordinate * 8.0 for coordinate in cell]
    cell_to = [(coordinate + 1) * 8.0 for coordinate in cell]
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
        for x in (source_element["from"][0] * 2.0, source_element["to"][0] * 2.0)
        for y in (source_element["from"][1] * 2.0, source_element["to"][1] * 2.0)
        for z in (source_element["from"][2] * 2.0, source_element["to"][2] * 2.0)
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
        if face_name in ("up", "down"):
            existing_rotation = mapped_face.get("rotation", 0)
            rotation = (existing_rotation + HORIZONTAL_FACE_ROTATIONS[facing]) % 360
            if rotation:
                mapped_face["rotation"] = rotation
            else:
                mapped_face.pop("rotation", None)
        mapped_faces[FACE_MAPS[facing].get(face_name, face_name)] = mapped_face
    element["faces"] = mapped_faces
    return element


def generate(source_path: Path, output_root: Path) -> None:
    source = json.loads(source_path.read_text(encoding="utf-8"))
    geometry_dir = output_root / "models" / "block" / "large_fermentation_barrel"
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

            child = {
                "parent": f"growthcraft_cellar:block/large_fermentation_barrel/{model_name}",
                "textures": {
                    "0": "growthcraft_cellar:block/barrel_ferment/large/oak_bottom",
                    "1": "growthcraft_cellar:block/barrel_ferment/large/oak_side",
                    "2": "growthcraft_cellar:block/barrel_ferment/large/oak_side_alt",
                    "3": "growthcraft_cellar:block/barrel_ferment/large/oak_top",
                    "particle": "growthcraft_cellar:block/barrel_ferment/large/oak_bottom"
                },
            }
            child_path = output_root / "models" / "block" / f"large_fermentation_barrel_oak_{model_name}.json"
            child_path.write_text(json.dumps(child, indent=2) + "\n", encoding="utf-8")

    variants = {}
    for facing in FACINGS:
        for part_name in PARTS.values():
            variants[f"facing={facing},part={part_name}"] = {
                "model": f"growthcraft_cellar:block/large_fermentation_barrel_oak_{facing}_{part_name}"
            }

    blockstate_dir = output_root / "blockstates"
    blockstate_dir.mkdir(parents=True, exist_ok=True)
    (blockstate_dir / "large_fermentation_barrel_oak.json").write_text(
        json.dumps({"variants": variants}, indent=2) + "\n", encoding="utf-8"
    )


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--source", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()
    generate(args.source, args.output)


if __name__ == "__main__":
    main()
