import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day11 {

  private static final Map<Pair<Long, Integer>, Long> cache = new HashMap<>();

  private static long blink(long value, int iterations) {
    Pair<Long, Integer> key = new Pair<>(value, iterations);
    if (cache.containsKey(key)) return cache.get(key);
    var sol = 0L;
    if (iterations == 0) {
      sol = 1L;
    } else if (value == 0L) {
      sol = blink(1L, iterations - 1);
    } else if (String.valueOf(value).length() % 2 == 0) {
      sol = blink(split(value, String.valueOf(value).length() / 2), iterations - 1);
    } else {
      sol = blink(value * 2024, iterations - 1);
    }
    cache.put(key, sol);
    return sol;

  }

  private static long blink(List<Long> lst, int iterations) {
    if (iterations == 0) {
      return lst.size();
    } else {
      return lst.stream().mapToLong(value -> blink(value, iterations)).sum();
    }
  }

  private static List<Long> split(long value, int index) {
    String valueStr = String.valueOf(value);
    long n1 = Long.parseLong(valueStr.substring(0, index));
    long n2 = Long.parseLong(valueStr.substring(index));
    return List.of(n1, n2);
  }

  public static long part1(String input, int iterations) {
    List<Long> source = Stream.of(input.split(" "))
        .map(Long::parseLong)
        .toList();
    return blink(source, iterations);
  }

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      assert part1("125 17", 6) == 22L : "Expected 22 but got " + part1("125 17", 6);
      assert part1("125 17", 25) == 55312L : "Expected 55312 but got " + part1("125 17", 25);

      String input = Files.readString(Paths.get("src", "11.txt"));
      System.out.println(part1(input, 25));
      System.out.println(part1(input, 75));
    } else {
      String input = new File(args[0]).getAbsolutePath();
      System.out.println(part1(input, 25));
      System.out.println(part1(input, 75));
    }
  }


  private record Pair<K, V>(K key, V value) {
  }
}