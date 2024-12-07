import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day07 {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      List<String> testInput = readInput("07t");
      assert part1(testInput) == 3749L : "Expected 3749 but got " + part1(testInput);

      List<String> input = readInput("07");
      System.out.println(part1(input));
      assert part2(testInput) == 11387L : "Expected 11387 but got " + part2(testInput);
      System.out.println(part2(input));
    } else {
      List<String> input = Files.readAllLines(Paths.get(args[0])).stream()
          .map(String::trim)
          .collect(Collectors.toList());
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }

  private static boolean reduce(long target, List<Integer> vars, LongStream curVal, boolean useConcat) {
    if (vars.isEmpty()) return curVal.anyMatch(it -> it == target);
    int first = vars.getFirst();
    return reduce(
        target,
        vars.subList(1, vars.size()),
        curVal
            .filter(it -> target > it)
            .flatMap(it ->
                LongStream.of(it + first, it * first, useConcat ? Long.parseLong(it + "" + first) : -1).filter(l -> l != -1)
            ),
        useConcat
    );
  }

  private static long part1(List<String> input) {
    return input.stream()
        .map(line -> line.split(":"))
        .mapToLong(parts -> {
          long result = Long.parseLong(parts[0]);
          List<Integer> vars = Arrays.stream(parts[1].trim().split(" "))
              .map(Integer::parseInt)
              .toList();
          return reduce(result, vars, LongStream.of(0), false) ? result : 0;
        })
        .sum();
  }

  private static long part2(List<String> input) {
    return input.stream()
        .map(line -> line.split(":"))
        .mapToLong(parts -> {
          long result = Long.parseLong(parts[0]);
          List<Integer> vars = Arrays.stream(parts[1].trim().split(" "))
              .map(Integer::parseInt)
              .toList();
          return reduce(result, vars, LongStream.of(0), true) ? result : 0;
        })
        .sum();
  }

  private static List<String> readInput(String filename) throws Exception {
    return Files.readAllLines(Paths.get("src/" + filename + ".txt").toAbsolutePath());
  }
}