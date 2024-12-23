import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.generate.GridGraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static javautils.JavaUtils.JGrid;
import static javautils.JavaUtils.JPoint2D;

public class Day20 {
  public static void main(String[] args) throws IOException {
    List<String> testInput = readInput("20t");
    assert solve(testInput, 12, 2) == 8 : "Wrong answer for test case 1";
    assert solve(testInput, 50, 20) == 285 : "Wrong answer for test case 2";

    List<String> input = readInput("20");
    System.out.println(solve(input, 100, 2));
    System.out.println(solve(input, 100, 20));
  }

  public static int solve(List<String> input, int minSave, int maxGlitchSize) {
    JGrid<Character> grid = JGrid.asCharGrid(input);

    // Supplier for generating P2 vertices
    Supplier<JPoint2D> vertexSupplier = new Supplier<>() {
      private final Iterator<JPoint2D> sequence = createPointIterator(grid);

      @Override
      public JPoint2D get() {
        return sequence.next();
      }
    };

    // Create an undirected weighted graph
    Graph<JPoint2D, DefaultWeightedEdge> graph = GraphTypeBuilder
        .undirected()
        .weighted(true)
        .vertexSupplier(vertexSupplier)
        .edgeSupplier(DefaultWeightedEdge::new)
        .buildGraph();

    // Generate grid graph
    new GridGraphGenerator<JPoint2D, DefaultWeightedEdge>(input.size(), input.get(0).length())
        .generateGraph(graph);

    // Remove blocked vertices
    graph.removeAllVertices(grid.data().entrySet().stream()
        .filter(entry -> entry.getValue() == '#')
        .map(Map.Entry::getKey)
        .toList());

    // Find "start" and "end" vertices
    JPoint2D start = grid.data().entrySet().stream()
        .filter(entry -> entry.getValue() == 'S')
        .map(Map.Entry::getKey)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Start point not found"));

    JPoint2D end = grid.data().entrySet().stream()
        .filter(entry -> entry.getValue() == 'E')
        .map(Map.Entry::getKey)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("End point not found"));

    // Compute all shortest paths
    Map<Pair<JPoint2D, JPoint2D>, Integer> allPaths = computeAllPaths(graph, start, end);

    // Calculate initial path length
    int initLength = allPaths.get(new Pair<>(start, end));

    // Evaluate possible glitch paths
    Map<Integer, Integer> results = new HashMap<>();
    results.put(initLength, 1);

    List<JPoint2D> vertices = graph.vertexSet().stream()
        .sorted(Comparator.comparingInt(v -> allPaths.get(new Pair<>(start, v))))
        .toList();

    for (int i = 0; i < vertices.size(); i++) {
      for (int j = i + 1; j < vertices.size(); j++) {
        JPoint2D glitchStart = vertices.get(i);
        JPoint2D glitchEnd = vertices.get(j);

        int distance = glitchStart.manhattanDistance(glitchEnd);
        if (distance >= 2 && distance <= maxGlitchSize) {
          int pathLength = allPaths.get(new Pair<>(start, glitchStart))
              + distance
              + allPaths.get(new Pair<>(end, glitchEnd));

          results.put(pathLength, results.getOrDefault(pathLength, 0) + 1);
        }
      }
    }

    // Filter by saving requirement
    return results.entrySet().stream()
        .filter(entry -> entry.getKey() <= initLength - minSave)
        .mapToInt(Map.Entry::getValue)
        .sum();
  }

  private static Map<Pair<JPoint2D, JPoint2D>, Integer> computeAllPaths(Graph<JPoint2D, DefaultWeightedEdge> graph, JPoint2D start, JPoint2D end) {
    Map<Pair<JPoint2D, JPoint2D>, Integer> paths = new HashMap<>();
    BFSShortestPath<JPoint2D, DefaultWeightedEdge> bfs = new BFSShortestPath<>(graph);

    for (JPoint2D source : List.of(start, end)) {
      ShortestPathAlgorithm.SingleSourcePaths<JPoint2D, DefaultWeightedEdge> bfsPaths = bfs.getPaths(source);
      graph.vertexSet().forEach(sink -> paths.put(new Pair<>(source, sink), (int) bfsPaths.getWeight(sink)));
    }
    return paths;
  }

  private static Iterator<JPoint2D> createPointIterator(JGrid<Character> grid) {
    int maxX = grid.data().keySet().stream().mapToInt(JPoint2D::x).max().orElse(0);
    int maxY = grid.data().keySet().stream().mapToInt(JPoint2D::y).max().orElse(0);

    List<JPoint2D> points = new ArrayList<>();
    for (int y = 0; y <= maxY; y++) {
      for (int x = 0; x <= maxX; x++) {
        points.add(new JPoint2D(x, y));
      }
    }
    return points.iterator();
  }

  private static List<String> readInput(String fileName) throws IOException {
    return Files.readAllLines(Paths.get("src", fileName + ".txt"));
  }

  public record Pair<K, V>(K first, V second) {

  }
}