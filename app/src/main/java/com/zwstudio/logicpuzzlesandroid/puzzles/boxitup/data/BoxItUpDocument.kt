package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data

import com.zwstudio.logicpuzzlesandroid.common.data.GameDocument
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGame
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameMove
import org.androidannotations.annotations.EBean

@EBean
class BoxItUpDocument : GameDocument<BoxItUpGame?, BoxItUpGameMove?>() {
    protected override fun saveMove(move: BoxItUpGameMove, rec: MoveProgress) {
        rec.row = move.p!!.row
        rec.col = move.p!!.col
        rec.intValue1 = move.dir
        rec.intValue2 = move.obj!!.ordinal
    }

    override fun loadMove(rec: MoveProgress): BoxItUpGameMove {
        return object : BoxItUpGameMove() {
            init {
                p = Position(rec.row, rec.col)
                dir = rec.intValue1
                obj = GridLineObject.values()[rec.intValue2]
            }
        }
    }
}