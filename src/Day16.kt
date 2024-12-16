import java.util.*


fun main() {
    fun Point2D.rotations() = when (this) {
        UP, DOWN -> listOf(LEFT, RIGHT)
        LEFT, RIGHT -> listOf(UP, DOWN)
        else -> error("Invalid direction $this")
    }


    data class State(val positionAndDirection: Pair<Point2D, Point2D>, val distance: Int, val previous: State?)

    fun neighbours(state: State, walls: Set<Point2D>): Set<State> {
        val (position, currentHeading) = state.positionAndDirection

        val rotatedNeighbours = currentHeading.rotations()
            .map { heading -> State(position to heading, state.distance + 1000, state) }

        val nextPosition = position + currentHeading
        val positionNeighbour = listOfNotNull(
            if (!walls.contains(nextPosition))
                State(nextPosition to currentHeading, state.distance + 1, state)
            else null
        )

        return (rotatedNeighbours + positionNeighbour).toSet()
    }

    fun shortestPath(start: Point2D, end: Point2D, walls: Set<Point2D>): Pair<Int, Int> {
        var shortest = -1
        val queue = PriorityQueue(
            compareBy<State> { it.distance }
                .thenComparing { it -> it.positionAndDirection.first }
                .thenComparing { it -> it.positionAndDirection.second })
        val viewingSpots = hashSetOf<Point2D>()
        val visited = hashSetOf<Pair<Point2D, Point2D>>()

        queue.add(State(start to RIGHT, 0, null))

        while (queue.isNotEmpty()) {
            val current = queue.poll()!!
            val (positionAndDirection, distance, _) = current
            if (positionAndDirection.first == end) {
                shortest = distance
                val path = hashSetOf<Point2D>()
                var tmp: State? = current
                while (tmp != null) {
                    path.add(tmp.positionAndDirection.first)
                    tmp = tmp.previous
                }
                viewingSpots.addAll(path)
            }

            visited.add(current.positionAndDirection)

            for (neighbor in neighbours(current, walls)) {
                if (!visited.contains(neighbor.positionAndDirection)) {
                    queue.add(neighbor)
                }
            }
        }

        return shortest to viewingSpots.size
    }

    fun solve(input: List<String>): Pair<Int, Int> {
        val grid = input.toGrid()
        val walls = grid.filter { it.second == '#' }.map { it.first }.toSet()
        val start = grid.filter { it.second == 'S' }.map { it.first }.firstOrNull()
            ?: throw IllegalStateException("Start point not found")
        val end = grid.filter { it.second == 'E' }.map { it.first }.firstOrNull()
            ?: throw IllegalStateException("End point not found")

        // Solve the pathfinding puzzle
        val (shortestDistance, viewingSpotsCount) = shortestPath(start, end, walls)

        // Print results
        return shortestDistance to viewingSpotsCount
    }
    solve(readInput("16t")).println()
    solve(readInput("16")).println()

}