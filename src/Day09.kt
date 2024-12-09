import java.io.File

fun main(args: Array<String>) {
    data class BlockInfo(
        val id: Int,
        var isFile: Boolean,
        val initSize: Int,
        var curSize: Int = initSize,
    )

    fun part1(input: String): Long {
        val fileStructure = input.map { it.digitToInt() }
            .mapIndexed { index, i -> BlockInfo(index / 2, index % 2 == 0, i) }

        var sum = 0L
        var pointer = 0L
        val indexedFileStructure = fileStructure.withIndex().toList()
        for ((curIndex, fileBlock) in indexedFileStructure) {
            if (fileBlock.isFile && fileBlock.curSize > 0)
                repeat(fileBlock.curSize) {
                    sum += fileBlock.id * pointer++
                } else
                repeat(fileBlock.initSize) {
                    val found =
                        indexedFileStructure
                            .lastOrNull { (index, fb) -> index > curIndex && fb.isFile && fb.curSize > 0 }
                            ?: return@repeat
                    fileStructure[found.index].curSize--
                    sum += found.value.id * pointer++
                }
        }
        return sum
    }

    fun part2(input: String): Long {
        val fileStructure = input.map { it.digitToInt() }
            .mapIndexed { index, i -> BlockInfo(index / 2, index % 2 == 0, i) }

        var indexedFileStructure = fileStructure.withIndex().toList().toMutableList()
        val reversedFileStructure = indexedFileStructure.reversed()
        for ((index, value) in reversedFileStructure) {
            if (!value.isFile) continue
            val (leftIndex, leftSpace) = indexedFileStructure
                .firstOrNull { (sourceIndex, sourceValue) -> sourceIndex < index && !sourceValue.isFile && sourceValue.curSize >= value.initSize }
                ?: continue
            val realLeftIndex = indexedFileStructure.indexOf(IndexedValue(leftIndex, leftSpace))
            leftSpace.curSize -= value.initSize
            value.isFile = false
            indexedFileStructure.add(
                realLeftIndex,
                IndexedValue(realLeftIndex, value.copy(isFile = true))
            )
        }
        var sum = 0L
        var pointer = 0L
        for ((_, fileBlock) in indexedFileStructure) {
            repeat(fileBlock.curSize) {
                if (fileBlock.isFile) sum += fileBlock.id * pointer++ else pointer++
            }
        }

        return sum
    }

    if (args.isEmpty()) {
        val testInput = "2333133121414131402"
        check(part1(testInput) == 1928L) { "Expected 1928 but got ${part1(testInput)}" }

        val input = File("src/09.txt").readText().trim()
        part1(input).println()
        check(part2(testInput) == 2858L) { "Expected 2858 but got ${part2(testInput)}" }
        part2(input).println()
    } else {
        val input = readln()
        part1(input).println()
        part2(input).println()
    }
}



