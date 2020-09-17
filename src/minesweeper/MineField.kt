package minesweeper

import java.util.*
import kotlin.random.Random

class MineField(size: Int) {
    private val scanner = Scanner(System.`in`)
    private val minefield = Array(size) { Array(size) { Cell() } }
    private var totalMinesNumber = 0
    private var mineMarkedCell = 0
    private var freeMarkedCell = 0

    companion object {
        enum class Command {
            MINE,
            FREE
        }
    }

    fun play() {
        while (true) {
            print("Set/unset mine marks or claim a cell as free: ")
            val col = scanner.nextInt()
            val row = scanner.nextInt()
            val command = scanner.next()

            val realRowIndex = row - 1
            val realColumnIndex = col - 1

            val cell = minefield[realRowIndex][realColumnIndex]

            if (cell.isExploredWithMinesAround()) {
                println("There is a number here!")
                continue
            }

            when (Command.valueOf(command.toUpperCase())) {
                Command.MINE -> {
                    if (cell.isUnexplored()) {
                        cell.state = Cell.Companion.State.UNEXPLORED_MARKED
                        if (cell.isMined()) {
                            mineMarkedCell++
                        } else {
                            freeMarkedCell++
                        }
                    } else {
                        if (cell.isUnexploredMarked()) {
                            cell.state = Cell.Companion.State.UNEXPLORED
                            if (cell.isMined()) {
                                mineMarkedCell--
                            } else {
                                freeMarkedCell--
                            }
                        }
                    }
                }
                Command.FREE -> {
                    if (cell.isMined()) {
                        explode()
                        printField()
                        println("You stepped on a mine and failed!")
                        break
                    } else {
                        exploreCell(realRowIndex, realColumnIndex)
                    }
                }
            }

            printField()

            if (isWin()) {
                println("Congratulations! You found all the mines!")
                break
            }
        }
    }

    private fun explode() {
        for (index in minefield.indices) {
            for (cell in minefield[index]) {
                if (cell.hasMine) {
                    cell.state = Cell.Companion.State.EXPLODED
                }
            }
        }
    }

    private fun exploreCell(i: Int, j: Int) {
        val cell = minefield[i][j]
        val minesAroundCell = countMinesAroundCell(i, j)

        if (minesAroundCell != 0) {
            cell.minesAround = minesAroundCell
            cell.state = Cell.Companion.State.EXPLORED_WITH_MINES_AROUND
        } else {
            // current cell has no mines around it
            cell.state = Cell.Companion.State.EXPLORED_WITHOUT_MINES_AROUND
            val adjacentCells = adjacentCells(i, j)
            for (adjCell in adjacentCells) {
                if (isValid(adjCell) && !minefield[adjCell.first][adjCell.second].isExplored()) {
                    exploreCell(adjCell.first, adjCell.second)
                }
            }
        }
    }

    private fun isWin(): Boolean {
        return mineMarkedCell == totalMinesNumber || freeMarkedCell == (minefield.size * minefield.size - totalMinesNumber)
    }

    fun placeMines() {
        print("How many mines do you want on the field? ")
        totalMinesNumber = scanner.nextInt()
        var currentMinesNumber = 0
        while (currentMinesNumber != totalMinesNumber) {
            val cell = minefield[Random.nextInt(minefield.size)][Random.nextInt(minefield.size)]
            if (cell.isUnexplored()) {
                cell.hasMine = true
                currentMinesNumber++
            }
        }
    }

    private fun countMinesAroundCell(i: Int, j: Int): Int {
        val coordinates = adjacentCells(i, j)
        var count = 0
        for (pair in coordinates) {
            if (isValid(pair)) {
                if (minefield[pair.first][pair.second].isMined()) {
                    count++
                }
            }
        }
        return count
    }

    private fun adjacentCells(i: Int, j: Int): List<Pair<Int, Int>> {
        val left = Pair(i, j - 1)
        val right = Pair(i, j + 1)
        val up = Pair(i - 1, j)
        val down = Pair(i + 1, j)
        val upperLeftCorner = Pair(i - 1, j - 1)
        val upperRightCorner = Pair(i - 1, j + 1)
        val downLeftCorner = Pair(i + 1, j - 1)
        val downRightCorner = Pair(i + 1, j + 1)

        return listOf(left, right, up, down, upperLeftCorner, upperRightCorner, downLeftCorner, downRightCorner)
    }

    private fun isValid(pair: Pair<Int, Int>): Boolean {
        return pair.first in minefield.indices && pair.second in minefield[pair.first].indices
    }

    fun printField() {
        print(" |")
        for (idx in minefield.indices) {
            print(idx + 1)
        }
        println('|')

        print('-')
        print('|')
        for (idx in minefield.indices) {
            print('-')
        }
        println('|')

        for (index in minefield.indices) {
            print(index + 1)
            print('|')
            for (cell in minefield[index]) {
                print(cell)
            }
            println('|')
        }

        print('-')
        print('|')
        for (idx in minefield.indices) {
            print('-')
        }
        println('|')
    }
}
