import kotlin.io.path.Path
import kotlin.io.path.readText


fun main(args: Array<String>) {


    fun antennas(grid: Grid<Char>): Map<Char, List<Point2D>> {
        return grid
            .asSequence()
            .filter { (_, c) -> c != '.' }
            .groupBy { it.second }
            .mapValues { it.value.map { it.first } }
    }

    fun part1(input: List<String>): Int {
        val grid = input.toGrid()
        return antennas(grid)
            .values
            .flatMap {
                it.flatMapIndexed { i, first ->
                    (i + 1 until it.size).flatMap { j ->
                        val second = it[j]
                        listOf(second * 2 - first, first * 2 - second)
                    }
                }
            }
            .distinct()
            .count { it in grid }
    }

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()
        return antennas(grid)
            .values
            .flatMap {
                it.flatMapIndexed { i, first ->
                    (i + 1 until it.size).flatMap { j ->
                        val second = it[j]
                        val diff = second - first
                        val sequence1 = generateSequence(second + diff) { it + diff }.takeWhile { it in grid }
                        val sequence2 = generateSequence(first - diff) { it - diff }.takeWhile { it in grid }
                        sequence1 + sequence2 + sequenceOf(first, second)
                    }
                }
            }
            .distinct()
            .size
    }

    if (args.isEmpty()) {
        val testInput = readInput("08t")
        check(part1(testInput) == 14) { "Expected 14 but got ${part1(testInput)}" }

        val input = readInput("08")
        part1(input).println()
        check(part2(testInput) == 34) { "Expected 34 but got ${part2(testInput)}" }
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}


