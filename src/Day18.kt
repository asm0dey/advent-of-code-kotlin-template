fun main() {

    fun Grid<Char>.fillEmptyFromStartTo(exit: Point2D) {
        for (i in 0..exit.x) {
            for (j in 0..exit.y) {
                if (Point2D(i, j) !in this) this[Point2D(i, j)] = '.'
            }
        }
    }

    fun part1(input: List<String>, byteNumber: Int, exit: Point2D): Int {
        val grid = Grid(
            input
                .asSequence()
                .filter(String::isNotBlank)
                .take(byteNumber)
                .map { it.split(",") }
                .map { it.map(String::toInt) }
                .map { (a, b) -> Point2D(a, b) }
                .associateWith { '#' }
                .toMutableMap())
        grid.fillEmptyFromStartTo(exit)
        return grid.bfs(Point2D(0, 0), exit) { it != '#' }.size - 1
    }

    fun part2(input: List<String>, byteNumber: Int, exit: Point2D): Point2D {
        val fullList = input
            .asSequence()
            .filter(String::isNotBlank)
            .map { it.split(",") }
            .map { it.map(String::toInt) }
            .map { (a, b) -> Point2D(a, b) }
        val grid = Grid<Char>()
        grid.fillEmptyFromStartTo(exit)
        for (it in fullList.take(byteNumber)) grid[it] = '#'
        for (byte in fullList) {
            grid[byte] = '#'
            if (grid.bfs(Point2D(0, 0), exit) { it != '#' }.isEmpty()) return byte
        }
        error("No solution found")
    }

    val testInput = readInput("18t")
    part1(testInput, 12, Point2D(6, 6)).println()
    val input = readInput("18")
    part1(input, 1024, Point2D(70, 70)).println()
    part2(testInput, 12, Point2D(6, 6)).println()
    part2(input, 1024, Point2D(70, 70)).println()
}

