fun main() {
    fun seqFromLong(l: Long) = sequence {
        var tmp = l
        while (true) {
            tmp = tmp xor (tmp * 64)
            tmp %= 16777216
            tmp = tmp xor (tmp / 32)
            tmp %= 16777216
            tmp = tmp xor (tmp * 2048)
            tmp %= 16777216
            yield(tmp)
        }
    }

    fun part2(allBuyers: List<Sequence<Long>>) = allBuyers
            .asSequence()
            .flatMap { buyer ->
                buyer
                    .take(2000)
                    .map { it % 10 }
                    .windowed(2)
                    .map { (a, b) -> b to b - a }
                    .windowed(4)
                    .map { (a, b, c, d) ->
                        listOf(a.second, b.second, c.second, d.second) to d.first
                    }
                    .distinctBy(Pair<List<Long>, Long>::first)
            }
            .groupingBy(Pair<List<Long>, Long>::first)
            .fold(0L) { acc, (_, res) -> acc + res }
            .maxBy(Map.Entry<List<Long>, Long>::value)
            .value

    fun part1(input: List<String>) = input.map(String::toLong).map { seqFromLong(it) }

    val testInput = readInput("22t")
    part1(testInput).sumOf { it.take(2000).last() }.println()
    val input = readInput("22")
    part1(input).sumOf { it.take(2000).last() }.println()
    part2(part1(input)).println()
}