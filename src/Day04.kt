import kotlin.io.path.Path
import kotlin.io.path.readText

fun main(args: Array<String>) {
    val oppositeDirections = listOf(listOf(UP_RIGHT, DOWN_LEFT), listOf(UP_LEFT, DOWN_RIGHT))

    fun part1(input: List<String>, grid: Grid<Char> = input.toGrid()) = grid
        .asSequence()
        .filter { (_, char) -> char == 'X' }
        .flatMap { (point, _) -> directions.map { point to it } }
        .filter { (point, direction) ->
            grid.beam(point, direction, 3).joinToString("") == "MAS"
        }
        .count()

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()
        return grid
            .asSequence()
            .filter { (_, char) -> char == 'A' }
            .filter { (point, _) ->
                oppositeDirections
                    .asSequence()
                    .map {
                        it
                            .asSequence()
                            .map(point::plus)
                            .filter(grid::contains)
                            .map(grid::get)
                            .sorted()
                            .joinToString("")
                    }
                    .filter { it == "MS" }
                    .count() == 2
            }
            .count()
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    if (args.isEmpty()) {
        val testInput = readInput("04t")
        check(part1(testInput) == 18)

        // Read the input from the `src/Day01.txt` file.
        val input = readInput("04")
        part1(input).println()
        check(part2(testInput) == 9)
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}


