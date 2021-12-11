package day11

import readInput

const val INPUT_SIZE = 10
const val DAYS = 100

//const val MORE_DAYS = 256
const val MAX_ENERGY = 9
const val MIN_ENERGY = 0
const val TEXT_RESET = "\u001B[0m"
const val TEXT_YELLOW = "\u001B[33m"

class OctopusFlashCounter(input: List<String>) {
    private val octopuses = input.map {
        it.toCharArray()
            .map { chr -> chr.toString().toInt() }
            .toIntArray()
    }.toTypedArray()

    private var flashCounter = 0

    private fun flashOctopus(x: Int, y: Int, flashed: HashSet<Pair<Int, Int>>) {
        if (flashed.contains(Pair(x, y))) {
            return
        }
        flashCounter++
        val startX = if (x == 0) 0 else x - 1
        val startY = if (y == 0) 0 else y - 1
        val endX = if (x >= INPUT_SIZE - 1) x else x + 1
        val endY = if (y >= INPUT_SIZE - 1) y else y + 1
        octopuses[x][y] = MIN_ENERGY
        flashed.add(Pair(x, y))
        for (i in startX..endX) {
            for (j in startY..endY) {
                if (flashed.contains(Pair(i, j))) {
                    continue
                } else {
                    octopuses[i][j]++
                    if (octopuses[i][j] > MAX_ENERGY) {
                        flashOctopus(i, j, flashed)
                    }
                }
            }
        }
    }

    private fun plusDay() {
        octopuses.forEachIndexed { x, row ->
            row.forEachIndexed { y, _ ->
                octopuses[x][y]++
            }
        }

        val flashedAccumulator = HashSet<Pair<Int, Int>>()
        octopuses.forEachIndexed { x, row ->
            row.forEachIndexed { y, octopus ->
                if (octopus > MAX_ENERGY) {
                    flashOctopus(x, y, flashedAccumulator)
                }
            }
        }
    }

    private fun allFlashed(): Boolean {
        return octopuses.sumOf { it.sum() } == 0
    }

    fun plusDays(days: Int): Int {
        println("========= Before any steps")
        printOut()
        for (i in 0 until days) {
            plusDay()
            if (allFlashed()) {
                println("========= After step ${i + 1}")
                printOut()
            }
        }
        println("========= After all steps")
        printOut()
        return flashCounter
    }

    fun goUntilAllFlashed(): Int {
        println("========= Before any steps")
        printOut()
        var step = 0
        while (!allFlashed()) {
            plusDay()
            step++
            if (allFlashed()) {
                println("========= After step $step")
                printOut()
            }
        }
        return step
    }

    private fun printOut() {
        octopuses.forEach { row ->
            row.forEach {
                if (it == MIN_ENERGY) {
                    print(TEXT_YELLOW + it + TEXT_RESET)
                } else {
                    print(it)
                }
            }
            println()
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return OctopusFlashCounter(input).plusDays(DAYS)
    }

    fun part2(input: List<String>): Int {
        return OctopusFlashCounter(input).goUntilAllFlashed()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/Example_test")
//    val part1 = part1(testInput)
//    println("Part 1 on test: $part1")
//    check(part1 == 1656)
    val part2 = part2(testInput)
    println("Part 2 on test: $part2")
    check(part2 == 195)

    val input = readInput("day11/Example")
//    println("******* PART 1: ${part1(input)} *******")
    println("******* PART 2: ${part2(input)} *******")
}
