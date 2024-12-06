import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day01 {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            List<String> testInput = readInput("Day01_test");
            assert part1(testInput) == 11;

            List<String> input = readInput("Day01");
            System.out.println(part1(input));
            System.out.println(part2(input));
        } else {
            List<String> lines = Files.lines(Paths.get(args[0])).toList();
            System.out.println(part1(lines));
            System.out.println(part2(lines));
        }
    }

    public static List<int[]> parseDay1(List<String> input) {
        return input.stream()
                .map(line -> line.split(" "))
                .map(it -> Stream.of(it).filter(x -> !x.isBlank()).map(Integer::parseInt).toList())
                .map(parts -> new int[]{parts.getFirst(), parts.get(1)})
                .collect(Collectors.toList());
    }

    public static int part1(List<String> input) {
        List<int[]> pairs = parseDay1(input);
        List<Integer> sortedLeft = pairs.stream().map(p -> p[0]).sorted().toList();
        List<Integer> sortedRight = pairs.stream().map(p -> p[1]).sorted().toList();
        var result = 0;
        for (int i = 0; i < sortedLeft.size(); i++) {
            var l = sortedLeft.get(i);
            var r = sortedRight.get(i);
            result += Math.abs(l - r);
        }
        return result;
    }

    public static long part2(List<String> input) {
        List<int[]> pairs = parseDay1(input);
        List<Integer> lefts = pairs.stream().map(p -> p[0]).sorted().toList();
        Map<Integer, Long> counts = pairs.stream().map(p -> p[1]).collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        long sum = 0;
        for (Integer left : lefts) {
            sum += left * counts.getOrDefault(left, 0L);
        }
        return sum;
    }

    public static List<String> readInput(String name) throws IOException {
        return Files.readAllLines(Paths.get("src/" + name + ".txt"));
    }
}
