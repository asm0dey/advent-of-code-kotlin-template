import org.jgrapht.Graph
import org.jgrapht.alg.clique.BronKerboschCliqueFinder
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.builder.GraphTypeBuilder
import org.jgrapht.nio.graph6.Graph6Sparse6Exporter
import org.jgrapht.nio.graphml.GraphMLExporter
import org.jgrapht.nio.json.JSONExporter
import java.io.File

fun main() {
    fun buildGraph(input: List<String>): Graph<String, DefaultEdge> {
        val graph = GraphTypeBuilder.undirected<String, DefaultEdge>().weighted(false).allowingMultipleEdges(false)
            .allowingSelfLoops(false).buildGraph()
        for (s in input) {
            val (l, r) = s.split("-")
            graph.addVertex(l)
            graph.addVertex(r)
            graph.addEdge(l, r, DefaultEdge())
        }
        return graph
    }

    fun part1(input: List<String>): Int {
        val graph = buildGraph(input)
        val tVertices = graph.vertexSet().filter { it.startsWith("t") }
        val sets = hashSetOf<Set<String>>()
        for (tVertex in tVertices) {
            val tEdges = graph.edgesOf(tVertex).toList()
            for (i in tEdges.indices) {
                for (j in i + 1 until tEdges.size) {
                    val iT = graph.getEdgeTarget(tEdges[i])
                    val iS = graph.getEdgeSource(tEdges[i])
                    val jT = graph.getEdgeTarget(tEdges[j])
                    val jS = graph.getEdgeSource(tEdges[j])
                    val x1 = if (iT == tVertex) iS else iT
                    val x2 = if (jT == tVertex) jS else jT
                    if (graph.containsEdge(x1, x2)) {
                        sets.add(setOf(tVertex, x1, x2))
                    }
                }
            }
        }
        return sets.size
    }

    fun part2(input: List<String>) =
        BronKerboschCliqueFinder(buildGraph(input)).maximumIterator().next().sorted().joinToString(",")

    val testInput = readInput("23t")
    part1(testInput).println()
    val input = readInput("23")
    part1(input).println()
    part2(testInput).println()
    part2(input).println()
    JSONExporter<String,DefaultEdge>({it}).exportGraph(buildGraph(input), File("/tmp/graph.graphml"))
}