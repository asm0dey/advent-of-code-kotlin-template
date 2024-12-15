import java.io.File

enum class ObjectType {
    ROBOT, BOX
}

fun main() {
    data class GameObject(
        var pos: Point2D,
        val type: ObjectType,
        val size: Point2D,
    )

    fun init(input: String): Pair<Grid<Char>, List<Char>> {
        val (gridLit, movesLit) = input.split("\n\n")
        val grid = gridLit.lines().toGrid()
        val moves = movesLit.lines().joinToString("").toList()
        return grid to moves
    }

    val directionMap = mapOf(
        '>' to RIGHT,
        '<' to LEFT,
        '^' to UP,
        'v' to DOWN
    )

    fun score(objects: List<GameObject>): Int {
        return objects
            .filter { it.type == ObjectType.BOX }
            .sumOf { it.pos.y * 100 + it.pos.x }
    }

    fun List<GameObject>.findRobot(): GameObject = first { it.type == ObjectType.ROBOT }
    fun Grid<Char>.collides(newPos: Point2D, size: Point2D): Boolean {
        for (x in newPos.x until newPos.x + size.x) {
            for (y in newPos.y until newPos.y + size.y) {
                if (get(Point2D(x, y)) == '#') return true
            }
        }
        return false
    }

    fun Grid<Char>.move(
        gameObject: GameObject,
        gameObjects: List<GameObject>,
        direction: Point2D?,
    ): Boolean {
        val dirVector = direction ?: return false
        val newPos = gameObject.pos + dirVector

        if (collides(newPos, gameObject.size)) return false

        val obstacles = gameObjects.filter { other ->
            other != gameObject && (0 until gameObject.size.x).any { dx ->
                (0 until gameObject.size.y).any { dy ->
                    val checkPos = newPos + Point2D(dx, dy)
                    val otherPos = other.pos
                    checkPos.x in otherPos.x until otherPos.x + other.size.x && checkPos.y in otherPos.y until otherPos.y + other.size.y
                }
            }
        }

        val isMovementBlocked = obstacles.any { !move(it, gameObjects, direction) }
        if (obstacles.isNotEmpty() && isMovementBlocked) return false
        gameObject.pos = newPos // Update position
        return true
    }

    fun fillWalls(
        map: Map<Point2D, Char>,
        width: Int,
    ): Map<Point2D, Char> = map
        .flatMap { (point, value) ->
            (0 until width)
                .map { i ->
                    Point2D(point.x * width + i, point.y) to if (value == '#') '#' else '.'
                }
        }
        .toMap()

    fun Map<Point2D, Char>.createObjectsAndAddToMap(
        width: Int,
        expandedMap: MutableMap<Point2D, Char>,
    ): List<GameObject> {
        val resultingObjects =
            filter { (_, value) -> value != '#' && value != '.' }
                .map { (point, value) ->
                    GameObject(
                        pos = Point2D(point.x * width, point.y),
                        type = if (value == '@') ObjectType.ROBOT else ObjectType.BOX,
                        size = if (value == '@') Point2D(1, 1) else Point2D(width, 1)
                    ).also {
                        expandedMap[Point2D(point.x * width, point.y)] = '.'
                    }
                }
                .toList()

        return resultingObjects
    }

    fun runSimulation(
        origGrid: Grid<Char>,
        moves: List<Char>,
        width: Int = 1,
    ): Int {
        val gridData = origGrid.data.toMutableMap()

        val expandedMap = HashMap(fillWalls(gridData, width))

        val objects = gridData.createObjectsAndAddToMap(width, expandedMap).toMutableList()
        val grid = Grid(expandedMap.toMap())

        for (direction in moves.map { directionMap[it] }) {
            val snapshot = objects.map { it.copy() }
            if (!grid.move(objects.findRobot(), objects, direction)) {
                objects.clear()
                objects.addAll(snapshot)
            }
        }

        return score(objects)
    }

    val input = File("/home/finkel/work_self/aoc-2025/src/15.txt").readText()
    val (grid, moves) = init(input)

    println("p1: ${runSimulation(grid, moves)}")
    println("p2: ${runSimulation(grid, moves, width = 2)}")
}