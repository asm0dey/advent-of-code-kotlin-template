import javautils.JavaUtils;
import javautils.JavaUtils.JGrid;
import javautils.JavaUtils.JPoint2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {

  public static List<JPoint2D> path(JGrid<Character> grid, JPoint2D start, JPoint2D exit) {
    ArrayDeque<Pair<JPoint2D, List<JPoint2D>>> queue = new ArrayDeque<>();
    Set<JPoint2D> visited = new HashSet<>();

    queue.add(new Pair<>(start, List.of(start)));
    visited.add(start);

    while (!queue.isEmpty()) {
      Pair<JPoint2D, List<JPoint2D>> currentPair = queue.pollFirst();
      JPoint2D current = currentPair.key();
      List<JPoint2D> path = currentPair.value();

      if (current.equals(exit)) return path;

      for (JPoint2D dir : JavaUtils.mainDirections) {
        JPoint2D next = current.plus(dir);
        if (grid.contains(next) && grid.get(next) == '.' && !visited.contains(next)) {
          visited.add(next);
          List<JPoint2D> newPath = new ArrayList<>(path);
          newPath.add(next);
          queue.add(new Pair<>(next, newPath));
        }
      }
    }

    return Collections.emptyList();
  }
  private static void fillGrid(JPoint2D exit, JGrid<Character> grid) {
    for (int i = 0; i <= exit.x(); i++) {
      for (int j = 0; j <= exit.y(); j++) {
        JPoint2D point = new JPoint2D(i, j);
        if (!grid.contains(point)) grid.set(point, '.');
      }
    }
  }
  public static int part1(List<String> input, int byteNumber, JPoint2D exit) {
    Map<JPoint2D, Character> initialMap = input.stream()
        .filter(line -> !line.isBlank())
        .limit(byteNumber)
        .map(line -> Arrays.stream(line.split(","))
            .map(Integer::parseInt)
            .toList())
        .map(coordinates -> new JPoint2D(coordinates.getFirst(), coordinates.get(1)))
        .collect(Collectors.toMap(point -> point, _ -> '#'));

    JGrid<Character> grid = new JGrid<>(initialMap);

    fillGrid(exit, grid);

    List<JPoint2D> path = path(grid, new JPoint2D(0, 0), exit);
    return path.size() - 1;
  }



  public static JPoint2D part2(List<String> input, JPoint2D exit) {
    Map<JPoint2D, Character> fullList = input.stream()
        .filter(line -> !line.isBlank())
        .map(line -> Arrays.stream(line.split(","))
            .map(Integer::parseInt)
            .toList())
        .map(coordinates -> new JPoint2D(coordinates.getFirst(), coordinates.get(1)))
        .collect(Collectors.toMap(point -> point, _ -> '#', (v1, _) -> v1, LinkedHashMap::new));

    var grid = new JGrid<Character>(List.of(List.of()));

    fillGrid(exit, grid);

    for (Map.Entry<JPoint2D, Character> entry : fullList.entrySet()) {
      grid.set(entry.getKey(), entry.getValue());
      if (path(grid, new JPoint2D(0, 0), exit).isEmpty()) {
        return entry.getKey();
      }
    }

    throw new IllegalStateException("No solution found");
  }

  public static void main(String[] ignoredArgs) throws IOException {
    List<String> testInput = readInput("18t"); // Implement readInput method as needed
    System.out.println(part1(testInput, 12, new JPoint2D(6, 6)));

    List<String> input = readInput("18");
    System.out.println(part1(input, 1024, new JPoint2D(70, 70)));
    System.out.println(part2(testInput, new JPoint2D(6, 6)));
    System.out.println(part2(input, new JPoint2D(70, 70)));
  }

  public static List<String> readInput(String filename) throws IOException {
    return Files.lines(Paths.get("src", filename + ".txt")).toList();
  }

  public record Pair<K, V>(K key, V value) {
  }
}