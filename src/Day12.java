import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static javautils.JavaUtils.*;
import static javautils.JavaUtils.JGrid.asCharGrid;

public class Day12 {

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      List<String> testInput0 = readInput("src/12t0.txt");
      assert part1(testInput0) == 140L : "Expected 140 but got " + part1(testInput0);

      List<String> testInput = readInput("src/12t.txt");
      assert part1(testInput) == 1930L : "Expected 1930 but got " + part1(testInput);

      List<String> input = readInput("src/12.txt");
      System.out.println(part1(input));
      assert part2(testInput0) == 80L : "Expected 80 but got " + part2(testInput0);
      System.out.println(part2(input));
    } else {
      List<String> input = Files.readAllLines(new File(args[0]).toPath());
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }

  private static List<String> readInput(String filename) throws IOException {
    return Files.readAllLines(new File(filename).toPath());
  }

  public static <T> Stream<Set<JPoint2D>> regions(JGrid<T> grid) {
    Set<JPoint2D> visited = new HashSet<>();

    return StreamSupport
        .stream(grid.spliterator(), false)
        .filter(start -> !visited.contains(start))
        .map(start -> {
          Set<JPoint2D> region = new HashSet<>();
          ArrayDeque<JPoint2D> toVisit = new ArrayDeque<>();
          T value = grid.get(start);

          toVisit.add(start);

          while (!toVisit.isEmpty()) {
            JPoint2D current = toVisit.pollFirst();
            if (!region.contains(current) && Objects.equals(grid.get(current), value)) {
              region.add(current);
              visited.add(current);

              for (JPoint2D direction : mainDirections) {
                JPoint2D neighbor = current.plus(direction);
                if (!visited.contains(neighbor) && grid.contains(neighbor)) {
                  toVisit.add(neighbor);
                }
              }
            }
          }

          return region;
        });
  }

  private static long part1(List<String> input) {
    return regions(asCharGrid(input))
        .mapToLong(region -> {
          int size = region.size();
          int perimeter = calculatePerimeter(region);
          return (long) size * perimeter;
        })
        .sum();
  }

  private static long part2(List<String> input) {
    return regions(asCharGrid(input))
        .mapToLong(region -> {
          int size = region.size();
          int angles = countAngles(region);
          return (long) size * angles;
        })
        .sum();
  }

  private static int calculatePerimeter(Set<JPoint2D> region) {
    int perimeter = 0;
    for (JPoint2D point : region) {
      for (JPoint2D direction : mainDirections) {
        if (!region.contains(point.plus(direction))) {
          perimeter++;
        }
      }
    }
    return perimeter;
  }

  private static int countAngles(Set<JPoint2D> region) {
    int angles = 0;
    for (JPoint2D point : region) {
      angles += Stream.of(
              new JPoint2D[]{UP, RIGHT, UP_RIGHT},
              new JPoint2D[]{UP, LEFT, UP_LEFT},
              new JPoint2D[]{DOWN, RIGHT, DOWN_RIGHT},
              new JPoint2D[]{DOWN, LEFT, DOWN_LEFT})
          .mapToInt(group -> {
            JPoint2D a = group[0].plus(point), b = group[1].plus(point), c = group[2].plus(point);
            var interim = 0;
            // two sides are absent
            if (!region.contains(a) && !region.contains(b)) interim++;
            // two sides are present and diagonal between them is absent
            if (region.contains(a) && region.contains(b) && !region.contains(c)) interim++;
            return interim;
          })
          .sum();
    }
    return angles;
  }

}