import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Character.getNumericValue;
import static java.util.Collections.reverse;
import static java.util.stream.Gatherers.fold;

public class Day09 {
  private static class BlockInfo {
    private final int id;
    private boolean isFile;
    private final int initSize;
    private int curSize;

    public BlockInfo(int id, boolean isFile, int initSize) {
      this.id = id;
      this.isFile = isFile;
      this.initSize = initSize;
      this.curSize = initSize;
    }

    public BlockInfo copy() {
      BlockInfo copy = new BlockInfo(this.id, this.isFile, this.initSize);
      copy.curSize = this.curSize;
      return copy;
    }

    @Override
    public String toString() {
      return (isFile ? String.valueOf(id) : ".").repeat(curSize);
    }
  }

  private record IndexedBlockInfo(int index, BlockInfo value) {
  }

  private static long part1(String input) {
    List<BlockInfo> fileStructure = parseToFileStructure(input);

    long sum = 0L;
    long pointer = 0L;
    List<IndexedBlockInfo> indexedFileStructure = fileStructureWithIndices(fileStructure);

    for (int curIndex = 0; curIndex < indexedFileStructure.size(); curIndex++) {
      IndexedBlockInfo fileBlock = indexedFileStructure.get(curIndex);
      if (fileBlock.value.isFile && fileBlock.value.curSize > 0) {
        for (int i = 0; i < fileBlock.value.curSize; i++) {
          sum += fileBlock.value.id * pointer++;
        }
      } else {
        for (int i = 0; i < fileBlock.value.initSize; i++) {
          IndexedBlockInfo found = null;
          for (int j = indexedFileStructure.size() - 1; j > curIndex; j--) {
            IndexedBlockInfo candidate = indexedFileStructure.get(j);
            if (candidate.value.isFile && candidate.value.curSize > 0) {
              found = candidate;
              break;
            }
          }
          if (found == null) break;
          fileStructure.get(found.index).curSize--;
          sum += found.value.id * pointer++;
        }
      }
    }
    return sum;
  }

  private static List<IndexedBlockInfo> fileStructureWithIndices(List<BlockInfo> fileStructure) {
    return IntStream.range(0, fileStructure.size())
        .mapToObj(i -> new IndexedBlockInfo(i, fileStructure.get(i)))
        .collect(Collectors.toList());
  }

  private static List<BlockInfo> parseToFileStructure(String input) {
    return IntStream.range(0, input.length())
        .mapToObj(i -> new BlockInfo(i / 2, i % 2 == 0, getNumericValue(input.charAt(i))))
        .collect(Collectors.toList());
  }

  private static long part2(String input) {
    List<BlockInfo> fileStructure = parseToFileStructure(input);
    List<IndexedBlockInfo> indexedFileStructure = fileStructureWithIndices(fileStructure);
    List<IndexedBlockInfo> reversedFileStructure = new ArrayList<>(indexedFileStructure);
    reverse(reversedFileStructure);

    for (IndexedBlockInfo entry : reversedFileStructure) {
      if (!entry.value.isFile) continue;

      IndexedBlockInfo foundSpace = null;
      for (IndexedBlockInfo candidate : indexedFileStructure) {
        if (candidate.index < entry.index && !candidate.value.isFile && candidate.value.curSize >= entry.value.initSize) {
          foundSpace = candidate;
          break;
        }
      }

      if (foundSpace == null) continue;

      int realLeftIndex = indexedFileStructure.indexOf(foundSpace);
      foundSpace.value.curSize -= entry.value.initSize;
      entry.value.isFile = false;

      IndexedBlockInfo newEntry = new IndexedBlockInfo(
          realLeftIndex,
          entry.value.copy()
      );
      newEntry.value.isFile = true;

      indexedFileStructure.add(realLeftIndex, newEntry);
    }

    long sum = 0L;
    long pointer = 0L;
    for (IndexedBlockInfo fileBlock : indexedFileStructure) {
      for (int i = 0; i < fileBlock.value.curSize; i++) {
        if (fileBlock.value.isFile) {
          sum += fileBlock.value.id * pointer++;
        } else {
          pointer++;
        }
      }
    }

    return sum;
  }

  public static void main(String[] args) throws IOException {
    String testInput = "2333133121414131402";
    assert part1(testInput) == 1928L : "Expected 1928 but got " + part1(testInput);

    String input;
    if (args.length == 0) {
      input = new String(Files.readAllBytes(Paths.get("src/09.txt"))).trim();
      System.out.println(part1(input));

      assert part2(testInput) == 2858L : "Expected 2858 but got " + part2(testInput);
      System.out.println(part2(input));
    } else {
      input = args[0];
      System.out.println(part1(input));
      System.out.println(part2(input));
    }
  }
}