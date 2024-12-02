import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

fun main(args:Array<String>) {
    fun parseDay1(input: List<String>): List<Pair<Int, Int>> {
        val pairs = input
            .map { it.split(" ").filter { it.isNotBlank() } }
            .map { (it[0].toInt()) to (it[1].toInt()) }
        return pairs
    }

    fun part1(input: List<String>): Int {
        val pairs = parseDay1(input)
        val sortedLeft = pairs.map { it.first }.sorted()
        val sortedRight = pairs.map { it.second }.sorted()
        return sortedLeft.zip(sortedRight).sumOf { (l, r) ->
            abs(l - r)
        }
    }

    fun part2(input: List<String>): Int {
        val pairs = parseDay1(input)
        val lefts = pairs.map { it.first }.sorted()
        val counts = pairs.map { it.second }.groupingBy { it }.eachCount()
//        val leftCounts = sortedRight.groupingBy { it }.eachCount()
        return lefts.sumOf { it * (counts[it] ?: 0) }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    if(args.isEmpty()){
        val testInput = readInput("Day01_test")
        check(part1(testInput) == 11)

        // Read the input from the `src/Day01.txt` file.
        val input = readInput("Day01")
        part1(input).println()
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
