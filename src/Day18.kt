fun main() {
    fun Grid<Char>.path(point2D: Point2D, exit: Point2D): List<Point2D> {
        val queue = ArrayDeque<Pair<Point2D, List<Point2D>>>()
        val visited = hashSetOf<Point2D>()

        queue.add(point2D to listOf(point2D))
        visited.add(point2D)

        while (queue.isNotEmpty()) {
            val (current, path) = queue.removeFirst()

            if (current == exit) return path

            for (dir in mainDirections) {
                val next = Point2D(current.x + dir.x, current.y + dir.y)
                if (next in this && this[next] == '.' && next !in visited) {
                    visited.add(next)
                    queue.add(next to path + next)
                }
            }
        }

        return emptyList()
    }

    fun fillGrid(exit: Point2D, grid: Grid<Char>) {
        for (i in 0..exit.x) {
            for (j in 0..exit.y) {
                if (Point2D(i, j) !in grid) grid[Point2D(i, j)] = '.'
            }
        }
    }

    fun part1(input: List<String>, byteNumber: Int, exit: Point2D): Int {
        val grid = Grid(
            input
                .asSequence()
                .filter { it.isNotBlank() }
                .take(byteNumber)
                .map { it.split(",") }
                .map { it.map { it.toInt() } }
                .map { (a, b) -> Point2D(a, b) }
                .toList()
                .associateWith { '#' }
                .toMutableMap())
        fillGrid(exit, grid)
        return grid.path(Point2D(0, 0), exit).size - 1
    }

    fun part2(input: List<String>, exit: Point2D): Point2D {
        val fullList = input
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.split(",") }
            .map { it.map { it.toInt() } }
            .map { (a, b) -> Point2D(a, b) }
        val grid = Grid(listOf(listOf<Char>()))
        fillGrid(exit, grid)
        for (byte in fullList) {
            grid[byte] = '#'
            if (grid.path(Point2D(0, 0), exit).isEmpty()) return byte
        }
        error("No solution found")
    }

    val testInput = readInput("18t")
    part1(testInput, 12, Point2D(6, 6)).println()
    val input = readInput("18")
    part1(input, 1024, Point2D(70, 70)).println()
    part2(testInput, Point2D(6, 6)).println()
    part2(input, Point2D(70, 70)).println()

}

