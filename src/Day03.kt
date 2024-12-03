import kotlin.io.path.Path
import kotlin.io.path.readText

fun main(args: Array<String>) {

    fun part1(input: List<String>): Long {
        return Regex("mul\\((-?\\d+),(-?\\d+)\\)").findAll(input.joinToString("")).sumOf {
            it.groupValues[1].toLong() * it.groupValues[2].toLong()
        }
    }

    fun part2(input: List<String>) =
        Regex("(mul\\((-?\\d+),(-?\\d+)\\))|((?:do|don't)\\(\\))")
            .findAll(input.joinToString(""))
            .fold(0L to true) { (acc, state), matchResult ->
                when (matchResult.value) {
                    "don't()" -> acc to false
                    "do()" -> acc to true
                    else ->
                        if (!state) acc to false
                        else (acc + (matchResult.groupValues[2].toLong() * matchResult.groupValues[3].toLong())) to true
                }
            }
            .first
    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    if (args.isEmpty()) {
//        val testInput = readInput("2t")
//        check(part1(testInput) == 2)

        // Read the input from the `src/Day01.txt` file.
        val input = readInput("03")
        part1(input).println()
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}


