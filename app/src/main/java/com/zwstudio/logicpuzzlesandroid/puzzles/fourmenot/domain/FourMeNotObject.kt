package com.zwstudio.logicpuzzlesandroid.puzzles.fourmenot.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

sealed class FourMeNotObject {
    open fun objAsString() = "empty"

    companion object {
        fun objFromString(str: String) = when (str) {
            "marker" -> FourMeNotMarkerObject()
            "tree" -> FourMeNotTreeObject()
            else -> FourMeNotEmptyObject()
        }
    }
}

class FourMeNotBlockObject : FourMeNotObject() {
    override fun objAsString() = "block"
}

class FourMeNotEmptyObject : FourMeNotObject()


class FourMeNotForbiddenObject : FourMeNotObject() {
    override fun objAsString() = "forbidden"
}

class FourMeNotMarkerObject : FourMeNotObject() {
    override fun objAsString() = "marker"
}

class FourMeNotTreeObject(var state: AllowedObjectState = AllowedObjectState.Normal) : FourMeNotObject() {
    override fun objAsString() = "tree"
}

class FourMeNotGameMove(val p: Position, var obj: FourMeNotObject = FourMeNotEmptyObject())
