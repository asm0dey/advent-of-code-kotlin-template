import kotlin.time.measureTimedValue

class Trie<T> {

    private val root = TrieNode<T>()

    class TrieNode<T> {
        val children = hashMapOf<T, TrieNode<T>>()
        var isEndOfWord = false
        override fun toString(): String {
            return "(children: $children, isEnd: $isEndOfWord)"
        }
    }

    fun insert(seq: Iterable<T>) {
        var current = root
        for (token in seq) {
            current = current.children.getOrPut(token) { TrieNode() }
        }
        current.isEndOfWord = true
    }

    fun search(seq: Iterable<T>): Boolean {
        var current = root
        for (token in seq) {
            current = current.children[token] ?: return false
        }
        return current.isEndOfWord
    }

    override fun toString(): String = "Trie($root)"
}

fun main() {

    fun <T> Trie<T>.dfsCount(iter: List<T>, cache: HashMap<List<T>, Long> = hashMapOf()): Long {
        if (iter.isEmpty()) return 1L.also { cache.compute(iter) { _, v -> v ?: 1 } }
        if (iter in cache) return cache[iter]!!
        var count = 0L
        for (i in iter.indices) {
            val prefix = iter.subList(0, i + 1)
            if (search(prefix)) {
                count += dfsCount(iter.subList(i + 1, iter.size), cache)
            }
        }
        return count.also { cache.compute(iter) { _, v -> v ?: count } }
    }

    fun solve(input: List<String>): Pair<Int, Long> {
        val flags = input.first { it.contains(',') }.split(", ")
        val trie = Trie<Char>()
        flags.forEach { trie.insert(it.toList()) }
        println(trie.toString())
        val reqs = input.drop(1).filter { it.contains(Regex("[wubrg]")) }
        val res = reqs.map { req -> trie.dfsCount(req.toList()) }
        return res.count { it > 0 } to res.sum()
    }

    val testInput = readInput("19t")
    check(solve(testInput).first == 6) { "Expected 6 but got ${solve(testInput).first}" }
    val input = readInput("19")
    measureTimedValue {
        solve(input)
    }.also {
        println(it.value.first)
        println(it.value.second)
        println("Took ${it.duration} for both parts")
    }
}