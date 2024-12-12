import java.io.File

fun main(args: Array<String>) {
    fun <T> Grid<T>.regions(): Sequence<Set<Point2D>> {
        val visited = hashSetOf<Point2D>()
        return asSequence()
            .filter { it.first !in visited }
            .map { (start, value) ->
                val toVisit = ArrayDeque<Point2D>()
                val region = hashSetOf<Point2D>()
                toVisit.add(start)
                while (toVisit.isNotEmpty()) {
                    val cur = toVisit.removeFirst()
                    if (cur !in region && this[cur] == value) {
                        region.add(cur)
                        visited.add(cur)
                        for (direction in mainDirections) {
                            val next = cur + direction
                            if (next !in visited && next in this) {
                                toVisit.addLast(next)
                            }
                        }
                    }
                }
                region
            }
    }

    fun Set<Point2D>.perimeter() = sumOf { point -> mainDirections.count { point + it !in this } }

    fun Set<Point2D>.countAngles(): Int {
        val directions = listOf(
            listOf(UP, RIGHT, UP_RIGHT),
            listOf(UP, LEFT, UP_LEFT),
            listOf(DOWN, RIGHT, DOWN_RIGHT),
            listOf(DOWN, LEFT, DOWN_LEFT),
        )
        return sumOf { point ->
                directions
                    .map { it.map { direction -> point + direction } }
                    .count { (a, b, c) ->
                        a !in this && b !in this || a in this && b in this && c !in this
                    }
            }
    }

    fun part1(input: List<String>) = input
        .toGrid()
        .regions()
        .sumOf { region ->
            val size = region.size
            val perimeter = region.perimeter()
            size.toLong() * perimeter.toLong()
        }

    fun part2(input: List<String>): Long {
        return input
            .toGrid()
            .regions()
            .sumOf { region ->
                val size = region.size
                val angles = region.countAngles()
                size.toLong() * angles.toLong()
            }
    }

    if (args.isEmpty()) {
        val testInput0 = readInput("12t0")
        check(part1(testInput0) == 140L) { "Expected 140 but got ${part1(testInput0)}" }
        val testInput = readInput("12t")
        check(part1(testInput) == 1930L) { "Expected 1930 but got ${part1(testInput)}" }

        val input = readInput("12")
        part1(input).println()
        check(part2(testInput0) == 80L) { "Expected 80 but got ${part2(testInput0)}" }
        part2(input).println()
    } else {
        val input = File(args[0]).readLines()
        part1(input).println()
        part2(input).println()
    }
}





