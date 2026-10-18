package growthcraft.cellar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LargeBarrelModelUvTest {
    private static final Path MODELS = Path.of("src/main/resources/assets/growthcraft_cellar/models/block");
    private static final List<String> DIRECTIONS = List.of("west", "east", "down", "up", "north", "south");

    @Test
    void fermentationBarrelPartsPreserveSourceTextureAtEveryFaceCorner() throws IOException {
        verifyModels(2, "large_fermentation_barrel");
    }

    @Test
    void storageBarrelPartsPreserveSourceTextureAtEveryFaceCorner() throws IOException {
        verifyModels(3, "large_storage_barrel");
    }

    private static void verifyModels(int size, String name) throws IOException {
        JsonArray sources = read(MODELS.resolve("fermentation_barrel_oak.json")).getAsJsonArray("elements");
        for (String facing : List.of("north", "south", "east", "west")) {
            for (int y = 0; y < size; y++) {
                for (int z = 0; z < size; z++) {
                    for (int x = 0; x < size; x++) {
                        String vertical = y == 0 ? "bottom" : y == size - 1 ? "top" : "middle";
                        String depth = z == 0 ? "near" : z == size - 1 ? "far" : "center";
                        String horizontal = x == 0 ? "left" : x == size - 1 ? "right" : "center";
                        String part = vertical + "_" + (size == 3 && x == 1 && z == 1 ? "center" : depth + "_" + horizontal);
                        Path path = MODELS.resolve(name + "/" + facing + "_" + part + ".json");
                        JsonArray generated = read(path).getAsJsonArray("elements");
                        int index = 0;
                        for (var sourceValue : sources) {
                            JsonObject source = sourceValue.getAsJsonObject();
                            double[] min = vector(source, "from");
                            double[] max = vector(source, "to");
                            double[] low = {Math.max(min[0], x * 16.0 / size), Math.max(min[1], y * 16.0 / size), Math.max(min[2], z * 16.0 / size)};
                            double[] high = {Math.min(max[0], (x + 1) * 16.0 / size), Math.min(max[1], (y + 1) * 16.0 / size), Math.min(max[2], (z + 1) * 16.0 / size)};
                            if (low[0] >= high[0] || low[1] >= high[1] || low[2] >= high[2]) continue;
                            JsonObject element = generated.get(index++).getAsJsonObject();
                            JsonObject faces = element.getAsJsonObject("faces");
                            int expectedFaces = 0;
                            for (int d = 0; d < DIRECTIONS.size(); d++) {
                                String direction = DIRECTIONS.get(d);
                                int axis = d / 2;
                                boolean upper = d % 2 == 1;
                                if ((upper ? high[axis] != max[axis] : low[axis] != min[axis])) continue;
                                expectedFaces++;
                                String mapped = mappedDirection(direction, facing);
                                JsonObject face = faces.getAsJsonObject(mapped);
                                JsonObject originalFace = source.getAsJsonObject("faces").getAsJsonObject(direction);
                                String texture = originalFace.get("texture").getAsString();
                                assertEquals(texture.equals("#missing") ? "#3" : texture, face.get("texture").getAsString(), path.toString());
                                // Each original clipped corner must retain the same UV after
                                // placement in world space, including reflected barrel facings.
                                for (int a = 0; a < 2; a++) {
                                    for (int b = 0; b < 2; b++) {
                                        double[] point = low.clone();
                                        point[axis] = upper ? high[axis] : low[axis];
                                        point[(axis + 1) % 3] = a == 0 ? low[(axis + 1) % 3] : high[(axis + 1) % 3];
                                        point[(axis + 2) % 3] = b == 0 ? low[(axis + 2) % 3] : high[(axis + 2) % 3];
                                        double[] local = transform(point, facing, size);
                                        double[] origin = transform(new double[]{x * 16.0 / size, y * 16.0 / size, z * 16.0 / size}, facing, size);
                                        // Negative structure axes anchor each part at its other edge.
                                        local[0] -= origin[0] - ((facing.equals("south") || facing.equals("west")) ? 16 : 0);
                                        local[1] -= origin[1];
                                        local[2] -= origin[2] - ((facing.equals("north") || facing.equals("west")) ? 16 : 0);
                                        double[] expected = uv(source, originalFace, direction, point);
                                        double[] actual = uv(element, face, mapped, local);
                                        assertEquals(expected[0], actual[0], 0.000001, path + " " + direction + " U");
                                        assertEquals(expected[1], actual[1], 0.000001, path + " " + direction + " V");
                                    }
                                }
                            }
                            assertEquals(expectedFaces, faces.size(), path.toString());
                        }
                        assertEquals(index, generated.size(), path.toString());
                    }
                }
            }
        }
    }

    private static double[] transform(double[] p, String facing, int size) {
        double x = p[0] * size, y = p[1] * size, z = p[2] * size;
        return switch (facing) {
            case "north" -> new double[]{x, y, 16 - z};
            case "south" -> new double[]{16 - x, y, z};
            case "east" -> new double[]{z, y, x};
            case "west" -> new double[]{16 - z, y, 16 - x};
            default -> throw new IllegalArgumentException(facing);
        };
    }

    private static String mappedDirection(String direction, String facing) {
        if (direction.equals("up") || direction.equals("down")) return direction;
        int index = List.of("north", "south", "east", "west").indexOf(direction);
        return switch (facing) {
            case "north" -> List.of("south", "north", "east", "west").get(index);
            case "south" -> List.of("north", "south", "west", "east").get(index);
            case "east" -> List.of("west", "east", "south", "north").get(index);
            case "west" -> List.of("east", "west", "north", "south").get(index);
            default -> throw new IllegalArgumentException(facing);
        };
    }

    private static double[] uv(JsonObject element, JsonObject face, String direction, double[] p) {
        double[] min = vector(element, "from"), max = vector(element, "to");
        double x = (p[0] - min[0]) / (max[0] - min[0]);
        double y = (p[1] - min[1]) / (max[1] - min[1]);
        double z = (p[2] - min[2]) / (max[2] - min[2]);
        double[] st = switch (direction) {
            case "north" -> new double[]{1 - x, 1 - y};
            case "south" -> new double[]{x, 1 - y};
            case "west" -> new double[]{z, 1 - y};
            case "east" -> new double[]{1 - z, 1 - y};
            case "up" -> new double[]{x, z};
            case "down" -> new double[]{x, 1 - z};
            default -> throw new IllegalArgumentException(direction);
        };
        double s = st[0], t = st[1];
        double[] rotated = switch (face.has("rotation") ? face.get("rotation").getAsInt() : 0) {
            case 90 -> new double[]{t, 1 - s};
            case 180 -> new double[]{1 - s, 1 - t};
            case 270 -> new double[]{1 - t, s};
            default -> st;
        };
        JsonArray uv = face.getAsJsonArray("uv");
        double u0 = uv.get(0).getAsDouble(), v0 = uv.get(1).getAsDouble();
        return new double[]{u0 + (uv.get(2).getAsDouble() - u0) * rotated[0], v0 + (uv.get(3).getAsDouble() - v0) * rotated[1]};
    }

    private static double[] vector(JsonObject object, String key) {
        JsonArray values = object.getAsJsonArray(key);
        return new double[]{values.get(0).getAsDouble(), values.get(1).getAsDouble(), values.get(2).getAsDouble()};
    }

    private static JsonObject read(Path path) throws IOException {
        return JsonParser.parseString(Files.readString(path)).getAsJsonObject();
    }
}
