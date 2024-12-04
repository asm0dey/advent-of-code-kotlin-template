package javautils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
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
    }

    public record JGrid<T>(T[][] data) implements Iterable<JPoint2D> {
        @SuppressWarnings("unchecked")
        public JGrid(List<List<T>> data) {
            this(data.stream().map(list -> list.toArray((T[]) new Object[0])).toArray(size -> (T[][]) new Object[size][]));
        }

        public T get(int x, int y) {
            return data[y][x];
        }

        public T get(JPoint2D point) {
            return data[point.y][point.x];
        }

        public boolean contains(JPoint2D point) {
            return point.x() >= 0 && point.x() < data[0].length && point.y() >= 0 && point.y() < data.length;
        }

        public Stream<T> beam(JPoint2D source, JPoint2D direction, int amount) {
            return IntStream
                .rangeClosed(1, amount)
                .mapToObj(i -> source.plus(direction.times(i)))
                .filter(this::contains)
                .map(this::get);
        }

        @NotNull
        @Override
        public Iterator<JPoint2D> iterator() {
            return new java.util.Iterator<>() {
                int rowIndex = 0;
                int colIndex = 0;

                @Override
                public boolean hasNext() {
                    return rowIndex < data.length && colIndex < data[rowIndex].length;
                }

                @Override
                public JPoint2D next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    var point = new JPoint2D(colIndex, rowIndex);
                    colIndex++;
                    if (colIndex >= data[rowIndex].length) {
                        colIndex = 0;
                        rowIndex++;
                    }
                    return point;
                }
            };
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
}
