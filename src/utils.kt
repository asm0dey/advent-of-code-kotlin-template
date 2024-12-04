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


data class Grid<T>(val data: Array<Array<T>>) : Iterable<Pair<Point2D, T>> {
    constructor(lst: List<List<T>>) : this(kotlin.run {
        @Suppress("UNCHECKED_CAST")
        lst
            .map { it.map { it as Any }.toTypedArray() }
            .toTypedArray() as Array<Array<T>>
    })

    operator fun get(x: Int, y: Int) = data[y][x]
    operator fun get(point: Point2D) = data[point.y][point.x]
    operator fun contains(point: Point2D) = point.x in data[0].indices && point.y in data.indices
    fun beam(source: Point2D, direction: Point2D, amount: Int = Int.MAX_VALUE) =
        (1..amount).asSequence().map { source + direction * it }.filter { contains(it) }.map { get(it) }

    override fun iterator(): Iterator<Pair<Point2D, T>> = GridIterator(data)

    private class GridIterator<T>(val data: Array<Array<T>>) : Iterator<Pair<Point2D, T>> {
        private var rowIndex = 0
        private var colIndex = 0

        override fun hasNext() = rowIndex < data.size && colIndex < data[rowIndex].size

        override fun next(): Pair<Point2D, T> {
            if (!hasNext()) throw NoSuchElementException()
            val point = p(colIndex, rowIndex)
            val value = data[rowIndex][colIndex]
            colIndex++
            if (colIndex >= data[rowIndex].size) {
                colIndex = 0
                rowIndex++
            }
            return point to value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid<*>

        return data.contentDeepEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentDeepHashCode()
    }
}

@Suppress("unused")
fun <T> Grid<T>.rotateRight(): Grid<T> {
    val height = data.size
    val width = data[0].size
    val rotatedData = Array(width) { Array<Any?>(height) { null } }

    for (i in data.indices) {
        for (j in data[i].indices) {
            rotatedData[j][height - i - 1] = data[i][j]
        }
    }

    @Suppress("UNCHECKED_CAST")
    return Grid(rotatedData as Array<Array<T>>)
}

fun List<String>.toGrid() = Grid(map { it.toCharArray().toList() })