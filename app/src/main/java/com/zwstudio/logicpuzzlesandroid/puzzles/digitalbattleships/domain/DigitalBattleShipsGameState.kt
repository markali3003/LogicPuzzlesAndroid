package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.*
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

class DigitalBattleShipsGameState(game: DigitalBattleShipsGame) : CellsGameState<DigitalBattleShipsGame, DigitalBattleShipsGameMove, DigitalBattleShipsGameState>(game) {
    var objArray = Array(rows() * cols()) { DigitalBattleShipsObject.Empty }
    var row2state = Array(rows()) { HintState.Normal }
    var col2state = Array(cols()) { HintState.Normal }

    operator fun get(row: Int, col: Int) = objArray[row * cols() + col]
    operator fun get(p: Position) = this[p.row, p.col]
    operator fun set(row: Int, col: Int, obj: DigitalBattleShipsObject) {objArray[row * cols() + col] = obj}
    operator fun set(p: Position, obj: DigitalBattleShipsObject) {this[p.row, p.col] = obj}

    init {
        updateIsSolved()
    }

    fun setObject(move: DigitalBattleShipsGameMove): Boolean {
        val p = move.p
        if (!isValid(p) || this[p] == move.obj) return false
        this[p] = move.obj
        updateIsSolved()
        return true
    }

    fun switchObject(move: DigitalBattleShipsGameMove): Boolean {
        val markerOption = MarkerOptions.values()[game.gdi.markerOption]
        val p = move.p
        if (!isValid(p)) return false
        val o = this[p]
        move.obj = when (o) {
            DigitalBattleShipsObject.Empty ->
                if (markerOption == MarkerOptions.MarkerFirst) DigitalBattleShipsObject.Marker else DigitalBattleShipsObject.BattleShipUnit
            DigitalBattleShipsObject.BattleShipUnit -> DigitalBattleShipsObject.BattleShipMiddle
            DigitalBattleShipsObject.BattleShipMiddle -> DigitalBattleShipsObject.BattleShipLeft
            DigitalBattleShipsObject.BattleShipLeft -> DigitalBattleShipsObject.BattleShipTop
            DigitalBattleShipsObject.BattleShipTop -> DigitalBattleShipsObject.BattleShipRight
            DigitalBattleShipsObject.BattleShipRight -> DigitalBattleShipsObject.BattleShipBottom
            DigitalBattleShipsObject.BattleShipBottom ->
                if (markerOption == MarkerOptions.MarkerLast) DigitalBattleShipsObject.Marker else DigitalBattleShipsObject.Empty
            DigitalBattleShipsObject.Marker ->
                if (markerOption == MarkerOptions.MarkerFirst) DigitalBattleShipsObject.BattleShipUnit else DigitalBattleShipsObject.Empty
            else -> o
        }
        return setObject(move)
    }

