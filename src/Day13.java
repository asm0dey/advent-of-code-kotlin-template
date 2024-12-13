import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day13 {

  public static long part1(List<String> input) {
    // Process the input in chunks of 4 lines
    List<List<List<Long>>> chunks = new ArrayList<>();
    for (int i = 0; i < input.size(); i += 4) {
      List<List<Long>> chunk = input.subList(i, Math.min(i + 3, input.size())).stream().map(Day13::extractLongs).collect(Collectors.toList());
      chunks.add(chunk);
    }

    // Perform the calculations
    return chunks.stream().mapToLong(chunk -> {
      List<Long> a = chunk.get(0);
      List<Long> b = chunk.get(1);
      List<Long> res = chunk.get(2);

      long ax = a.get(0), ay = a.get(1);
      long bx = b.get(0), by = b.get(1);
      long resx = res.get(0), resy = res.get(1);

      long bsol = (resy * ax - resx * ay) / (by * ax - ay * bx);
      long asol = (resx - bsol * bx) / ax;

      return asol <= 100 && bsol <= 100 &&
          asol * ax + bsol * bx == resx &&
          asol * ay + bsol * by == resy
          ? asol * 3 + bsol : 0;
    }).sum();
  }

  public static long part2(List<String> input) {
    List<List<List<Long>>> chunks = new ArrayList<>();
    for (int i = 0; i < input.size(); i += 4) {
      List<List<Long>> chunk = input.subList(i, Math.min(i + 3, input.size())).stream().map(Day13::extractLongs).toList();
      chunks.add(chunk);
    }

    return chunks.stream().mapToLong(chunk -> {
      List<Long> a = chunk.get(0);
      List<Long> b = chunk.get(1);
      List<Long> res = chunk.get(2);

      long ax = a.get(0), ay = a.get(1);
      long bx = b.get(0), by = b.get(1);
      long resx = res.get(0) + 10000000000000L, resy = res.get(1) + 10000000000000L;

      long bsol = (resy * ax - resx * ay) / (by * ax - ay * bx);
      long asol = (resx - bsol * bx) / ax;

      return asol * ax + bsol * bx == resx &&
          asol * ay + bsol * by == resy
          ? asol * 3 + bsol : 0L;
    }).sum();
  }

  public static List<String> readInput(String fileName) throws IOException {
    return Files.readAllLines(Paths.get("src/" + fileName + ".txt"));
  }


  private static List<Long> extractLongs(String line) {
    List<Long> numbers = new ArrayList<>();
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher = pattern.matcher(line);
    while (matcher.find()) {
      numbers.add(Long.parseLong(matcher.group()));
    }
    return numbers;
  }

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      List<String> testInput = readInput("13t");
      assert part1(testInput) == 480L : "Expected 480 but got " + part1(testInput);

      List<String> input = readInput("13");
      System.out.println(part1(input));
      System.out.println(part2(input));
    } else {
      List<String> input = Files.readAllLines(Paths.get(args[0]));
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }
}