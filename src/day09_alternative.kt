@file:JvmName("Day09_2")

import java.io.File

fun main(args: Array<String>) {
    data class BlockInfo(val id: Int, var isFile: Boolean, val initSize: Int, var curSize: Int = initSize) {
        override fun toString(): String = (if (isFile) id.toString() else ".").repeat(curSize)
        val isSpace = !isFile
    }

    fun parseToFileStructure(input: String): List<BlockInfo> {
        return input.mapIndexed { idx, c ->
            BlockInfo(idx / 2, idx % 2 == 0, c.digitToInt())
        }
    }

    fun part1(input: String): Long {
        val fileStructure = parseToFileStructure(input)
        var sum = 0L
        var pointer = 0L
        val indexedFileStructure = fileStructure.withIndex().toList()

        for (fileBlock in indexedFileStructure) {
            if (fileBlock.value.isFile && fileBlock.value.curSize > 0) {
                repeat(fileBlock.value.curSize) {
                    sum += fileBlock.value.id * pointer++
                }
            } else {
                for (i in 0 until fileBlock.value.initSize) {
                    indexedFileStructure
                        .subList(fileBlock.index + 1, indexedFileStructure.size)
                        .findLast { it.value.isFile && it.value.curSize > 0 }
                        ?.let {
                            fileStructure[it.index].curSize--
                            sum += it.value.id * pointer++
                        } ?: break
                }
            }
        }
        return sum
    }

    fun part2(input: String): Long {
        val fileStructure = parseToFileStructure(input)
        val indexedFileStructure = fileStructure.withIndex().toMutableList()
        val reversedFileStructure = indexedFileStructure.reversed()

        for ((endIndex, endFile) in reversedFileStructure) {
            if (!endFile.isFile) continue

            indexedFileStructure
                .find { it.index < endIndex && it.value.isSpace && it.value.curSize >= endFile.initSize }
                ?.let {
                    val realLeftIndex = indexedFileStructure.indexOf(it)
                    it.value.curSize -= endFile.initSize
                    endFile.isFile = false

                    indexedFileStructure.add(
                        realLeftIndex,
                        IndexedValue(0, endFile.copy(isFile = true))
                    )
                }
        }

        var sum = 0L
        var pointer = 0L
        indexedFileStructure.forEach { fileBlock ->
            repeat(fileBlock.value.curSize) {
                if (fileBlock.value.isFile) {
                    sum += fileBlock.value.id * pointer++
                } else {
                    pointer++
                }
            }
        }

        return sum
    }

    val testInput = "2333133121414131402"
    assert(part1(testInput) == 1928L) { "Expected 1928 but got ${part1(testInput)}" }

    val input = if (args.isEmpty()) {
        File("src/09.txt").readText().trim()
    } else {
        args[0]
    }
    println(part1(input))

    assert(part2(testInput) == 2858L) { "Expected 2858 but got ${part2(testInput)}" }
    println(part2(input))
}