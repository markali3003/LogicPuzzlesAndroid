package com.zwstudio.logicpuzzlesandroid.puzzles.boxitagain.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class BoxItAgainGame(layout: List<String>, gi: GameInterface<BoxItAgainGame, BoxItAgainGameMove, BoxItAgainGameState>, gdi: GameDocumentInterface) : CellsGame<BoxItAgainGame, BoxItAgainGameMove, BoxItAgainGameState>(gi, gdi) {

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0)
        )
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    var objArray: Array<Array<GridLineObject>>
    var pos2hint = mutableMapOf<Position, Int>()

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = get(p.row, p.col)

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        objArray = arrayOfNulls(rows() * cols())
        for (i in objArray.indices) {
            objArray[i] = arrayOfNulls(4)
            Arrays.fill(objArray[i], GridLineObject.Empty)
        }
        for (r in 0 until rows() - 1) {
            val str = layout[r]
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch == ' ') continue
                val n = ch - '0'
                pos2hint[p] = n
            }
        }
        for (r in 0 until rows() - 1) {
            get(r, 0)[2] = GridLineObject.Line
            get(r + 1, 0)[0] = GridLineObject.Line
            get(r, cols() - 1)[2] = GridLineObject.Line
            get(r + 1, cols() - 1)[0] = GridLineObject.Line
        }
        for (c in 0 until cols() - 1) {
            get(0, c)[1] = GridLineObject.Line
            get(0, c + 1)[3] = GridLineObject.Line
            get(rows() - 1, c)[1] = GridLineObject.Line
            get(rows() - 1, c + 1)[3] = GridLineObject.Line
        }
        val state = BoxItAgainGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: BoxItAgainGameMove, f: (BoxItAgainGameState, BoxItAgainGameMove) -> Boolean): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        val changed = f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: BoxItAgainGameMove) = changeObject(move, BoxItAgainGameState::switchObject)
    fun setObject(move: BoxItAgainGameMove) = changeObject(move, BoxItAgainGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int)  = state()[row, col]
    fun getState(p: Position) = state().pos2state[p]
}