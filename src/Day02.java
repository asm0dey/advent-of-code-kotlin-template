import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import static java.lang.Integer.signum;
import static java.lang.Math.abs;
import static java.nio.file.Files.readAllLines;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.concat;

public class Day02 {
    public static void main(String[] args) throws Exception {
        List<String> input;
        if (args.length == 0) {
            assert part1(readAllLines(Path.of("src/2t.txt"))) == 2;
            input = readAllLines(Path.of("src/2.txt"));
        } else input = readAllLines(Path.of(args[0]));
        System.out.println(part1(input));
        System.out.println(part2(input));
    }

    private static Stream<Integer[]> parse(List<String> input) {
        return input
            .stream()
            .map(line -> Stream
                .of(line.split(" "))
                .map(Integer::parseInt)
                .toArray(Integer[]::new));
    }

    @SuppressWarnings("preview")
    private static boolean validReport(Integer[] report) {
        int dir = signum(report[1] - report[0]);
        if (dir == 0) return false;
        return Stream
            .of(report)
            .gather(Gatherers.windowSliding(2))
            .noneMatch(l -> {
                int diff = l.get(1) - l.get(0);
                return abs(diff) > 3 || signum(diff) != dir;
            });
    }

    private static int part1(List<String> input) {
        return (int) parse(input).filter(Day02::validReport).count();
    }

    private static int part2(List<String> input) {
        return (int)
            parse(input)
                .filter(report -> concat(
                    Stream.ofNullable(report),
                    range(0, report.length)
                        .mapToObj(index -> withIndexRemoved(report, index))
                )
                    .anyMatch(Day02::validReport))
                .count();
    }

    private static Integer[] withIndexRemoved(Integer[] report, int index) {
        var modifiedReport = new ArrayList<>(Arrays.asList(report));
        modifiedReport.remove(index);
        return modifiedReport.toArray(Integer[]::new);
    }

}