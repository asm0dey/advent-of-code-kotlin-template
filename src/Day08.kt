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


    fun Grid<Char>.findAntiNodes(first: Point2D, second: Point2D): Sequence<Point2D> {
        val diff = second - first
        val sequence1 = generateSequence(second + diff) { it + diff }.takeWhile { it in this }
        val sequence2 = generateSequence(first - diff) { it - diff }.takeWhile { it in this }
        return sequence1 + sequence2 + first + second
    }

    fun part1Komprehensions(input: List<String>): Int {
        return doFlatMapIterable(
            { listOf(input.toGrid()) },
            { grid -> antennas(grid).values },
            { _, antennas -> antennas.withIndex() },
            { _, antennas, (i, _) -> (i + 1 until antennas.size).asIterable() },
            { grid, antennas, (_, antenna1), j ->
                val antenna2 = antennas[j]
                sequenceOf(antenna2 * 2 - antenna1, antenna1 * 2 - antenna2).filter { it in grid }.asIterable()
            })
            .distinct()
            .count()
    }

    fun part2Komprehensions(input: List<String>) = doFlatMapIterable(
        { listOf(input.toGrid()) },
        { grid -> antennas(grid).values },
        { _, antennas -> antennas.withIndex() },
        { _, antennas, (i, _) -> (i + 1 until antennas.size).asIterable() },
        { grid, antennas, (_, antenna), j -> grid.findAntiNodes(antenna, antennas[j]).asIterable() })
        .distinct()
        .count()

    fun part1(input: List<String>): Int {
        val grid = input.toGrid()
        return antennas(grid).values.flatMap {
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
        return antennas(grid).values.flatMap {
            it.flatMapIndexed { i, curPoint ->
                (i + 1 until it.size).flatMap { j -> grid.findAntiNodes(curPoint, it[j]) }
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
        part1Komprehensions(input).println()
        check(part2(testInput) == 34) { "Expected 34 but got ${part2(testInput)}" }
        part2(input).println()
        part2Komprehensions(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
        part1Komprehensions(input).println()
        part2(input).println()
    }
}


