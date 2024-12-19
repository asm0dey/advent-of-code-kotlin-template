import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"ExplicitToImplicitClassMigration", "RedundantSuppression"})
public class Day19 {

  public static class Trie<T> {

    private final TrieNode<T> root = new TrieNode<>();

    public static class TrieNode<T> {
      Map<T, TrieNode<T>> children = new HashMap<>();
      boolean isEndOfWord = false;

      @Override
      public String toString() {
        return "(children: " + children + ", isEnd: " + isEndOfWord + ")";
      }
    }

    public void insert(Iterable<T> seq) {
      TrieNode<T> current = root;
      for (T token : seq) {
        current = current.children.computeIfAbsent(token, _ -> new TrieNode<>());
      }
      current.isEndOfWord = true;
    }

    public boolean search(Iterable<T> seq) {
      TrieNode<T> current = root;
      for (T token : seq) {
        current = current.children.getOrDefault(token, null);
        if (current == null) {
          return false;
        }
      }
      return current.isEndOfWord;
    }

    @Override
    public String toString() {
      return "Trie(" + root + ")";
    }
  }

  private static <T> long dfsCount(Trie<T> trie, List<T> iter, Map<List<T>, Long> cache) {
    if (iter.isEmpty()) {
      cache.putIfAbsent(iter, 1L);
      return 1L;
    }
    if (cache.containsKey(iter)) {
      return cache.get(iter);
    }
    long count = 0L;
    for (int i = 0; i < iter.size(); i++) {
      List<T> prefix = iter.subList(0, i + 1);
      if (trie.search(prefix)) {
        count += dfsCount(trie, iter.subList(i + 1, iter.size()), cache);
      }
    }
    cache.putIfAbsent(iter, count);
    return count;
  }

  private static Pair<Integer, Long> solve(List<String> input) {
    String[] flags = input.stream()
        .filter(s -> s.contains(","))
        .findFirst()
        .orElse("")
        .split(", ");
    Trie<Character> trie = new Trie<>();
    for (String flag : flags) {
      trie.insert(flag.chars().mapToObj(c -> (char) c).toList());
    }
    List<String> reqs = input.stream()
        .skip(1)
        .filter(req -> req.matches(".*[wubrg].*"))
        .toList();
    var count = 0;
    var sum = 0L;
    for (String req : reqs) {
      var tmp = dfsCount(trie, req.chars().mapToObj(c -> (char) c).toList(), new HashMap<>());
      if (tmp > 0) {
        count++;
        sum += tmp;
      }
    }
    return new Pair<>(count, sum);
  }

  public static void main(String[] ignoredArgs) throws IOException {
    List<String> testInput = readInput("19t");
    Pair<Integer, Long> testResult = solve(testInput);
    if (testResult.first != 6) {
      throw new AssertionError("Expected 6 but got " + testResult.first);
    }
    List<String> input = readInput("19");
    long start = System.nanoTime();
    Pair<Integer, Long> result = solve(input);
    long end = System.nanoTime();
    System.out.println(result.first);
    System.out.println(result.second);
    System.out.println("Took " + (end - start) / 1_000_000 + " ms for both parts");
  }

  public static List<String> readInput(String fileName) throws IOException {
    return Files.readAllLines(Paths.get("src", fileName + ".txt"));
  }

  private record Pair<F, S>(F first, S second) {
  }
}