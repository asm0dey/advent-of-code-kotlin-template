import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
import static java.lang.String.join;
import static java.util.stream.Gatherers.fold;

public class Day03 {

    public static void main(String[] args) throws IOException {
        Path path = args.length == 0 ? Paths.get("src", "03.txt") : Paths.get(args[0]);
        List<String> input = Files.readAllLines(path);
        System.out.println(part1(input));
        System.out.println(part2(input));
    }

    public static long part1(List<String> input) {
        String inputAsString = join("\n", input);
        Pattern pattern = Pattern.compile("mul\\((-?\\d+),(-?\\d+)\\)");
        Matcher matcher = pattern.matcher(inputAsString);
        long sum = 0;
        while (matcher.find()) {
            long num1 = parseLong(matcher.group(1));
            long num2 = parseLong(matcher.group(2));
            sum += num1 * num2;
        }
        return sum;
    }

    @SuppressWarnings("preview")
    public static long part2(List<String> input) {
        record Pair(boolean proceed, long accumulator) {
        }
        //noinspection OptionalGetWithoutIsPresent
        return Pattern
            .compile("(mul\\((-?\\d+),(-?\\d+)\\))|(do(?:n't|)\\(\\))")
            .matcher(join("\n", input))
            .results()
            .gather(fold(() -> new Pair(true, 0), (state, matchResult) -> {
                String match = matchResult.group(0);
                return new Pair(
                    isDo(match) || (isMultiplication(match) && state.proceed),
                    isMultiplication(match) && state.proceed
                        ? state.accumulator + parseLong(matchResult.group(2)) * parseLong(matchResult.group(3))
                        : state.accumulator
                );
            }))
            .map(it -> it.accumulator)
            .findFirst()
            .get();
    }

    private static boolean isDo(String match) {
        return "do()".equals(match);
    }

    private static boolean isMultiplication(String match) {
        return match.startsWith("mul");
    }

}
