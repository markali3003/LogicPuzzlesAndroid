package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.F2
import java.util.*

class TapARowGame(layout: List<String>, gi: GameInterface<TapARowGame?, TapARowGameMove?, TapARowGameState?>?, gdi: GameDocumentInterface?) : CellsGame<TapARowGame?, TapARowGameMove?, TapARowGameState?>(gi, gdi) {
    var pos2hint: MutableMap<Position?, List<Int>> = HashMap()
    private fun changeObject(move: TapARowGameMove?, f: F2<TapARowGameState?, TapARowGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: TapARowGameMove?): Boolean {
        return changeObject(move, F2 { obj: TapARowGameState?, move: TapARowGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: TapARowGameMove?): Boolean {
        return changeObject(move, F2 { obj: TapARowGameState?, move: TapARowGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): TapARowObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): TapARowObject? {
        return state()!![row, col]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(-1, 1),
            Position(0, 1),
            Position(1, 1),
            Position(1, 0),
            Position(1, -1),
            Position(0, -1),
            Position(-1, -1))
        var offset2 = arrayOf(
            Position(0, 0),
            Position(0, 1),
            Position(1, 0),
            Position(1, 1))
    }

    init {
        size = Position(layout.size, layout[0].length / 4)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val s = str.substring(c * 4, c * 4 + 4).trim { it <= ' ' }
                if (s.isEmpty()) continue
                val hint: MutableList<Int> = ArrayList()
                for (ch in s.toCharArray()) {
                    if (ch == '?' || ch >= '0' && ch <= '9') {
                        val n = if (ch == '?') -1 else ch - '0'
                        hint.add(n)
                    }
                }
                pos2hint[p] = hint
            }
        }
        val state = TapARowGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}