import kotlin.io.path.Path
import kotlin.io.path.readText


fun main(args: Array<String>) {

    fun parsePageLists(input: List<String>): List<List<Int>> {
        return input.filter { it.contains(",") }.map { it.split(",").map { it.toInt() } }
    }

    fun comparator(input: List<String>): Comparator<Int> {
        val precedences = input
            .filter { it.contains("|") }
            .map { it.split("|").map { it.toInt() } }
            .groupBy({ it[0] }, { it[1] })
            .mapValues { it.value.toSet() }
        return Comparator { o1, o2 -> if (precedences[o1]?.contains(o2) == true) -1 else 1 }
    }

    fun part1(input: List<String>): Int {
        val comparator = comparator(input)
        return parsePageLists(input)
            .filter { it.windowed(2).all { (a, b) -> comparator.compare(a, b) == -1 } }
            .sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        val comparator = comparator(input)
        return parsePageLists(input)
            .filter { it.windowed(2).find { (a, b) -> comparator.compare(a, b) == 1 } != null }
            .map { it.sortedWith(comparator) }
            .sumOf { it[it.size / 2] }
    }


    // Or read a large test input from the `src/Day01_test.txt` file:
    if (args.isEmpty()) {
        val testInput = readInput("05t")
        check(part1(testInput) == 143) { "Expected 143 but got ${part1(testInput)}" }

        // Read the input from the `src/Day01.txt` file.
        val input = readInput("05")
        part1(input).println()
        check(part2(testInput) == 123) { "Expected 123 but got ${part2(testInput)}" }
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}


