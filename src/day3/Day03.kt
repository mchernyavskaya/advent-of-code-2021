package day3

import readInput

const val BOARD_SIZE = 5

class Board(input: List<String>) {
    private val matrix = input.map {
        it.split(Regex("\\s+"))
            .filter { el -> el.trim().isNotEmpty() }
            .map { el -> el.trim().toInt() }
            .map { el -> Pair(el, false) }
            .toMutableList()
    }

    private fun rowFull(rowIndex: Int): Boolean = matrix[rowIndex].none { !it.second }

    private fun colFull(colIndex: Int): Boolean {
        for (i in 0 until BOARD_SIZE) {
            if (!matrix[i][colIndex].second) {
                return false
            }
        }
        return true
    }

    fun markNumber(number: Int): Boolean {
        var result = false
        matrix.forEach { row ->
            for (i in 0 until BOARD_SIZE) {
                val el = row[i]
                if (el.first == number) {
                    row[i] = el.copy(second = true)
                    result = true
                }
            }
        }
        return result
    }

    fun unmarkedSum() = matrix.sumOf {
        it.filter { el -> !el.second }.sumOf { el -> el.first }
    }

    fun wins(): Boolean {
        for (i in 0 until BOARD_SIZE) {
            if (colFull(i) || rowFull(i)) {
                return true
            }
        }
        return false
    }
}

class Bingo(input: List<String>) {
    private val winners = input[0].split(",").map { it.toInt() }
    private val winningBoards = mutableSetOf<Int>()

    private val boards = input.filter { !it.contains(",") }
        .filter { it.trim().isNotEmpty() }
        .windowed(BOARD_SIZE, BOARD_SIZE)
        .map { Board(it) }

    /**
     * The score of the winning board can be calculated. Start by finding the sum
     * of all unmarked numbers on that board.
     * Then, multiply that sum by the number that was just called when the board won.
     */
    private fun playStep(index: Int): Int {
        val number = winners[index]
        println()
        println("*** Step $index - pulling number $number from a bag... ***")

        var result = 0

        boards.forEachIndexed() { i, b ->
            if (winningBoards.contains(i)) {
                println("Board $i already won, skipping...")
            } else {
                println("Checking board $i...")
                val marked = b.markNumber(number)
                println("Board #$i - marked? $marked")
                if (b.wins()) {
                    winningBoards.add(i)
                    println("Board $i wins!")
                    result = b.unmarkedSum() * number
                }
            }
        }
        return result
    }

    fun play(): Int {
        winningBoards.clear()
        var result = 0
        for (step in winners.indices) {
            result = playStep(step)
            if (result > 0) {
                println("+++++++++++ GAME OVER in ${step + 1} steps with result $result !!! +++++++++++")
                break
            }
        }
        return result
    }

    fun playTillAllWin(): Int {
        winningBoards.clear()
        var result = 0
        for (step in winners.indices) {
            result = playStep(step)
            if (winningBoards.size >= boards.size) {
                println("+++++++++++ GAME OVER in ${step + 1} steps with result $result !!! +++++++++++")
                break
            }
        }
        return result
    }
}


fun main() {
    fun part1(input: List<String>): Int {
        val game = Bingo(input)
        return game.play()
    }

    fun part2(input: List<String>): Int {
        val game = Bingo(input)
        return game.playTillAllWin()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day3/Example_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("day3/Example")
    println("******* PART 1: ${part1(input)} *******")
    println("******* PART 2: ${part2(input)} *******")
}
