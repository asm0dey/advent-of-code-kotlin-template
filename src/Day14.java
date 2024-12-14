package day14;

import javautils.JavaUtils;
import javautils.JavaUtils.JGrid;
import javautils.JavaUtils.JPoint2D;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class Day14 {

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      List<String> testInput = readInput("14t.txt");
      if (part1(testInput, 11, 7) != 12)
        throw new AssertionError("Expected 12, but got " + part1(testInput, 11, 7));

      List<String> input = readInput("14.txt");
      System.out.println(part1(input, 101, 103));
      System.out.println(part2(input, 101, 103));
    } else {
      List<String> input = Files.readAllLines(new File(args[0]).toPath());
      System.out.println(part1(input, 101, 103));
      System.out.println(part2(input, 101, 103));
    }
  }

  public static int part1(List<String> input, int maxX, int maxY) {
    List<Robot> robots = parseInput(input);
    List<JPoint2D> newLocations = robots.stream()
        .map(robot -> robot.position.plus(robot.velocity.times(100)))
        .map(point -> new JPoint2D(point.x() % maxX, point.y() % maxY))
        .map(point -> new JPoint2D(
            point.x() < 0 ? maxX + point.x() : point.x(),
            point.y() < 0 ? maxY + point.y() : point.y()
        )).collect(Collectors.toList());

    Map<List<Range>, Integer> quadrants = new HashMap<>();
    quadrants.put(List.of(new Range(0, maxX / 2 - 1), new Range(0, maxY / 2 - 1)), 0);
    quadrants.put(List.of(new Range(0, maxX / 2 - 1), new Range(maxY / 2 + 1, maxY)), 0);
    quadrants.put(List.of(new Range(maxX / 2 + 1, maxX), new Range(0, maxY / 2 - 1)), 0);
    quadrants.put(List.of(new Range(maxX / 2 + 1, maxX), new Range(maxY / 2 + 1, maxY)), 0);

    for (JPoint2D location : newLocations) {
      int x = location.x();
      int y = location.y();
      for (Map.Entry<List<Range>, Integer> quadrant : quadrants.entrySet()) {
        Range xRange = quadrant.getKey().get(0);
        Range yRange = quadrant.getKey().get(1);
        if (xRange.contains(x) && yRange.contains(y)) {
          quadrants.put(quadrant.getKey(), quadrant.getValue() + 1);
          break;
        }
      }
    }

    return quadrants.values().stream().reduce(1, (a, b) -> a * b);
  }

  public static long part2(List<String> input, int maxX, int maxY) {
    List<Robot> robots = parseInput(input);
    long counter = 1;

    while (true) {
      robots = robots.stream()
          .map(robot -> new Robot(
              robot.position.plus(robot.velocity),
              robot.velocity
          ))
          .map(robot -> new Robot(
              new JPoint2D(
                  robot.position.x() % maxX,
                  robot.position.y() % maxY
              ), robot.velocity
          ))
          .map(robot -> new Robot(
              new JPoint2D(
                  robot.position.x() < 0 ? maxX + robot.position.x() : robot.position.x(),
                  robot.position.y() < 0 ? maxY + robot.position.y() : robot.position.y()
              ), robot.velocity
          ))
          .toList();

      JGrid<Character> grid = new JGrid<>(robots.stream()
          .collect(Collectors.toMap(Robot::position, _ -> '*', (_, _) -> '*')));

      JGrid<Character> filteredGrid = new JGrid<>(
          StreamSupport.stream(grid.spliterator(), false)
              .filter(entry -> (grid.contains(entry.plus(JavaUtils.LEFT)) || grid.contains(entry.plus(JavaUtils.RIGHT)))
                  && (grid.contains(entry.plus(JavaUtils.UP)) || grid.contains(entry.plus(JavaUtils.DOWN))))
              .collect(Collectors.toMap(Function.identity(), _ -> '*'))
      );

      OptionalInt minY = StreamSupport.stream(filteredGrid.spliterator(), false)
          .mapToInt(JPoint2D::y).min();
      OptionalInt minX = StreamSupport.stream(filteredGrid.spliterator(), false)
          .mapToInt(JPoint2D::x).min();
      OptionalInt max = StreamSupport.stream(filteredGrid.spliterator(), false)
          .mapToInt(JPoint2D::x).max();

      if (minY.isEmpty() || minX.isEmpty() || max.isEmpty()) {
        counter++;
        continue;
      }

      boolean containsVertical = IntStream.rangeClosed(minX.getAsInt(), max.getAsInt()).anyMatch(x -> {
        long count = filteredGrid.beam(
            new JPoint2D(x, minY.getAsInt()),
            JavaUtils.DOWN,
            maxY
        ).count();
        return count > 10;
      });

      if (containsVertical) {
        System.out.println("Seconds: " + counter);
        grid.print();
        return counter;
      }

      counter++;
    }
  }

  private static List<Robot> parseInput(List<String> input) {
    return input.stream()
        .map(line -> {
          var res = Pattern.compile("-?\\d+").matcher(line).results().toList();

          String[] parts = line.split(" ");
          int px = Integer.parseInt(res.get(0).group());
          int py = Integer.parseInt(res.get(1).group());
          int vx = Integer.parseInt(res.get(2).group());
          int vy = Integer.parseInt(res.get(3).group());
          return new Robot(new JPoint2D(px, py), new JPoint2D(vx, vy));
        }).collect(Collectors.toList());
  }

  private static List<String> readInput(String fileName) throws IOException {
    return Files.readAllLines(new File("src/" + fileName).toPath());
  }

  private record Robot(JPoint2D position, JPoint2D velocity) {
  }

  private static class Range {
    private final int start, end;

    public Range(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public boolean contains(int value) {
      return value >= start && value <= end;
    }
  }
}