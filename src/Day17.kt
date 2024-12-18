import OpcodeD17.Companion.opcode
import OperandD17.Companion.combo
import OperandD17.Companion.literalCache
import kotlin.math.pow

sealed interface OperandD17 {
    fun print(): String

    companion object {
        val literalCache = Array(8) { Literal(it.toLong()) }
        private val comboCache = Array(7) {
            when (it) {
                0 -> O0
                1 -> O1
                2 -> O2
                3 -> O3
                4 -> O4
                5 -> O5
                6 -> O6
                7 -> O7
                else -> error("Unsupported combo $it")
            }
        }
        val Int.combo get() = comboCache[this]
    }

    operator fun invoke(registers: LongArray): Long
    data class Literal(val value: Long) : OperandD17 {
        override fun print(): String = value.toString()
        override fun invoke(registers: LongArray): Long = value
    }

    sealed interface Combo : OperandD17
    data object O0 : Combo {
        override fun print(): String = "0"
        override fun invoke(registers: LongArray): Long = 0
    }

    data object O1 : Combo {
        override fun print(): String = "1"
        override fun invoke(registers: LongArray): Long = 1
    }

    data object O2 : Combo {
        override fun print(): String = "2"
        override fun invoke(registers: LongArray): Long = 2
    }

    data object O3 : Combo {
        override fun print(): String = "3"
        override fun invoke(registers: LongArray): Long = 3
    }

    data object O4 : Combo {
        override fun print(): String = "A"
        override fun invoke(registers: LongArray): Long = registers[0]
    }

    data object O5 : Combo {
        override fun print(): String = "B"
        override fun invoke(registers: LongArray): Long = registers[1]
    }

    data object O6 : Combo {
        override fun print(): String = "C"
        override fun invoke(registers: LongArray): Long = registers[2]
    }

    data object O7 : Combo {
        override fun print(): String = "wrong operator"
        override fun invoke(registers: LongArray): Long = error("Operand is reserved")
    }
}

sealed interface OpcodeD17 {
    companion object {
        private val map = mapOf(0 to Adv, 1 to Bxl, 2 to Bst, 3 to Jnz, 4 to Bxc, 5 to Out, 6 to Bdv, 7 to Cdv)
        private val cache = Array(8) { map[it]!! }
        val Int.opcode get() = cache[this]
    }

    operator fun invoke(registers: LongArray, operand: Int): Long
    fun print(operand: Int): String

    data object Adv : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            val numerator = registers[0]
            val denominator = 2.0.pow(operand.combo(registers).toInt()).toLong()
            registers[0] = numerator/denominator
            registers[3] += 2L
            return -1
        }

        override fun print(operand: Int): String = "A = A / (2 ^ ${operand.combo.print()})"
    }

    data object Bxl : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            registers[1] = registers[1] xor literalCache[operand].value
            registers[3] += 2L
            return -1
        }

        override fun print(operand: Int): String = "B = B ^ ${literalCache[operand].print()}"
    }

    data object Bst : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            registers[1] = operand.combo(registers) and 0b111
            registers[3] += 2L
            return -1
        }

        override fun print(operand: Int): String = "B = ${operand.combo.print()} % 8"
    }

    data object Jnz : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            if (registers[0] == 0L) registers[3] += 2L
            else registers[3] = literalCache[operand].value
            return -1
        }

        override fun print(operand: Int): String = "A == 0 ? skip : JMP ${literalCache[operand].print()}"
    }

    data object Bxc : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            registers[1] = registers[1] xor registers[2]
            registers[3] += 2L
            return -1
        }

        override fun print(operand: Int): String = "B = B ^ C"
    }

    data object Out : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            registers[3] += 2L
            return operand.combo(registers) and 0b111
        }

        override fun print(operand: Int): String = "print ${operand.combo.print()} % 8"
    }

    data object Bdv : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            val numerator = registers[0]
            val denominator = 2.0.pow(operand.combo(registers).toDouble()).toLong()
            registers[1] = numerator / denominator
            registers[3] += 2L
            return -1
        }

        override fun print(operand: Int): String = "B = A / (2 ^ ${operand.combo.print()})"
    }

    data object Cdv : OpcodeD17 {
        override fun invoke(registers: LongArray, operand: Int): Long {
            val numerator = registers[0]
            val denominator = 2.0.pow(operand.combo(registers).toInt()).toInt()
            registers[2] = numerator / denominator
            registers[3] += 2L
            return -1
        }

        override fun print(operand: Int): String = "C = A / (2 ^ ${operand.combo.print()})"
    }
}

fun main() {

    fun List<Int>.execute(registers: LongArray, maxLength: Int = Int.MAX_VALUE): ArrayList<Long> {
        val results = arrayListOf<Long>()
        while (registers[3] < size) {
            val ip = registers[3].toInt()
            val opcode = this[ip].opcode
            val operand = this[ip + 1]
            val res = opcode(registers, operand)
            if (res > -1) {
                results += res
                if (results.size >= maxLength) break
            }
        }
        return results
    }

    fun part1(input: List<String>): String {
        val a = "\\d+".toRegex().find(input.first { it.startsWith("Register A") })!!.value.toLong()
        val b = "\\d+".toRegex().find(input.first { it.startsWith("Register B") })!!.value.toLong()
        val c = "\\d+".toRegex().find(input.first { it.startsWith("Register C") })!!.value.toLong()
        val registers = longArrayOf(a, b, c, 0)
        val program =
            "\\d+".toRegex().findAll(input.first { it.startsWith("Program") }).map { it.value.toInt() }.toList()
        val results = program.execute(registers)
        return results.joinToString(",")
    }


    fun part2(input: List<String>): Long {
        val b = "\\d+".toRegex().find(input.first { it.startsWith("Register B") })!!.value.toInt()
        val c = "\\d+".toRegex().find(input.first { it.startsWith("Register C") })!!.value.toInt()
        val program =
            "\\d+".toRegex().findAll(input.first { it.startsWith("Program") }).map { it.value.toInt() }.toList()
//        println(program.chunked(2).joinToString("\n") { (a, b) -> a.opcode.print(b) })
        var initLength = 1
        var a = 0L
//        println("Target program: ${program.joinToString(",")}")
        while (true) {
            val registers = longArrayOf(a, b.toLong(), c.toLong(), 0)
            val result = program.execute(registers, initLength).map { it.toInt() }
            if (result != program.takeLast(initLength)) a++
            else {
//                println("With A=$a we get the program ${result.joinToString(",")}")
                if (result.size == program.size) return a
                initLength++
                a = a shl 3
//                println("Next A to try $a")
            }
        }

    }

    val testInput = readInput("17t")
    part1(testInput).println()
    val input = readInput("17")
    part1(input).println()
    part2(input).println()
}