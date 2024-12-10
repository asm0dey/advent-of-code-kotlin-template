import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.stream.StreamSupport.stream;
import static javautils.JavaUtils.*;
import static javautils.JavaUtils.JGrid.asCharGrid;

public class Day10 {

  public static int countPaths(JGrid<Character> grid, Entry<JPoint2D, Character> start, Entry<JPoint2D, Character> finish) {
    if (start.getKey().equals(finish.getKey())) return 1;
    if (start.getValue() == '9') return 0;
    int count = 0;
    for (JPoint2D direction : mainDirections) {
      JPoint2D newPoint = start.getKey().plus(direction);
      if (grid.contains(newPoint) && grid.get(newPoint) == start.getValue() + 1) {
        count += countPaths(grid, new AbstractMap.SimpleEntry<>(newPoint, (char) (start.getValue() + 1)), finish);
      }
    }
    return count;
  }

  public static int part1(List<String> input) {
    JGrid<Character> grid = asCharGrid(input);
    Stream<JPoint2D> starts = stream(grid.spliterator(), false).filter(it -> grid.get(it) == '0');
    List<JPoint2D> finishes = stream(grid.spliterator(), false).filter(it -> grid.get(it) == '9').toList();
    return starts
        .mapToInt(start ->
            (int) finishes
                .stream()
                .filter(finish -> countPaths(grid, entry(start, grid.get(start)), entry(finish, grid.get(finish))) > 0)
                .count()
        )
        .sum();
  }

  public static int part2(List<String> input) {
    JGrid<Character> grid = asCharGrid(input);
    Stream<JPoint2D> starts = stream(grid.spliterator(), false).filter(it -> grid.get(it) == '0');
    List<JPoint2D> finishes = stream(grid.spliterator(), false).filter(it -> grid.get(it) == '9').toList();
    return starts
        .mapToInt(start ->
            finishes
                .stream()
                .mapToInt(finish -> countPaths(grid, entry(start, grid.get(start)), entry(finish, grid.get(finish))))
                .sum()
        )
        .sum();
  }

  public static List<String> readInput(String filename) throws IOException {
    return Files.readAllLines(Paths.get("src", filename + ".txt"));
  }

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      List<String> testInput = readInput("10t");
      assert part1(testInput) == 36 : "Expected 36 but got " + part1(testInput);

      List<String> input = readInput("10");
      System.out.println(part1(input));
      assert part2(testInput) == 81 : "Expected 81 but got " + part2(testInput);
      System.out.println(part2(input));
    } else {
      List<String> input = Files.readAllLines(Paths.get(args[0]));
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }
}