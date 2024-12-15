import java.io.File

fun main(args: Array<String>) {

    fun part1(input: List<String>, maxX: Int, maxY: Int): Int {
        val robots = StringTemplate("p={px|int},{py|int} v={vx|int},{vy|int}").parse(input.joinToString("\n"))
            .map { Point2D(it["px"] as Int, it["py"] as Int) to Point2D(it["vx"] as Int, it["vy"] as Int) }
        val newLocations = robots
            .map { (pos, vel) -> pos + vel * 100 }
            .map {
                Point2D(it.x % maxX, it.y % maxY)
            }
            .map { (x, y) ->
                Point2D(if (x < 0) maxX + x else x, if (y < 0) maxY + y else y)
            }
        val quadrants = hashMapOf(
            listOf(0..maxX / 2 - 1, 0..maxY / 2 - 1) to 0,
            listOf(0..maxX / 2 - 1, maxY / 2 + 1..maxY) to 0,
            listOf(maxX / 2 + 1..maxX, 0..maxY / 2 - 1) to 0,
            listOf(maxX / 2 + 1..maxX, maxY / 2 + 1..maxY) to 0,
        )
        newLocations.forEach { (x, y) ->
            quadrants.entries.firstOrNull { x in it.key[0] && y in it.key[1] }?.let { it.setValue(it.value + 1) }
        }
        return quadrants.values.reduce(Int::times)
    }

    fun part2(input: List<String>, maxX: Int, maxY: Int): Long {
        var robots = StringTemplate("p={px|int},{py|int} v={vx|int},{vy|int}").parse(input.joinToString("\n"))
            .map { Point2D(it["px"] as Int, it["py"] as Int) to Point2D(it["vx"] as Int, it["vy"] as Int) }
        var counter = 1L
        while (true) {
            robots = robots.map { (pos, vel) -> pos + vel to vel }
                .map { (it, vel) -> Point2D(it.x % maxX, it.y % maxY) to vel }
                .map { (it, vel) ->
                    Point2D(
                        if (it.x < 0) maxX + it.x else it.x,
                        if (it.y < 0) maxY + it.y else it.y
                    ) to vel
                }
            var grid = Grid(robots.associate { it.first to '*' })
            val grid2 =
                Grid(grid
                    .filter { (it.first + LEFT in grid || it.first + RIGHT in grid) && (it.first + UP in grid || it.first + DOWN in grid) }
                    .associate { it.first to '*' })
            val minY = grid2.data.keys.minOfOrNull { it.y }
            val minX = grid2.data.keys.minOfOrNull { it.x }
            val maxX = grid2.data.keys.maxOfOrNull { it.x }
            if (minY == null || minX == null || maxX == null) {
                counter++
                continue
            }
            val containsVertical = (minX..maxX).any { x ->
                grid2.beam(Point2D(x, minY), DOWN, maxY).toList().size > 10
            }
            if (containsVertical) {
                println("Second $counter:")
                grid.print(" ")
                return counter
            }
            counter++
        }
    }


    if (args.isEmpty()) {
        val testInput = readInput("14t")
        check(part1(testInput, 11, 7) == 12) { "Expected 12 but got ${part1(testInput, 11, 7)}" }

        val input = readInput("14")
        part1(input, 101, 103).println()
        part2(input, 101, 103).println()
    } else {
        val input = File(args[0]).readLines()
        part1(input, 101, 103).println()
        part2(input, 101, 103).println()
    }
}





