package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import fj.F2
import java.util.*

class FenceSentinelsGame(layout: List<String>, gi: GameInterface<FenceSentinelsGame?, FenceSentinelsGameMove?, FenceSentinelsGameState?>?, gdi: GameDocumentInterface?) : CellsGame<FenceSentinelsGame?, FenceSentinelsGameMove?, FenceSentinelsGameState?>(gi, gdi) {
    override fun isValid(row: Int, col: Int): Boolean {
        return row >= 0 && col >= 0 && row < size.row - 1 && col < size.col - 1
    }

    var pos2hint: MutableMap<Position?, Int> = HashMap()
    private fun changeObject(move: FenceSentinelsGameMove?, f: F2<FenceSentinelsGameState?, FenceSentinelsGameMove?, Boolean>): Boolean {
        if (canRedo()) {
            states.subList(stateIndex + 1, states.size).clear()
            moves.subList(stateIndex, states.size).clear()
        }
        val state = cloner.deepClone(state())
        val changed = f.f(state, move)
        if (changed) {
            states.add(state)
            stateIndex++
            moves.add(move)
            moveAdded(move)
            levelUpdated(states[stateIndex - 1], state)
        }
        return changed
    }

    fun switchObject(move: FenceSentinelsGameMove?): Boolean {
        return changeObject(move, F2 { obj: FenceSentinelsGameState?, move: FenceSentinelsGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: FenceSentinelsGameMove?): Boolean {
        return changeObject(move, F2 { obj: FenceSentinelsGameState?, move: FenceSentinelsGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): Array<GridLineObject?>? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): Array<GridLineObject?>? {
        return state()!![row, col]
    }

    fun pos2State(p: Position?): HintState? {
        return state()!!.pos2state[p]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
        var offset2 = arrayOf(
            Position(0, 0),
            Position(1, 1),
            Position(1, 1),
            Position(0, 0))
        var dirs = intArrayOf(1, 0, 3, 2)
    }

    init {
        size = Position(layout.size + 1, layout[0].length + 1)
        for (r in 0 until rows() - 1) {
            val str = layout[r]
            for (c in 0 until cols() - 1) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch >= '0' && ch <= '9') {
                    val n = ch - '0'
                    pos2hint[p] = n
                }
            }
        }
        val state = FenceSentinelsGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}