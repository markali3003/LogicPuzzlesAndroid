package com.zwstudio.logicpuzzlesandroid.puzzles.walls.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.CellsGameState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import java.util.*

class WallsGameState(game: WallsGame) : CellsGameState<WallsGame, WallsGameMove, WallsGameState>(game) {
    var objArray: Array<WallsObject?>
    var pos2state: Map<Position, HintState> = HashMap<Position, HintState>()
    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    operator fun set(row: Int, col: Int, dotObj: WallsObject?) {
        objArray[row * cols() + col] = dotObj
    }

    operator fun set(p: Position, obj: WallsObject?) {
        set(p!!.row, p.col, obj)
    }

    fun setObject(move: WallsGameMove): Boolean {
        if (!isValid(move.p) || get(move.p) == move.obj) return false
        set(move.p, move.obj)
        updateIsSolved()
        return true
    }

    fun switchObject(move: WallsGameMove): Boolean {
        val o: WallsObject? = get(move.p)
        move.obj = if (o is WallsEmptyObject) WallsHorzObject() else if (o is WallsHorzObject) WallsVertObject() else if (o is WallsVertObject) WallsEmptyObject() else o
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Walls

        Summary
        Find the maze of Bricks

        Description
        1. In Walls you must fill the board with straight horizontal and
           vertical lines (walls) that stem from each number.
        2. The number itself tells you the total length of Wall segments
           connected to it.
        3. Wall pieces have two ways to be put, horizontally or vertically.
        4. Not every wall piece must be connected with a number, but the
           board must be filled with wall pieces.
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly: Boolean = game.gdi.isAllowedObjectsOnly()
        isSolved = true
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val o: WallsObject? = get(p)
            if (o is WallsEmptyObject) // 1. In Walls you must fill the board with straight horizontal and
            // vertical lines (walls) that stem from each number.
            // 4. Not every wall piece must be connected with a number, but the
            // board must be filled with wall pieces.
                isSolved = false else if (o is WallsHintObject) {
                val n2: Int = o.walls
                var n1 = 0
                for (i in 0..3) {
                    val os: Position = WallsGame.offset.get(i)
                    val p2 = p.add(os)
                    while (isValid(p2)) {
                        if (i % 2 == 0) // 3. Wall pieces have two ways to be put, horizontally or vertically.
                            if (get(p2) is WallsVertObject) n1++ else break else  // 3. Wall pieces have two ways to be put, horizontally or vertically.
                            if (get(p2) is WallsHorzObject) n1++ else break
                        p2.addBy(os)
                    }
                }
                // 2. The number itself tells you the total length of Wall segments
                // connected to it.
                val s: HintState = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
                if (s != HintState.Complete) isSolved = false
                (get(p) as WallsHintObject?)!!.state = s
            }
        }
    }

    init {
        objArray = arrayOfNulls<WallsObject>(rows() * cols())
        for (i in objArray.indices) objArray[i] = WallsEmptyObject()
        for ((p, n) in game.pos2hint.entries) {
            val o = WallsHintObject()
            o.walls = n
            o.state = HintState.Normal
            set(p, o)
        }
    }
}