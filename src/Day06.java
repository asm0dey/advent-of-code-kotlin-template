import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static javautils.JavaUtils.*;

public class Day06 {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      List<String> testInput = Files.readAllLines(Paths.get("src/06t.txt"));
      assert part1(testInput) == 41 : "Expected 41 but got " + part1(testInput);

      List<String> input = Files.readAllLines(Paths.get("src/06.txt"));
      System.out.println(part1(input));
      assert part2(testInput) == 6 : "Expected 6 but got " + part2(testInput);
      System.out.println(part2(input));
    } else {
      List<String> input = Files.readAllLines(Paths.get(args[0]));
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }

  private static int part1(List<String> input) {
    JGrid<Character> grid = toGrid(input);
    JPoint2D startingPoint = getStartingPoint(grid);
    return walk(grid, startingPoint).size();
  }

  private static int part2(List<String> input) {
    JGrid<Character> grid = toGrid(input);
    JPoint2D startingPoint = getStartingPoint(grid);
    Set<JPoint2D> initialWalk = new HashSet<>(walk(grid, startingPoint));

    return (int) initialWalk
        .stream()
        .filter(toReplace -> {
          var modifiedGrid = copyAndReplace(grid, toReplace, '#');
          JPoint2D curPoint = startingPoint;
          JPoint2D curDirection = UP;
          Set<Entry<JPoint2D, JPoint2D>> visited = new HashSet<>();
          while (true) {
            var next = next(modifiedGrid, curPoint, curDirection);
            if (next == null) return false;
            if (!visited.add(next)) break;
            curPoint = next.getKey();
            curDirection = next.getValue();
          }
          return true;
        })
        .count();
  }

  private static Entry<JPoint2D, JPoint2D> next(JGrid<Character> grid, JPoint2D point, JPoint2D direction) {
    JPoint2D nextDirection = direction;
    if (Objects.equals(grid.get(point.plus(direction)), '#')) {
      nextDirection = turn(direction);
      if (Objects.equals(grid.get(point.plus(nextDirection)), '#')) {
        nextDirection = turn(nextDirection);
      }
    }
    JPoint2D nextPoint = point.plus(nextDirection);
    return grid.contains(nextPoint) ? Map.entry(nextPoint, nextDirection) : null;
  }

  private static JPoint2D turn(JPoint2D direction) {
    if (UP.equals(direction)) return RIGHT;
    else if (RIGHT.equals(direction)) return DOWN;
    else if (DOWN.equals(direction)) return LEFT;
    else return UP; // LEFT case
  }

  private static Set<JPoint2D> walk(JGrid<Character> grid, JPoint2D startingPoint) {
    Set<JPoint2D> visited = new HashSet<>();
    visited.add(startingPoint);
    JPoint2D nextPoint = startingPoint;
    JPoint2D direction = UP;

    while (true) {
      Entry<JPoint2D, JPoint2D> next = next(grid, nextPoint, direction);
      if (next == null) break;
      nextPoint = next.getKey();
      direction = next.getValue();
      visited.add(nextPoint);
    }

    return visited;
  }

  private static JPoint2D getStartingPoint(JGrid<Character> grid) {
    return StreamSupport.stream(grid.spliterator(), false)
        .filter(e -> grid.get(e) == '^')
        .map(e -> new JPoint2D(e.x(), e.y()))
        .findFirst()
        .orElseThrow();
  }

  private static JGrid<Character> toGrid(List<String> input) {
    List<List<Character>> gridData = input
        .stream()
        .map(line -> line.chars().mapToObj(c -> (char) c).collect(Collectors.toList()))
        .collect(Collectors.toList());
    return new JGrid<>(gridData);
  }

  private static JGrid<Character> copyAndReplace(JGrid<Character> grid, JPoint2D toReplace, char ch) {
    var newGrid = new JGrid<>(new HashMap<>(grid.data()));
    newGrid.set(toReplace, ch);
    return newGrid;
  }
}