    /*
        iOS Game: Logic Games/Puzzle Set 13/Digital Battle Ships

        Summary
        Please divert your course 12+1+2 to avoid collision

        Description
        1. Play like Solo Battle Ships, with a difference.
        2. Each number on the outer board tells you the SUM of the ship or
           ship pieces you're seeing in that row or column.
        3. A ship or ship piece is worth the number it occupies on the board.
        4. Standard rules apply: a ship or piece of ship can't touch another,
           not even diagonally.
        5. In each puzzle there are
           1 Aircraft Carrier (4 squares)
           2 Destroyers (3 squares)
           3 Submarines (2 squares)
           4 Patrol boats (1 square)

        Variant
        5. Some puzzle can also have a:
           1 Supertanker (5 squares)
    */
    private fun updateIsSolved() {
        val allowedObjectsOnly = game.gdi.isAllowedObjectsOnly
        isSolved = true
        for (r in 0 until rows())
            for (c in 0 until cols())
                if (this[r, c] == DigitalBattleShipsObject.Forbidden)
                    this[r, c] = DigitalBattleShipsObject.Empty
        // 2. Each number on the outer board tells you the SUM of the ship or
        // ship pieces you're seeing in that row.
        for (r in 0 until rows()) {
            var n1 = 0
            val n2 = game.row2hint[r]
            for (c in 0 until cols()) {
                if (this[r, c].isShipPiece())
                    // 3. A ship or ship piece is worth the number it occupies on the board.
                    n1 += game!![r, c]
            }
            row2state[r] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        // 2. Each number on the outer board tells you the SUM of the ship or
        // ship pieces you're seeing in that column.
        for (c in 0 until cols()) {
            var n1 = 0
            val n2 = game!!.col2hint[c]
            for (r in 0 until rows()) {
                if (this[r, c].isShipPiece())
                    // 3. A ship or ship piece is worth the number it occupies on the board.
                    n1 += game!![r, c]
            }
            col2state[c] = if (n1 < n2) HintState.Normal else if (n1 == n2) HintState.Complete else HintState.Error
            if (n1 != n2) isSolved = false
        }
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val o = this[r, c]
                if ((o == DigitalBattleShipsObject.Empty || o == DigitalBattleShipsObject.Marker) &&
                    allowedObjectsOnly && (row2state[r] != HintState.Normal || col2state[c] != HintState.Normal))
                    this[r, c] = DigitalBattleShipsObject.Forbidden
        }
        val g = Graph()
        val pos2node = mutableMapOf<Position, Node>()
        for (r in 0 until rows()) for (c in 0 until cols()) {
            val p = Position(r, c)
            if (this[p].isShipPiece()) {
                val node = Node(p.toString())
                g.addNode(node)
                pos2node[p] = node
            }
        }
        for ((p, node) in pos2node) {
            for (i in 0..3) {
                val p2 = p.add(DigitalBattleShipsGame.offset[i * 2])
                val node2 = pos2node[p2]
                if (node2 != null) g.connectNode(node, node2)
            }
        }
        val shipNumbers = mutableListOf(0, 0, 0, 0, 0)
        while (pos2node.isNotEmpty()) {
            g.setRootNode(pos2node.values.first())
            val nodeList = g.bfs()
            val area = pos2node.filter { (_, node) -> nodeList.contains(node) }.keys.toList().sorted()
            for (p in area)
                pos2node.remove(p)
            if (!(area.size == 1 && get(area[0]) == DigitalBattleShipsObject.BattleShipUnit || area.size in 2..4 && (
                    area.all { it.row == area[0].row } &&
                        this[area[0]] == DigitalBattleShipsObject.BattleShipLeft && this[area[area.size - 1]] == DigitalBattleShipsObject.BattleShipRight ||
                    area.all { it.col == area[0].col } &&
                        this[area[0]] == DigitalBattleShipsObject.BattleShipTop && this[area[area.size - 1]] == DigitalBattleShipsObject.BattleShipBottom) &&
                    (1 until area.size - 1).all { this[area[it]] == DigitalBattleShipsObject.BattleShipMiddle })) {
                isSolved = false
                continue
            }
            for (p in area)
                for (os in DigitalBattleShipsGame.offset) {
                    // 4. A ship or piece of ship can't touch another, not even diagonally.
                    val p2 = p.add(os)
                    if (!isValid(p2) || area.contains(p2)) continue
                    val o = get(p2)
                    if (this[p2].isShipPiece())
                        isSolved = false
                    else if (allowedObjectsOnly)
                        this[p] = DigitalBattleShipsObject.Forbidden
            }
            shipNumbers[area.size]++
        }
        // 5. In each puzzle there are
        //    1 Aircraft Carrier (4 squares)
        //    2 Destroyers (3 squares)
        //    3 Submarines (2 squares)
        //    4 Patrol boats (1 square)
        if (shipNumbers != listOf(0, 4, 3, 2, 1)) isSolved = false
    }
}