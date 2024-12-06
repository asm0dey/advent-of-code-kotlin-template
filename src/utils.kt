@file:Suppress("unused")

data class Point2D(val x: Int, val y: Int) {
    operator fun plus(other: Point2D) = Point2D(x + other.x, y + other.y)
    operator fun times(other: Int) = Point2D(x * other, y * other)
}

fun p(x: Int, y: Int) = Point2D(x, y)
val UP = p(0, -1)
val DOWN = p(0, 1)
val LEFT = p(-1, 0)
val RIGHT = p(1, 0)
val UP_RIGHT = p(1, -1)
val UP_LEFT = p(-1, -1)
val DOWN_RIGHT = p(1, 1)
val DOWN_LEFT = p(-1, 1)
val directions = listOf(UP, DOWN, LEFT, RIGHT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT)


data class Grid<T>(val data: Map<Point2D, T>, val generator: (Point2D) -> T? = { null }) : Iterable<Pair<Point2D, T>> {
    constructor(lst: List<List<T>>) : this(
        lst
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, value ->
                    Point2D(x, y) to value
                }
            }
            .toMap()
    )

    operator fun get(x: Int, y: Int) = data[Point2D(x, y)]
    operator fun get(point: Point2D) = data[point]
    operator fun contains(point: Point2D) = point in data
    fun beam(source: Point2D, direction: Point2D, amount: Int = Int.MAX_VALUE) =
        (1..amount).asSequence().map { source + direction * it }.filter(::contains).map { get(it)!! }

    override fun iterator(): Iterator<Pair<Point2D, T>> = data.entries.map { (a, b) -> a to b }.iterator()
}

fun <T> Grid<T>.rotateCW(): Grid<T> {
    val maxY = data.keys.maxOf { it.y }
    val rotatedData = data.mapKeys { (point) ->
        Point2D(maxY - point.y, point.x)
    }
    return Grid(rotatedData)
}

fun <T> Grid<T>.rotateCCW(): Grid<T> {
    val maxX = data.keys.maxOf { it.x }
    val rotatedData = data.mapKeys { (point) ->
        Point2D(point.y, maxX - point.x)
    }
    return Grid(rotatedData)
}

fun <T> Grid<T>.flipX(): Grid<T> {
    val maxX = data.keys.maxOf { it.x }
    val flippedData = data.mapKeys { (point) ->
        Point2D(maxX - point.x, point.y)
    }
    return Grid(flippedData)
}

fun <T> Grid<T>.flipY(): Grid<T> {
    val maxY = data.keys.maxOf { it.y }
    val flippedData = data.mapKeys { (point) ->
        Point2D(point.x, maxY - point.y)
    }
    return Grid(flippedData)
}

fun <T> Grid<T>.print() {
    val maxY = data.keys.maxOfOrNull { it.y } ?: return
    val maxX = data.keys.maxOfOrNull { it.x } ?: return
    val minX = data.keys.minOfOrNull { it.x } ?: return
    val minY = data.keys.minOfOrNull { it.y } ?: return
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            print(data[Point2D(x, y)] ?: " ")
        }
        println()
    }
}

fun List<String>.toGrid() = Grid(map { it.toCharArray().toList() })
