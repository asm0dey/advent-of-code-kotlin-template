import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.math.sign

fun main(args: Array<String>) {
    fun parseDay1(input: List<String>) = input
        .map { it.split(" ").map(String::toInt) }

    fun validReport(report: List<Int>): Boolean {
        val dir = (report[1] - report[0]).sign
        if (dir == 0) return false
        for (i in 1 until report.size) {
            val diff = report[i] - report[i - 1]
            if (abs(diff) > 3 || diff.sign != dir.sign)
                return false
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val reports = parseDay1(input)
        return reports.count(::validReport)
    }

    fun part2(input: List<String>): Int {
        val reports = sequence {
            val data = parseDay1(input)
            for (report in data) {
                yield(sequence {
                    yield(report)
                    yieldAll(report.indices.asSequence().map { index ->
                        report.toMutableList().also { it.removeAt(index) }.toList()
                    })
                })
            }
        }

        return reports.count {
            it.any(::validReport)
        }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    if (args.isEmpty()) {
        val testInput = readInput("2t")
        check(part1(testInput) == 2)

        // Read the input from the `src/Day01.txt` file.
        val input = readInput("2")
        part1(input).println()
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}


