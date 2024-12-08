import javautils.JavaUtils.JGrid;
import javautils.JavaUtils.JPoint2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.iterate;

public class Day08 {

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      List<String> testInput = Files.readAllLines(Path.of("src/08t.txt".trim()));
      assert part1(testInput) == 14 : "Expected 14 but got " + part1(testInput);

      List<String> input = Files.readAllLines(Path.of("src/08.txt".trim()));
      System.out.println(part1(input));
      assert part2(testInput) == 34 : "Expected 34 but got " + part2(testInput);
      System.out.println(part2(input));
    } else {
      List<String> input = Files.readAllLines(Path.of(args[0]));
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }

  public static Map<Character, List<JPoint2D>> antennas(JGrid<Character> grid) {
    return StreamSupport.stream(grid.spliterator(), false)
        .filter(antenna -> grid.get(antenna) != '.')
        .collect(Collectors.groupingBy(grid::get, mapping(identity(), toList())));
  }

  public static int part1(List<String> input) {
    final var grid = JGrid.asCharGrid(input);
    return (int) antennas(grid).values().stream()
        .flatMap(list -> range(0, list.size()).boxed()
            .flatMap(i -> range(i + 1, list.size()).boxed()
                .flatMap(j -> {
                  JPoint2D first = list.get(i);
                  JPoint2D second = list.get(j);
                  return Stream.of(
                      second.times(2).minus(first),
                      first.times(2).minus(second)
                  );
                })
            ))
        .distinct()
        .filter(grid::contains)
        .count();
  }

  public static int part2(List<String> input) {
    final var grid = JGrid.asCharGrid(input);
    return (int) antennas(grid).values().stream()
        .flatMap(list ->
            range(0, list.size()).boxed()
                .flatMap(i -> range(i + 1, list.size())
                    .boxed().flatMap(j -> {
                      JPoint2D first = list.get(i);
                      JPoint2D second = list.get(j);
                      JPoint2D diff = second.minus(first);
                      var sequence1 = iterate(second.plus(diff), grid::contains, p -> p.plus(diff));
                      var sequence2 = iterate(first.minus(diff), grid::contains, p -> p.minus(diff));
                      return concat(sequence1, sequence2, Stream.of(first, second));
                    })
                ))
        .distinct()
        .count();
  }

  @SafeVarargs
  private static <T> Stream<T> concat(Stream<T>... streams) {
    return Stream.of(streams)
        .reduce(Stream::concat)
        .orElseGet(Stream::empty);
  }

}