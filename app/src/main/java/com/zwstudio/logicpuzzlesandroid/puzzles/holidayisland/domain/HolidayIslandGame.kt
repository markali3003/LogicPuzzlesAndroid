package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import fj.F2
import java.util.*

class HolidayIslandGame(layout: List<String>, gi: GameInterface<HolidayIslandGame?, HolidayIslandGameMove?, HolidayIslandGameState?>?, gdi: GameDocumentInterface?) : CellsGame<HolidayIslandGame?, HolidayIslandGameMove?, HolidayIslandGameState?>(gi, gdi) {
    var pos2hint: MutableMap<Position?, Int> = HashMap()
    private fun changeObject(move: HolidayIslandGameMove?, f: F2<HolidayIslandGameState?, HolidayIslandGameMove?, Boolean>): Boolean {
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

    fun switchObject(move: HolidayIslandGameMove?): Boolean {
        return changeObject(move, F2 { obj: HolidayIslandGameState?, move: HolidayIslandGameMove? -> obj!!.switchObject(move) })
    }

    fun setObject(move: HolidayIslandGameMove?): Boolean {
        return changeObject(move, F2 { obj: HolidayIslandGameState?, move: HolidayIslandGameMove? -> obj!!.setObject(move) })
    }

    fun getObject(p: Position?): HolidayIslandObject? {
        return state()!![p]
    }

    fun getObject(row: Int, col: Int): HolidayIslandObject? {
        return state()!![row, col]
    }

    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1))
    }

    init {
        size = Position(layout.size, layout[0].length)
        for (r in 0 until rows()) {
            val str = layout[r]
            for (c in 0 until cols()) {
                val p = Position(r, c)
                val ch = str[c]
                if (ch != ' ') pos2hint[p] = ch - '0'
            }
        }
        val state = HolidayIslandGameState(this)
        states.add(state)
        levelInitilized(state)
    }
}