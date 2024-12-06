import kotlin.io.path.Path
import kotlin.io.path.readText


fun main(args: Array<String>) {
    fun Grid<Char>.next(point: Point2D, direction: Point2D): Pair<Point2D, Point2D>? {
        fun turn(direction: Point2D) = when (direction) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            else -> error("Unsupported direction: $direction")
        }

        var nextDirection = direction
        if (this[point + direction] == '#') {
            nextDirection = turn(direction)
            // turn two times!!
            if (this[point + nextDirection] == '#') nextDirection = turn(nextDirection)
        }
        val nextPoint = point + nextDirection
        return if (nextPoint in this) return (nextPoint to nextDirection) else null
    }

    fun Grid<Char>.walk(startingPoint: Point2D): HashSet<Point2D> {
        val visited = hashSetOf(startingPoint)
        var nextPoint: Point2D = startingPoint
        var direction = UP
        while (true) {
            val (first, second) = next(nextPoint, direction) ?: break
            nextPoint = first
            direction = second
            visited.add(nextPoint)
        }
        return visited
    }

    fun part1(input: List<String>): Int {
        val grid = input.toGrid()
        val startingPoint = grid.data.entries.first { it.value == '^' }.key
        return grid.walk(startingPoint).size
    }

    fun part2(input: List<String>): Int {
        val grid = input.toGrid()
        val startingPoint = grid.data.entries.first { it.value == '^' }.key
        val initialWalk = grid.walk(startingPoint).toSet()
        return initialWalk
            .filter { toReplace ->
                val grid2 = grid.copy(data = grid.data.toMutableMap().also { it[toReplace] = '#' })
                var curPoint = startingPoint
                var curDirection = UP
                val visited = hashSetOf(curPoint to curDirection)
                while (true) {
                    val tmp1 = grid2.next(curPoint, curDirection) ?: return@filter false
                    if (!visited.add(tmp1)) break
                    curPoint = tmp1.first
                    curDirection = tmp1.second
                }
                true
            }
            .count()
    }


    // Or read a large test input from the `src/Day01_test.txt` file:
    if (args.isEmpty()) {
        val testInput = readInput("06t")
        check(part1(testInput) == 41) { "Expected 41 but got ${part1(testInput)}" }

        // Read the input from the `src/Day01.txt` file.
        val input = readInput("06")
        part1(input).println()
        check(part2(testInput) == 6) { "Expected 6 but got ${part2(testInput)}" }
        part2(input).println()
    } else {
        val input = Path(args[0]).readText().trim().lines()
        part1(input).println()
        part2(input).println()
    }
}


