package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState

sealed class HolidayIslandObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> HolidayIslandMarkerObject()
            "tree" -> HolidayIslandTreeObject()
            else -> HolidayIslandEmptyObject()
        }
    }
}

class HolidayIslandEmptyObject : HolidayIslandObject()

class HolidayIslandForbiddenObject : HolidayIslandObject() {
    override fun objAsString() = "forbidden"

}

class HolidayIslandHintObject(var state: HintState = HintState.Normal, var tiles: Int = 0) : HolidayIslandObject() {
    override fun objAsString() = "hint"
}

class HolidayIslandMarkerObject : HolidayIslandObject() {
    override fun objAsString() = "marker"
}

class HolidayIslandTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : HolidayIslandObject() {
    override fun objAsString() = "tree"
}

class HolidayIslandGameMove(val p: Position, var obj: HolidayIslandObject = HolidayIslandEmptyObject())
