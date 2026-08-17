#!/usr/bin/env python3
"""Create detailed 32x32 textures for scaled multiblock barrel models."""

from __future__ import annotations

import argparse
import colorsys
from pathlib import Path
from zipfile import ZipFile

from PIL import Image


TEXTURE_NAMES = ("bottom", "side", "side_alt", "top")
WOODS = (
    "acacia", "apple", "bamboo", "birch", "cherry", "crimson", "dark_oak",
    "jungle", "mangrove", "oak", "pale_oak", "spruce", "warped",
)


def luminance(pixel: tuple[int, int, int, int]) -> float:
    red, green, blue, _ = pixel
    return (0.2126 * red + 0.7152 * green + 0.0722 * blue) / 255.0


def generate_texture(source: Image.Image, planks: Image.Image, size: int) -> Image.Image:
    enlarged = source.convert("RGBA").resize((size, size), Image.Resampling.NEAREST)
    plank_pixels = list(planks.convert("RGBA").getdata())
    plank_mean = sum(luminance(pixel) for pixel in plank_pixels) / len(plank_pixels)
    output = enlarged.copy()

    for y in range(size):
        for x in range(size):
            red, green, blue, alpha = enlarged.getpixel((x, y))
            hue, saturation, value = colorsys.rgb_to_hsv(red / 255.0, green / 255.0, blue / 255.0)
            if alpha == 0 or saturation < 0.16 or value < 0.16:
                continue

            detail = luminance(plank_pixels[(y % 16) * 16 + (x % 16)]) - plank_mean
            value = max(0.0, min(1.0, value + detail * 0.32))
            out_red, out_green, out_blue = colorsys.hsv_to_rgb(hue, saturation, value)
            output.putpixel((x, y), (
                round(out_red * 255),
                round(out_green * 255),
                round(out_blue * 255),
                alpha,
            ))

    return output


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--templates", type=Path, required=True)
    parser.add_argument("--minecraft-jar", type=Path, required=True)
    parser.add_argument("--apple-planks", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--size", type=int, default=32)
    args = parser.parse_args()

    args.output.mkdir(parents=True, exist_ok=True)
    with ZipFile(args.minecraft_jar) as minecraft:
        for wood in WOODS:
            if wood == "apple":
                planks = Image.open(args.apple_planks)
            else:
                planks = Image.open(minecraft.open(f"assets/minecraft/textures/block/{wood}_planks.png"))
            for texture_name in TEXTURE_NAMES:
                source = Image.open(args.templates / f"{wood}_{texture_name}.png")
                generate_texture(source, planks, args.size).save(args.output / f"{wood}_{texture_name}.png")


if __name__ == "__main__":
    main()
