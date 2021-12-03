package day2

import readInput

class RateCalculator(input: List<String>) {
    private val rows = input.map { it.toCharArray().map { c -> c.digitToInt() } }
    private val itemLength = if (rows.isNotEmpty()) rows[0].size else 0
    private val cols = mutableListOf<List<Int>>()

    init {
        for (i in 0 until itemLength) {
            cols.add(rows.map { it[i] }.toCollection(mutableListOf()))
        }
    }

    private fun gammaEpsilonRate(colIndex: Int): Triple<Int, Int, Boolean> {
        val col = cols[colIndex]
        val onesCount = col.filter { it == 1 }.size
        val zerosCount = col.size - onesCount
        val third = onesCount == zerosCount
        return if (onesCount >= zerosCount) Triple(1, 0, third) else Triple(0, 1, third)
    }

    fun powerConsumption(): Int {
        var gamma = ""
        var epsilon = ""
        for (i in 0 until itemLength) {
            val rate = gammaEpsilonRate(i)
            gamma += rate.first
            epsilon += rate.second
        }
        return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
    }

    fun generatorRating(mostCommon: Boolean): Int {
        var list = rows
        for (i in 0 until itemLength) {
            var calc = RateCalculator(list.map { it.joinToString("") })
            val rate = calc.gammaEpsilonRate(i)
            val filteredList = list.filter {
                it[i] == if (mostCommon) rate.first else rate.second
            }
            if (filteredList.size == 1) {
                return Integer.parseInt(filteredList[0].joinToString(""), 2)
            } else {
                list = filteredList
                if (list.isEmpty()) break
            }
        }
        return 0
    }
}


fun main() {
    // most and least common bits

    fun part1(input: List<String>): Int {
        val calc = RateCalculator(input)
        return calc.powerConsumption()
    }

    fun part2(input: List<String>): Int {
        val calc = RateCalculator(input)
        val oxy = calc.generatorRating(true)
        println(oxy)
        val co2 = calc.generatorRating(false)
        println(co2)
        return oxy * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day2/Example_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("day2/Example")
    println(part1(input))
    println(part2(input))
}
