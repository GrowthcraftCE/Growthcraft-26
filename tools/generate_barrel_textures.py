"""Generate a fermentation-barrel texture set from a vanilla-style plank texture.

Requires Pillow. The transformation preserves the existing oak barrel masks,
shading, seams, and hardware while shifting wood pixels toward the supplied
plank texture's mean HSV palette.
"""

from __future__ import annotations

import argparse
import colorsys
import math
from pathlib import Path

from PIL import Image


TEXTURE_NAMES = ("bottom", "side", "side_alt", "top")


def plank_pixels(image: Image.Image) -> list[tuple[float, float, float]]:
    pixels = []
    for red, green, blue, alpha in image.convert("RGBA").getdata():
        if alpha == 0:
            continue
        pixels.append(colorsys.rgb_to_hsv(red / 255, green / 255, blue / 255))
    return pixels


def mean_hue(pixels: list[tuple[float, float, float]]) -> float:
    x = sum(math.cos(hue * math.tau) for hue, _, _ in pixels)
    y = sum(math.sin(hue * math.tau) for hue, _, _ in pixels)
    return (math.atan2(y, x) / math.tau) % 1.0


def mean_channel(channel: int, pixels: list[tuple[float, float, float]]) -> float:
    return sum(pixel[channel] for pixel in pixels) / len(pixels)


def recolor(
        source: Image.Image,
        source_palette: tuple[float, float, float],
        target_palette: tuple[float, float, float]) -> Image.Image:
    source_hue, source_saturation, source_value = source_palette
    target_hue, target_saturation, target_value = target_palette
    converted = []
    for red, green, blue, alpha in source.convert("RGBA").getdata():
        if alpha == 0:
            converted.append((red, green, blue, alpha))
            continue

        hue, saturation, value = colorsys.rgb_to_hsv(red / 255, green / 255, blue / 255)
        is_wood = 0.035 <= hue <= 0.19 and saturation >= 0.10 and value >= 0.10
        if not is_wood:
            converted.append((red, green, blue, alpha))
            continue

        new_hue = (hue + (target_hue - source_hue)) % 1.0
        new_saturation = max(0.0, min(1.0, saturation * target_saturation / source_saturation))
        new_value = max(0.0, min(1.0, target_value * value / source_value))
        new_red, new_green, new_blue = colorsys.hsv_to_rgb(new_hue, new_saturation, new_value)
        converted.append((round(new_red * 255), round(new_green * 255), round(new_blue * 255), alpha))

    output = Image.new("RGBA", source.size)
    output.putdata(converted)
    return output


def palette(image: Image.Image) -> tuple[float, float, float]:
    pixels = plank_pixels(image)
    return mean_hue(pixels), mean_channel(1, pixels), mean_channel(2, pixels)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--templates", type=Path, required=True, help="Directory containing oak_*.png")
    parser.add_argument("--oak-planks", type=Path, required=True)
    parser.add_argument("--target-planks", type=Path, required=True)
    parser.add_argument("--wood", required=True, help="Output filename prefix")
    parser.add_argument("--output", type=Path, required=True)
    args = parser.parse_args()

    args.output.mkdir(parents=True, exist_ok=True)
    source_palette = palette(Image.open(args.oak_planks))
    target_palette = palette(Image.open(args.target_planks))
    for texture_name in TEXTURE_NAMES:
        source = Image.open(args.templates / f"oak_{texture_name}.png")
        result = recolor(source, source_palette, target_palette)
        result.save(args.output / f"{args.wood}_{texture_name}.png")


if __name__ == "__main__":
    main()
