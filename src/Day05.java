import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.*;
import static java.util.stream.Gatherers.windowSliding;

public class Day05 {

  private static List<List<Integer>> parsePageLists(List<String> input) {
    return input.stream()
        .filter(line -> line.contains(","))
        .map(line -> Arrays
            .stream(line.split(","))
            .map(Integer::parseInt)
            .toList())
        .toList();
  }

  @SuppressWarnings("ComparatorMethodParameterNotUsed")
  private static Comparator<Integer> comparator(List<String> input) {
    Map<Integer, Set<Integer>> precedences = input.stream()
        .filter(line -> line.contains("|"))
        .map(line -> Arrays
            .stream(line.split("\\|"))
            .map(Integer::parseInt)
            .toArray(Integer[]::new))
        .collect(groupingBy(
            arr -> arr[0],
            mapping(arr -> arr[1], toSet())
        ));

    return (o1, o2) -> precedences.getOrDefault(o1, emptySet()).contains(o2) ? -1 : 1;
  }

  private static int part1(List<String> input) {
    Comparator<Integer> comparator = comparator(input);
    return parsePageLists(input)
        .stream()
        .filter(list -> list
            .stream()
            .gather(windowSliding(2))
            .noneMatch(i -> comparator.compare(i.getFirst(), i.get(1)) >= 0))
        .mapToInt(list -> list.get(list.size() / 2))
        .sum();
  }

  private static int part2(List<String> input) {
    Comparator<Integer> comparator = comparator(input);
    return parsePageLists(input).stream()
        .filter(list -> list
            .stream()
            .gather(windowSliding(2))
            .anyMatch(i -> comparator.compare(i.getFirst(), i.get(1)) >= 0))
        .map(list -> list.stream().sorted(comparator).toList())
        .mapToInt(list -> list.get(list.size() / 2))
        .sum();
  }

  private static List<String> readInput(String filename) throws IOException {
    return Files.lines(Path.of(filename)).toList();
  }

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      List<String> testInput = readInput("src/05t.txt");
      assert part1(testInput) == 143 : "Expected 143 but got " + part1(testInput);
      List<String> input = readInput("src/05.txt");
      System.out.println(part1(input));
      assert part2(testInput) == 123 : "Expected 123 but got " + part2(testInput);
      System.out.println(part2(input));
    } else {
      List<String> input = Files.readAllLines(Path.of(args[0]));
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }
}