fun main() {

    fun Grid<Char>.fillEmptyFromStartTo(exit: Point2D) {
        for (i in 0..exit.x) {
            for (j in 0..exit.y) {
                if (P2(i, j) !in this) this[P2(i, j)] = '.'
            }
        }
    }

    fun MutableMap<Point2D, Char>.toGrid() = Grid(this)

    fun part1(input: List<String>, byteNumber: Int, exit: Point2D): Int {
        val grid = input
            .asSequence()
            .filter(String::isNotBlank)
            .take(byteNumber)
            .map { it.split(",") }
            .map { it.map(String::toInt) }
            .map { (a, b) -> P2(a, b) }
            .associateWith { '#' }
            .toMutableMap()
            .toGrid()
        grid.fillEmptyFromStartTo(exit)
        return grid.bfs(P2(0, 0), exit) { it != '#' }.size - 1
    }

    fun part2(input: List<String>, byteNumber: Int, exit: Point2D): Point2D {
        val fullList = input
            .asSequence()
            .filter(String::isNotBlank)
            .map { it.split(",") }
            .map { it.map(String::toInt) }
            .map { (a, b) -> P2(a, b) }
        val grid = Grid<Char>()
        for (it in fullList.take(byteNumber)) grid[it] = '#'
        grid.fillEmptyFromStartTo(exit)
        for (byte in fullList) {
            grid[byte] = '#'
            if (grid.bfs(P2(0, 0), exit) { it != '#' }.isEmpty()) return byte
        }
        error("No solution found")
    }

    val testInput = readInput("18t")
    val testExit = P2(6, 6)
    val testBytes = 12
    part1(testInput, testBytes, testExit).println()
    val input = readInput("18")
    part1(input, 1024, P2(70, 70)).println()
    part2(testInput, testBytes, testExit).println()
    part2(input, 1024, P2(70, 70)).println()
}

