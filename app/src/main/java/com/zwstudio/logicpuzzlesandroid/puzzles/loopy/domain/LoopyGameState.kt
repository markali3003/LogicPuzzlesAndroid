package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain

import com.rits.cloning.Cloner
import com.zwstudio.logicpuzzlesandroid.common.domain.*
import fj.F
import fj.data.List
import java.util.*

class LoopyGameState(game: LoopyGame) : CellsGameState<LoopyGame, LoopyGameMove, LoopyGameState>(game) {
    var objArray: Array<Array<GridLineObject?>?>?
    operator fun get(row: Int, col: Int) = objArray!![row * cols() + col]

    operator fun get(p: Position) = get(p!!.row, p.col)

    private fun isValidMove(move: LoopyGameMove?) = !(move!!.p!!.row == rows() - 1 && move.dir == 2 || move.p!!.col == cols() - 1 && move.dir == 1)

    fun setObject(move: LoopyGameMove): Boolean {
        if (!isValidMove(move)) return false
        val dir = move!!.dir
        val dir2 = (dir + 2) % 4
        val p1 = move.p
        val p2 = p1!!.add(LoopyGame.offset.get(dir))
        if (!isValid(p2) || game!![p1][dir] == GridLineObject.Line || get(p1)!![dir] == move.obj) return false
        get(p1)!![dir] = move.obj
        get(p2)!![dir2] = get(p1)!![dir]
        updateIsSolved()
        return true
    }

    fun switchObject(move: LoopyGameMove): Boolean {
        if (!isValidMove(move)) return false
        val markerOption = MarkerOptions.values()[game!!.gdi.markerOption]
        val f = label@ F { obj: GridLineObject? ->
            when (obj) {
                GridLineObject.Empty -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Marker else GridLineObject.Line
                GridLineObject.Line -> return@label if (markerOption == MarkerOptions.MarkerLast) GridLineObject.Marker else GridLineObject.Empty
                GridLineObject.Marker -> return@label if (markerOption == MarkerOptions.MarkerFirst) GridLineObject.Line else GridLineObject.Empty
            }
            obj
        }
        val dotObj = get(move!!.p)
        move.obj = f.f(dotObj!![move.dir])
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 5/Loopy

        Summary
        Loop a loop! And touch all the dots!

        Description
        1. Draw a single looping path. You have to touch all the dots. As usual,
           the path cannot have branches or cross itself.
    */
    private fun updateIsSolved() {
        isSolved = true
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            val n = fj.data.Array.array(*get(p)).filter { o: GridLineObject? -> o == GridLineObject.Line }.length()
            when (n) {
                2 -> {
                    val node = Node(p.toString())
                    g.addNode(node)
                    pos2node[p] = node
                }
                else -> {
                    // 1. The path cannot have branches or cross itself.
                    // 1. You have to touch all the dots.
                    isSolved = false
                    return
                }
            }
        }
        for (p in pos2node.keys) {
            val dotObj = get(p)
            for (i in 0..3) {
                if (dotObj!![i] != GridLineObject.Line) continue
                val p2 = p.add(LoopyGame.offset.get(i))
                g.connectNode(pos2node[p], pos2node[p2])
            }
        }
        // 1. Draw a single looping path.
        g.setRootNode(List.iterableList(pos2node.values).head())
        val nodeList = g.bfs()
        if (nodeList.size != pos2node.size) isSolved = false
    }

    init {
        objArray = Cloner().deepClone(game.objArray)
    }
}