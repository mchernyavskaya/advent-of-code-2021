package day6

import readInput

const val DAYS = 80
const val MORE_DAYS = 256
const val NEWBORN_MAX_SEED = 8
const val MAX_SEED = 6

class FishFarm(input: String) {
    private val fish = mutableListOf<Fish>()

    init {
        fish.addAll(input.split(",")
            .filter { it.trim().isNotEmpty() }
            .map { Fish(it.trim().toInt()) }
        )
    }

    private fun plusDay() {
        val newborns = fish.mapNotNull { it.plusDay() }
        fish.addAll(newborns)
    }

    fun plusDays(days: Int): Int {
        for (i in 0 until days) {
            plusDay()
        }
        return fish.size
    }
}

class Fish(private var seed: Int = NEWBORN_MAX_SEED) {
    fun plusDay(): Fish? {
        if (seed == 0) {
            seed = MAX_SEED
            return Fish()
        }
        seed--
        return null
    }
}

class FishFarmCounter(input: String) {
    private val fish = LongArray(NEWBORN_MAX_SEED + 1)

    init {
        input.split(",")
            .filter { it.trim().isNotEmpty() }
            .map { it.trim().toInt() }
            .forEach {
                fish[it]++
            }

    }

    private fun plusDay() {
        val parentsCount = fish[0]
        fish.takeLast(NEWBORN_MAX_SEED)
            .onEachIndexed { index, count ->
                fish[index] += count
                fish[index + 1] -= count
            }
        fish[NEWBORN_MAX_SEED] += parentsCount
        fish[MAX_SEED] += parentsCount
        fish[0] -= parentsCount
    }

    fun plusDays(days: Int): Long {
        for (i in 0 until days) {
            plusDay()
        }
        return fish.sum()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        // return FishFarm(input[0]).plusDays(DAYS)
        return FishFarmCounter(input[0]).plusDays(DAYS)
    }

    fun part2(input: List<String>): Long {
        return FishFarmCounter(input[0]).plusDays(MORE_DAYS)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day6/Example_test")
    val part1 = part1(testInput)
    check(part1 == 5934L)
    val part2 = part2(testInput)
    check(part2 == 26984457539L)

    val input = readInput("day6/Example")
    println("******* PART 1: ${part1(input)} *******")
    println("******* PART 2: ${part2(input)} *******")
}
