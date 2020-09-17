package minesweeper

class Cell(var state: State = State.UNEXPLORED) {
    companion object {
        enum class State {
            UNEXPLORED,
            UNEXPLORED_MARKED,
            EXPLORED_WITHOUT_MINES_AROUND,
            EXPLORED_WITH_MINES_AROUND,
            EXPLODED
        }
    }

    var minesAround = 0
    var hasMine = false

    fun isUnexplored(): Boolean {
        return state == State.UNEXPLORED
    }

    fun isExplored(): Boolean {
        return state == State.EXPLORED_WITHOUT_MINES_AROUND || state == State.EXPLORED_WITH_MINES_AROUND
    }

    fun isUnexploredMarked(): Boolean {
        return state == State.UNEXPLORED_MARKED
    }

    fun isExploredWithMinesAround(): Boolean {
        return minesAround != 0
    }

    fun isMined(): Boolean {
        return hasMine
    }

    override fun toString(): String {
        return when (state) {
            State.UNEXPLORED -> "."
            State.UNEXPLORED_MARKED -> "*"
            State.EXPLORED_WITHOUT_MINES_AROUND -> "/"
            State.EXPLORED_WITH_MINES_AROUND -> minesAround.toString()
            State.EXPLODED -> "X"
        }
    }
}
