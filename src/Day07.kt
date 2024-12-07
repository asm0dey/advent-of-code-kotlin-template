import kotlin.io.path.Path
import kotlin.io.path.readText


fun main(args: Array<String>) {

    tailrec fun reduce(target: Long, vars: List<Int>, curVal: Sequence<Long>, useConcat: Boolean = false): Boolean {
        if (vars.isEmpty()) return curVal.any { it == target }
        return reduce(
            target,
            vars.subList(1, vars.size),
            curVal
                .filter { target > it }
                .flatMap {
                    sequence {
                        yield(it + vars.first())
                        yield(it * vars.first())
                        if (useConcat) yield("$it${vars.first()}".toLong())
                    }
                },
            useConcat
        )
    }

    fun solve(input: List<String>, useConcat: Boolean) = input
        .asSequence()
        .map { it.split(':').first().toLong() to it.split(' ').drop(1).map(String::toInt) }
        .sumOf { (result, vars) ->
            if (reduce(result, vars, sequenceOf(0), useConcat)) result else 0
        }

    fun part1(input: List<String>): Long = solve(input, false)

    fun part2(input: List<String>): Long = solve(input, true)

    if (args.isEmpty()) {
        val testInput = readInput("07t")
        check(part1(testInput) == 3749L) { "Expected 3749 but got ${part1(testInput)}" }

        val input = readInput("07")
        part1(input).println()
        check(part2(testInput) == 11387L) { "Expected 11387 but got ${part2(testInput)}" }
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}


