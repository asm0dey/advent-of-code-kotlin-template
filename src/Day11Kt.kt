import java.io.File

object Day11Kt {
    private val cache = hashMapOf<Pair<Long, Int>, Long>()
    private fun blink(value: Long, iterations: Int): Long {
        return cache.getOrPut(value to iterations) {
            when {
                iterations == 0 -> 1
                value == 0L -> blink(1L, iterations - 1)
                value.toString().length % 2 == 0 -> blink(
                    split(value, value.toString().length / 2),
                    iterations - 1
                )

                else -> blink((value * 2024), iterations - 1)
            }
        }
    }

    private fun blink(lst: List<Long>, iterations: Int): Long {
        return when (iterations) {
            0 -> lst.size.toLong()
            else -> lst.sumOf { blink(it, iterations) }
        }
    }

    private fun split(value: Long, i: Int): List<Long> {
        val n1 = value.toString().substring(0, i).toLong()
        val n2 = value.toString().substring(i).toLong()
        return listOf(n1, n2)
    }

    private fun part1(input: String, iterations: Int): Long {
        val blink = blink(input.split(" ").map { it.toLong() }, iterations)
        return blink
    }

    @JvmStatic
    fun main(args: Array<String>) {

        if (args.isEmpty()) {
//        val testInput = readInput("11t").joinToString("")
            check(part1("125 17", 6) == 22L) { "Expected 22 but got ${part1("125 17", 6)}" }
            check(part1("125 17", 25) == 55312L) { "Expected 55312 but got ${part1("125 17", 25)}" }

            val input = readInput("11").joinToString("")
            part1(input, 25).println()
            part1(input, 75).println()
        } else {
            val input = File(args[0]).readText()
            part1(input, 25).println()
            part1(input, 75).println()
        }
    }
}