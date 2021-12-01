fun main() {
    fun part1(input: List<String>): Int {
        val windows = input.windowed(2, 1)
        return windows.filter {
            it[1].toInt() > it[0].toInt()
        }.size
    }

    fun part2(input: List<String>): Int {
        val sums = input.windowed(3, 1).map { list ->
            list.sumOf { it.toInt() }
        }
        val windows = sums.windowed(2, 1)
        return windows.filter { it[1] > it[0] }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
