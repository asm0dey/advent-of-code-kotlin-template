import org.jgrapht.alg.shortestpath.BFSShortestPath
import org.jgrapht.generate.GridGraphGenerator
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.builder.GraphTypeBuilder
import java.util.function.Supplier

fun main() {
    fun solve(input: List<String>, minSave: Int, maxGlitchSize: Int): Int {
        val grid = input.toGrid()
        val value = object : Supplier<P2> {
            val sequence =
                (0..grid.data.maxOf { it.key.y })
                    .asSequence()
                    .flatMap { y ->
                        (0..grid.data.maxOf { it.key.x }).asSequence().map { x -> P2(x, y) }
                    }
                    .asIterable()
                    .iterator()

            override fun get(): P2 {
                return sequence.next()
            }
        }
        val graph =
            GraphTypeBuilder
                .undirected<P2, DefaultWeightedEdge>()
                .weighted(true)
                .vertexSupplier(value)
                .edgeSupplier { DefaultWeightedEdge() }
                .buildGraph()
        GridGraphGenerator<P2, DefaultWeightedEdge>(input.size, input.first().length)
            .generateGraph(graph)
        graph.removeAllVertices(grid.data.filter { it.value == '#' }.keys)
        val start = grid.data.asSequence().first { it.value == 'S' }.key
        val end = grid.data.asSequence().first { it.value == 'E' }.key
        val allPaths = buildMap {
            for (tStart in listOf(start, end)) {
                val paths = BFSShortestPath(graph).getPaths(tStart)
                for (tEnd in graph.vertexSet()) {
                    paths.getPath(tEnd)?.let { put(tStart to tEnd, it.weight.toInt()) }
                }
            }
        }

        val initLength = allPaths[start to end]!!.toInt()
        val results = hashMapOf(initLength to 1)
        val vertices = graph.vertexSet().toList().sortedBy { allPaths[start to it] }
        for (i in vertices.indices) {
            for (j in i + 1 until vertices.size) {
                val glitchStart = vertices[i]
                val glitchEnd = vertices[j]
                val distance = glitchStart.manhattanDistance(glitchEnd)
                if (distance in 2..maxGlitchSize) {
                    val sz = allPaths[start to glitchStart]!! + distance + allPaths[end to glitchEnd]!!
                    results[sz] = (results[sz] ?: 0) + 1
                }
            }
        }
        return results.filter { it.key <= initLength - minSave }.values.sum()
    }

    val testInput = readInput("20t")
    check(solve(testInput, 12, 2) == 8) { "Wrong answer ${solve(testInput, 12, 2)}, expected 8" }
    check(solve(testInput, 50, 20) == 285) { "Wrong answer ${solve(testInput, 50, 20)}, expected 285" }
    val input = readInput("20")
    solve(input, 100, 2).println()
    solve(input, 100, 20).println()
}