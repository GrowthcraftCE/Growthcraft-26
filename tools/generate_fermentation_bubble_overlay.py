#!/usr/bin/env python3
"""Derive a transparent white activity mask from the fermentation GUI bubbles."""

from pathlib import Path

from PIL import Image


SOURCE = Path("src/main/resources/assets/growthcraft_cellar/textures/gui/fermentation_barrel_screen.png")
OUTPUT = Path("src/main/resources/assets/growthcraft_cellar/textures/gui/fermentation_bubbles_overlay.png")
X, Y, WIDTH, HEIGHT = 59, 20, 9, 28
BACKGROUND = (186, 186, 186, 255)


def main() -> None:
    source = Image.open(SOURCE).convert("RGBA")
    overlay = Image.new("RGBA", (WIDTH, HEIGHT), (0, 0, 0, 0))
    for y in range(HEIGHT):
        for x in range(WIDTH):
            pixel = source.getpixel((X + x, Y + y))
            if pixel != BACKGROUND and pixel[3] != 0:
                overlay.putpixel((x, y), (255, 255, 255, 255))
    overlay.save(OUTPUT)


if __name__ == "__main__":
    main()
