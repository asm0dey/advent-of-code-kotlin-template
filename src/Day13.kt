import java.io.File

fun main(args: Array<String>) {


    fun part1(input: List<String>) = input
        .chunked(4)
        .map {
            it
                .take(3)
                .map { Regex("\\d+").findAll(it).map { it.value.toInt() }.toList() }
        }
        .sumOf { (a, b, res) ->
            val (ax, ay) = a
            val (bx, by) = b
            val (resx, resy) = res
            val bsol = (resy * ax - resx * ay) / (by * ax - ay * bx)
            val asol = (resx - bsol * bx) / ax
            if (asol <= 100 && bsol <= 100 && asol * ax + bsol * bx == resx && asol * ay + bsol * by == resy) asol * 3 + bsol else 0
        }

    fun part2(input: List<String>) = input
        .chunked(4)
        .map {
            it
                .take(3)
                .map { Regex("\\d+").findAll(it).map { it.value.toLong() }.toList() }
        }
        .sumOf { (a, b, res) ->
            val (ax, ay) = a
            val (bx, by) = b
            var (resx, resy) = res
            resx += 10000000000000
            resy += 10000000000000
            val bsol = (resy * ax - resx * ay) / (by * ax - ay * bx)
            val asol = (resx - bsol * bx) / ax
            if (asol * ax + bsol * bx == resx && asol * ay + bsol * by == resy) asol * 3 + bsol else 0L
        }



    if (args.isEmpty()) {
        val testInput = readInput("13t")
        check(part1(testInput) == 480) { "Expected 480 but got ${part1(testInput)}" }

        val input = readInput("13")
        part1(input).println()
        part2(input).println()
    } else {
        val input = File(args[0]).readLines()
        part1(input).println()
        part2(input).println()
    }
}





