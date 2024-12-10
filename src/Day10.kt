import java.io.File

fun main(args: Array<String>) {
    fun Grid<Char>.countPaths(start: Pair<Point2D, Char>, finish: Pair<Point2D, Char>): Int = when {
        start.first == finish.first -> 1
        start.second == '9' -> 0
        else -> mainDirections
            .filter { this[start.first + it] == start.second + 1 }
            .map { start.first + it to start.second + 1 }
            .sumOf { countPaths(it, finish) }
    }

    fun part1(input: List<String>): Int {
        val grid = input.toGrid()
        val starts = grid.filter { it.second == '0' }
        val finishes = grid.filter { it.second == '9' }
        return starts.sumOf { start -> finishes.count { finish -> grid.countPaths(start, finish) > 0 } }
    }

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()
        val starts = grid.filter { it.second == '0' }
        val finishes = grid.filter { it.second == '9' }
        return starts.sumOf { start -> finishes.sumOf { finish -> grid.countPaths(start, finish) } }
    }

    if (args.isEmpty()) {
        val testInput = readInput("10t")
        check(part1(testInput) == 36) { "Expected 36 but got ${part1(testInput)}" }

        val input = readInput("10")
        part1(input).println()
        check(part2(testInput) == 81) { "Expected 81 but got ${part2(testInput)}" }
        part2(input).println()
    } else {
        val input = File(args[0]).readLines()
        part1(input).println()
        part2(input).println()
    }
}




