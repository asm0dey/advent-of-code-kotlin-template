import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static javautils.JavaUtils.*;

@SuppressWarnings({"ExplicitToImplicitClassMigration", "RedundantSuppression"})
public class Day15 {
  public enum ObjectType {
    ROBOT, BOX
  }

  public static class GameObject {
    private JPoint2D pos;
    private final ObjectType type;
    private final JPoint2D size;

    public GameObject(JPoint2D pos, ObjectType type, JPoint2D size) {
      this.pos = pos;
      this.type = type;
      this.size = size;
    }

    public JPoint2D getPos() {
      return pos;
    }

    public void setPos(JPoint2D pos) {
      this.pos = pos;
    }

    public ObjectType getType() {
      return type;
    }

    public JPoint2D getSize() {
      return size;
    }

    public GameObject copy() {
      return new GameObject(pos.copy(), type, size.copy());
    }
  }

  public static Pair<JGrid<Character>, List<Character>> init(String input) {
    String[] splitInput = input.split("\n\n");
    JGrid<Character> grid = JGrid.asCharGrid(Arrays.asList(splitInput[0].split("\n")));
    List<Character> moves = splitInput[1].chars().mapToObj(c -> (char) c).toList();
    return new Pair<>(grid, moves);
  }

  public static int score(List<GameObject> objects) {
    return objects.stream()
        .filter(obj -> obj.getType() == ObjectType.BOX)
        .mapToInt(obj -> obj.getPos().y() * 100 + obj.getPos().x())
        .sum();
  }

  public static GameObject findRobot(List<GameObject> objects) {
    return objects.stream()
        .filter(obj -> obj.getType() == ObjectType.ROBOT)
        .findFirst()
        .orElseThrow();
  }

  public static boolean collides(JGrid<Character> grid, JPoint2D newPos, JPoint2D size) {
    for (int x = newPos.x(); x < newPos.x() + size.x(); x++) {
      for (int y = newPos.y(); y < newPos.y() + size.y(); y++) {
        if (grid.get(x, y) != null && grid.get(x, y) == '#') {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean move(JGrid<Character> grid, GameObject gameObject, List<GameObject> gameObjects, JPoint2D direction) {
    if (direction == null) return true;

    JPoint2D newPos = gameObject.pos.plus(direction);

    if (collides(grid, newPos, gameObject.size)) return true;

    List<GameObject> obstacles = gameObjects.stream()
        .filter(other -> other != gameObject)
        .filter(other -> {
          for (int dx = 0; dx < gameObject.getSize().x(); dx++) {
            for (int dy = 0; dy < gameObject.getSize().y(); dy++) {
              JPoint2D checkPos = newPos.plus(new JPoint2D(dx, dy));
              JPoint2D otherPos = other.getPos();
              if (checkPos.x() >= otherPos.x() && checkPos.x() < otherPos.x() + other.getSize().x() &&
                  checkPos.y() >= otherPos.y() && checkPos.y() < otherPos.y() + other.getSize().y()) {
                return true;
              }
            }
          }
          return false;
        })
        .toList();

    boolean isMovementBlocked = obstacles.stream().anyMatch(obstacle -> move(grid, obstacle, gameObjects, direction));
    if (!obstacles.isEmpty() && isMovementBlocked) return true;

    gameObject.setPos(newPos); // Update position
    return false;
  }

  public static Map<JPoint2D, Character> fillWalls(Map<JPoint2D, Character> map, int width) {
    Map<JPoint2D, Character> newMap = new HashMap<>();
    for (Map.Entry<JPoint2D, Character> entry : map.entrySet()) {
      JPoint2D point = entry.getKey();
      char value = entry.getValue();
      for (int i = 0; i < width; i++) {
        newMap.put(new JPoint2D(point.x() * width + i, point.y()), value == '#' ? '#' : '.');
      }
    }
    return newMap;
  }

  public static List<GameObject> createObjectsAndAddToMap(Map<JPoint2D, Character> map, int width, Map<JPoint2D, Character> expandedMap) {
    List<GameObject> resultingObjects = new ArrayList<>();
    for (Map.Entry<JPoint2D, Character> entry : map.entrySet()) {
      JPoint2D point = entry.getKey();
      char value = entry.getValue();
      if (value != '#' && value != '.') {
        JPoint2D pos = new JPoint2D(point.x() * width, point.y());
        GameObject gameObject = new GameObject(
            pos,
            value == '@' ? ObjectType.ROBOT : ObjectType.BOX,
            value == '@' ? new JPoint2D(1, 1) : new JPoint2D(width, 1)
        );
        expandedMap.put(pos, '.');
        resultingObjects.add(gameObject);
      }
    }
    return resultingObjects;
  }

  public static int runSimulation(JGrid<Character> origGrid, List<Character> moves, int width) {
    Map<JPoint2D, Character> gridData = new HashMap<>(origGrid.data());

    Map<JPoint2D, Character> expandedMap = fillWalls(gridData, width);

    List<GameObject> objects = createObjectsAndAddToMap(gridData, width, expandedMap);
    JGrid<Character> grid = new JGrid<>(expandedMap);

    for (Character move : moves) {
      JPoint2D direction = switch (move) {
        case '>' -> RIGHT;
        case '<' -> LEFT;
        case '^' -> UP;
        case 'v' -> DOWN;
        default -> null;
      };
      List<GameObject> snapshot = objects.stream().map(GameObject::copy).toList();
      if (move(grid, findRobot(objects), objects, direction)) {
        objects.clear();
        objects.addAll(snapshot);
      }
    }

    return score(objects);
  }

  public static void main(String[] ignoredArgs) throws IOException {
    String input = Files.readString(Path.of("src/15.txt"));
    Pair<JGrid<Character>, List<Character>> initResult = init(input);

    JGrid<Character> grid = initResult.first();
    List<Character> moves = initResult.second();

    System.out.println("p1: " + runSimulation(grid, moves, 1));
    System.out.println("p2: " + runSimulation(grid, moves, 2));
  }

  public record Pair<A, B>(A first, B second) {
  }
}