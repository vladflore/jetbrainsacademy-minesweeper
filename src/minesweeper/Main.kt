package minesweeper

fun main() {
    val mineField = MineField(9)
    mineField.placeMines()
    mineField.printField()
    mineField.play()
}
