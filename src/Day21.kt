package aockt.y2024

import DOWN
import LEFT
import P2
import RIGHT
import UP
import println
import readInput


/**
 * A keypad with buttons.
 * @param blank   The Point missing from the key pad grid.
 * @param buttons Button labels associated with their grid location.
 */
private sealed class JKeyPad(
    private val blank: P2,
    private val buttons: Map<Char, P2>,
) {

    /** A numerical keypad for doors. */
    data object Numerical : JKeyPad(
        blank = P2(0, 0),
        buttons = mapOf(
            '0' to P2(1, 0),
            'A' to P2(2, 0),
            '1' to P2(0, 1),
            '2' to P2(1, 1),
            '3' to P2(2, 1),
            '4' to P2(0, 2),
            '5' to P2(1, 2),
            '6' to P2(2, 2),
            '7' to P2(0, 3),
            '8' to P2(1, 3),
            '9' to P2(2, 3),
        ),
    )

    /** A directional keypad for keypad-operating robots. */
    data object Directional : JKeyPad(
        blank = P2(0, 1),
        buttons = mapOf(
            '<' to P2(0, 0),
            'v' to P2(1, 0),
            '>' to P2(2, 0),
            '^' to P2(1, 1),
            'A' to P2(2, 1),
        )
    )

    /** Return the optimal moves required to move to another button and press it. */
    fun move(from: Char, to: Char): String {
        val start = buttons.getValue(from)
        val end = buttons.getValue(to)

        fun recurse(p2: P2, acc: String): Sequence<String> = sequence {
            if (p2 == end) yield(acc + "A")
            val left = p2 + LEFT
            val right = p2 + RIGHT
            val down = p2 + DOWN
            val up = p2 + UP
            if (p2.x > end.x && left != blank) yieldAll(recurse(left, "$acc<"))
            if (p2.x < end.x && right != blank) yieldAll(recurse(right, "$acc>"))
            if (p2.y < end.y && down != blank) yieldAll(recurse(down, "$acc^"))
            if (p2.y > end.y && up != blank) yieldAll(recurse(up, "${acc}v"))
        }

        return recurse(start, "").minBy { path -> path.zipWithNext().count { it.first != it.second } }
    }

    /** Return the optimal moves required to type out this [code]. */
    fun type(code: String): String =
        "A$code"
            .windowed(size = 2)
            .joinToString(separator = "") { move(it[0], it[1]) }
}


/** A three-digit door code. */
@JvmInline
private value class CodeD21(private val value: String) : CharSequence by value {
    /**
     * Splits this string into distinct segments and returns their frequencies.
     * A segment is a series of directions followed by an 'A'.
     */
    private fun String.segmentFrequency(): Map<String, Long> =
        removeSuffix("A")
            .split("A")
            .map { it + 'A' }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

    init {
        val regex = Regex("""^\d{3}A""")
        require(value.matches(regex)) { "Invalid code." }
    }

    /** Calculates the complexity of using different layers of [robots] to type out this code. */
    fun complexity(robots: Int = 0): Long {
        var segmentFrequency: Map<String, Long> = JKeyPad.Numerical.type(value).segmentFrequency()

        repeat(robots) {
            segmentFrequency = buildMap {
                for ((segment, count) in segmentFrequency) {
                    for ((seg, times) in JKeyPad.Directional.type(segment).segmentFrequency()) {
                        compute(seg) { _, old -> (old ?: 0) + count * times }
                    }
                }
            }
        }

        val minLength = segmentFrequency.entries.sumOf { it.key.length * it.value }
        val numericPart = substring(0, 3).toLong()
        return minLength * numericPart
    }
}

fun main() {

    fun partOne(strings: List<String>): Long = strings.map(::CodeD21).sumOf { it.complexity(robots = 2) }
    fun partTwo(strings: List<String>): Long = strings.map(::CodeD21).sumOf { it.complexity(robots = 25) }
    val input = readInput("21")
    partOne(input).println()
    partTwo(input).println()
}


