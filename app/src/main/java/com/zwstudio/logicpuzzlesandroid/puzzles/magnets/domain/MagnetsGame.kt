package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocumentInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGame
import com.zwstudio.logicpuzzlesandroid.common.domain.GameInterface
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

class MagnetsGame(layout: List<String>, gi: GameInterface<MagnetsGame, MagnetsGameMove, MagnetsGameState>, gdi: GameDocumentInterface) : CellsGame<MagnetsGame, MagnetsGameMove, MagnetsGameState>(gi, gdi) {
    companion object {
        var offset = arrayOf(
            Position(-1, 0),
            Position(0, 1),
            Position(1, 0),
            Position(0, -1)
        )
    }

    var row2hint: IntArray
    var col2hint: IntArray
    var areas = mutableListOf<MagnetsArea>()
    var singles = mutableListOf<Position>()

    init {
        size = Position(layout.size - 2, layout[0].length - 2)
        row2hint = IntArray(rows() * 2)
        col2hint = IntArray(cols() * 2)
        for (r in 0 until rows() + 2) {
            val str = layout[r]
            for (c in 0 until cols() + 2) {
                val p2 = Position(r, c)
                when (val ch = str[c]) {
                    '.' -> {
                        areas.add(MagnetsArea(p2))
                        singles.add(p2)
                    }
                    'H' -> areas.add(MagnetsArea(p2, MagnetsAreaType.Horizontal))
                    'V' -> areas.add(MagnetsArea(p2, MagnetsAreaType.Vertical))
                    in '0'..'9' -> {
                        val n = ch - '0'
                        if (r >= rows())
                            col2hint[c * 2 + r - rows()] = n
                        else if (c >= cols())
                            row2hint[r * 2 + c - cols()] = n
                    }
                }
            }
        }
        val state = MagnetsGameState(this)
        states.add(state)
        levelInitilized(state)
    }

    private fun changeObject(move: MagnetsGameMove, f: (MagnetsGameState, MagnetsGameMove) -> Boolean): Boolean {
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

    fun switchObject(move: MagnetsGameMove) = changeObject(move, MagnetsGameState::switchObject)
    fun setObject(move: MagnetsGameMove) = changeObject(move, MagnetsGameState::setObject)

    fun getObject(p: Position) = state()[p]
    fun getObject(row: Int, col: Int) = state()[row, col]
    fun getRowState(id: Int) = state().row2state[id]
    fun getColState(id: Int) = state().col2state[id]
}
