import javautils.JavaUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static javautils.JavaUtils.*;

public class Day04 {

    public static void main(String[] args) throws Exception {
        var day04 = new Day04();

        if (args.length == 0) {
            List<String> testInput = day04.readInput("04t");
            assert day04.part1(testInput) == 18 : "Test input failed for part 1";

            List<String> input = day04.readInput("04");
            System.out.println(day04.part1(input));
            assert day04.part2(testInput) == 9 : "Test input failed for part 2";
            System.out.println(day04.part2(input));
        } else {
            List<String> input = Files.readAllLines(Path.of(args[0]));
            System.out.println(day04.part1(input));
            System.out.println(day04.part2(input));
        }
    }


    private static final List<List<JPoint2D>> oppositeDirections = List.of(
        List.of(UP_RIGHT, DOWN_LEFT),
        List.of(UP_LEFT, DOWN_RIGHT)
    );

    public List<String> readInput(String fileName) throws Exception {
        return Files.readAllLines(Path.of("src/" + fileName + ".txt"));
    }

    public JGrid<Character> toGrid(List<String> input) {
        List<List<Character>> data = new ArrayList<>();
        for (String line : input) {
            List<Character> row = new ArrayList<>();
            for (char c : line.toCharArray()) {
                row.add(c);
            }
            data.add(row);
        }
        return new JGrid<>(data);
    }

    public int part1(List<String> input) {
        JGrid<Character> grid = toGrid(input);
        return (int) StreamSupport
            .stream(grid.spliterator(), false)
            .filter(point -> grid.get(point) == 'X')
            .flatMap(point -> JavaUtils.directions.stream().map(dir -> new JPoint2D[]{point, dir}))
            .filter(pair -> grid
                .beam(pair[0], pair[1], 3)
                .map(String::valueOf)
                .collect(Collectors.joining())
                .equals("MAS"))
            .count();
    }

    public int part2(List<String> input) {
        JGrid<Character> grid = toGrid(input);
        return (int) StreamSupport
            .stream(grid.spliterator(), false)
            .filter(point -> grid.get(point) == 'A')
            .filter(point -> oppositeDirections.stream()
                .map(opposite -> opposite.stream()
                    .map(point::plus)
                    .filter(grid::contains)
                    .map(grid::get)
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.joining()))
                .filter(s -> s.equals("MS"))
                .count() == 2)
            .count();
    }
}