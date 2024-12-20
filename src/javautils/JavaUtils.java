package javautils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class JavaUtils {
  public record JPoint2D(int x, int y) {
    public JPoint2D plus(JPoint2D other) {
      return new JPoint2D(x + other.x, y + other.y);
    }

    public JPoint2D times(int other) {
      return new JPoint2D(x * other, y * other);
    }

    public JPoint2D copy() {
      return new JPoint2D(x, y);
    }

    public JPoint2D minus(JPoint2D other) {
      return new JPoint2D(x - other.x, y - other.y);
    }

    public int manhattanDistance(JPoint2D other) {
      return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
  }

  public record JGrid<T>(Map<JPoint2D, T> data) implements Iterable<JPoint2D> {
    public static JGrid<Character> asCharGrid(List<String> input) {
      return new JGrid<>(
          input.stream()
              .map(String::toCharArray)
              .map(arr -> IntStream.range(0, arr.length)
                  .mapToObj(i -> arr[i])
                  .toList())
              .toList()
      );
    }

    public JGrid(List<List<T>> dataList) {
      this(dataListToMap(dataList));
    }

    private static <T> Map<JPoint2D, T> dataListToMap(List<List<T>> dataList) {
      Map<JPoint2D, T> mapData = new HashMap<>();
      for (int y = 0; y < dataList.size(); y++) {
        List<T> row = dataList.get(y);
        for (int x = 0; x < row.size(); x++) {
          mapData.put(new JPoint2D(x, y), row.get(x));
        }
      }
      return mapData;
    }

    public T get(int x, int y) {
      return data.get(new JPoint2D(x, y));
    }

    public void set(JPoint2D point, T value) {
      data.put(point, value);
    }

    public T get(JPoint2D point) {
      return data.get(point);
    }

    public boolean contains(JPoint2D point) {
      return data.containsKey(point);
    }

    public Stream<T> beam(JPoint2D source, JPoint2D direction, int amount) {
      return IntStream
          .rangeClosed(1, amount)
          .mapToObj(i -> source.plus(direction.times(i)))
          .filter(this::contains)
          .map(this::get);
    }

    @Override
    public Iterator<JPoint2D> iterator() {
      return data.keySet().iterator();
    }

    public void print() {
      int maxY = data.keySet().stream().mapToInt(JPoint2D::y).max().orElse(0);
      int minY = data.keySet().stream().mapToInt(JPoint2D::y).min().orElse(0);
      int maxX = data.keySet().stream().mapToInt(JPoint2D::x).max().orElse(0);
      int minX = data.keySet().stream().mapToInt(JPoint2D::x).min().orElse(0);
      for (int y = minY; y <= maxY; y++) {
        for (int x = minX; x <= maxX; x++) {
          T obj = get(x, y);
          System.out.print(obj == null ? " " : obj);
        }
        System.out.println();
      }
      System.out.println();
    }
  }

  public static final JPoint2D UP = new JPoint2D(0, -1);
  public static final JPoint2D DOWN = new JPoint2D(0, 1);
  public static final JPoint2D LEFT = new JPoint2D(-1, 0);
  public static final JPoint2D RIGHT = new JPoint2D(1, 0);
  public static final JPoint2D UP_RIGHT = new JPoint2D(1, -1);
  public static final JPoint2D UP_LEFT = new JPoint2D(-1, -1);
  public static final JPoint2D DOWN_RIGHT = new JPoint2D(1, 1);
  public static final JPoint2D DOWN_LEFT = new JPoint2D(-1, 1);
  public static final List<JPoint2D> directions = List.of(UP, DOWN, LEFT, RIGHT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT);
  public static final List<JPoint2D> mainDirections = List.of(UP, DOWN, LEFT, RIGHT);
}
