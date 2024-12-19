@file:Suppress("unused")

import java.util.*
import kotlin.collections.ArrayDeque


data class Point2D(val x: Int, val y: Int) : Comparable<Point2D> {
    operator fun plus(other: Point2D) = Point2D(x + other.x, y + other.y)
    operator fun times(other: Int) = Point2D(x * other, y * other)
    operator fun minus(point2D: Point2D): Point2D = Point2D(x - point2D.x, y - point2D.y)
    override fun compareTo(other: Point2D): Int = if (x == other.x) y.compareTo(other.y) else x.compareTo(other.x)
    override fun toString(): String = "($x,$y)"
}
typealias P2 = Point2D

fun p(x: Int, y: Int) = Point2D(x, y)
val UP = P2(0, -1)
val DOWN = P2(0, 1)
val LEFT = P2(-1, 0)
val RIGHT = P2(1, 0)
val UP_RIGHT = P2(1, -1)
val UP_LEFT = P2(-1, -1)
val DOWN_RIGHT = P2(1, 1)
val DOWN_LEFT = P2(-1, 1)
val mainDirections = listOf(UP, DOWN, LEFT, RIGHT)
val diagonalsDirections = listOf(UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT)
val directions = listOf(UP, DOWN, LEFT, RIGHT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT)


data class Grid<T>(val data: MutableMap<Point2D, T>, val generator: (Point2D) -> T? = { null }) :
    Iterable<Pair<Point2D, T>> {
    constructor(lst: List<List<T>>) : this(
        lst
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, value ->
                    Point2D(x, y) to value
                }
            }
            .toMap()
            .toMutableMap()
    )

    constructor() : this(hashMapOf<Point2D, T>())

    operator fun get(x: Int, y: Int) = data[Point2D(x, y)]
    operator fun get(point: Point2D) = data[point]
    operator fun contains(point: Point2D) = point in data
    fun beam(source: Point2D, direction: Point2D, amount: Int = Int.MAX_VALUE) =
        (1..amount).asSequence().map { source + direction * it }.filter(::contains).map { get(it)!! }

    override fun iterator(): Iterator<Pair<Point2D, T>> = data.entries.map { (a, b) -> a to b }.iterator()
    operator fun set(first: Point2D, value: T) {
        data[first] = value
    }
}

fun <T> Grid<T>.bfs(
    start: Point2D,
    target: Point2D,
    allowedDirections: List<Point2D> = mainDirections,
    isAllowed: (T) -> Boolean = { true },
): List<Pair<Point2D, T>> {
    val visited = hashSetOf<Point2D>()
    val queue: ArrayDeque<Point2D> = ArrayDeque()
    val result = mutableListOf<Pair<Point2D, T>>()

    queue.add(start)
    while (queue.isNotEmpty()) {
        val point = queue.removeFirst()
        if (point !in visited && point in this) {
            visited.add(point)
            val value = this[point] ?: continue
            if (isAllowed(value)) {
                result.add(point to value)
                if (point == target) return result
                allowedDirections
                    .map(point::plus)
                    .filterNot(visited::contains)
                    .forEach(queue::add)
            }
        }
    }

    return emptyList()
}

fun <T> Grid<T>.rotateCW(): Grid<T> {
    val maxY = data.keys.maxOf { it.y }
    val rotatedData = data.mapKeys { (point) ->
        Point2D(maxY - point.y, point.x)
    }
    return Grid(rotatedData.toMutableMap())
}

fun <T> Grid<T>.rotateCCW(): Grid<T> {
    val maxX = data.keys.maxOf { it.x }
    val rotatedData = data.mapKeys { (point) ->
        Point2D(point.y, maxX - point.x)
    }
    return Grid(rotatedData.toMutableMap())
}

fun <T> Grid<T>.flipX(): Grid<T> {
    val maxX = data.keys.maxOf { it.x }
    val flippedData = data.mapKeys { (point) ->
        Point2D(maxX - point.x, point.y)
    }
    return Grid(flippedData.toMutableMap())
}

fun <T> Grid<T>.flipY(): Grid<T> {
    val maxY = data.keys.maxOf { it.y }
    val flippedData = data.mapKeys { (point) ->
        Point2D(point.x, maxY - point.y)
    }
    return Grid(flippedData.toMutableMap())
}

fun <T> Grid<T>.print(emptyPlaceholder: String = " ") {
    val maxY = data.keys.maxOfOrNull { it.y } ?: return
    val maxX = data.keys.maxOfOrNull { it.x } ?: return
    val minX = data.keys.minOfOrNull { it.x } ?: return
    val minY = data.keys.minOfOrNull { it.y } ?: return
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            print(data[Point2D(x, y)] ?: emptyPlaceholder)
        }
        println("")
    }
}


fun List<String>.toGrid() = Grid(map { it.toCharArray().toList() })

@JvmInline
value class StringTemplate(private val template: String) {
    fun parse(input: String): List<Map<String, Any?>> {

        val map = template.split("\\{\\w+\\|\\w+\\??}".toRegex()).map { Regex.escape(it) }
        val findAll = Regex("\\{\\w+\\|\\w+\\??}").findAll(template).toList()
        val lines = buildString {
            map.mapIndexed { index, s ->
                append(s)
                if (index < findAll.size) {
                    append(findAll[index].value)
                }
            }
        }.lines()
        val templatePattern = lines
            .joinToString("\n") { line ->
                line.replace("\\{([^|}]+)\\|([^}]+)(\\??)}".toRegex()) { match ->
                    val key = match.groupValues[1] // key inside the placeholder
                    val isOptional = match.groupValues[3] == "?" // Check if the value is optional
                    if (isOptional) "(?<$key>[^,\\s]*)?" else "(?<$key>[^,\\s]+)" // Optional group or mandatory group
                }
            }
            .toRegex()

        return templatePattern.findAll(input).map { matchResult ->
            val tempResult = mutableMapOf<String, Any?>()

            // Extract all named groups from the match
            "(?<=\\(\\?<)([a-zA-Z][a-zA-Z0-9]*)".toRegex().findAll(templatePattern.pattern).forEach { match ->
                val groupName = match.value
                val rawValue = matchResult.groups[groupName]?.value
                val isOptional =
                    template.contains("{$groupName|") && template.contains("\\{$groupName\\|.*?\\?}".toRegex())
                if (rawValue != null) {
                    // Identify the type from the template and convert the raw value
                    val type = template.substringAfter("{$groupName|")
                        .substringBefore("}")
                        .trim()
                    tempResult[groupName] = rawValue.trim().convertToType(type)
                } else if (!isOptional) {
                    throw IllegalArgumentException("Mandatory value for '$groupName' is missing in the input")
                }
            }

            tempResult.toMap()
        }
            .toList()

    }

}

fun String.convertToType(type: String): Any? {
    val isNullable = type.endsWith("?")
    val actualType = if (isNullable) type.removeSuffix("?") else type

    return try {
        when (actualType.lowercase(Locale.getDefault())) {
            "int" -> toInt()
            "long" -> toLong()
            "double" -> toDouble()
            "string" -> this
            else -> throw IllegalArgumentException("Unsupported type: $actualType")
        }
    } catch (e: Exception) {
        if (isNullable) null else throw e // Return null for nullable types if conversion fails, else propagate the exception
    }
}

private fun String.toTemplate() = StringTemplate(this)