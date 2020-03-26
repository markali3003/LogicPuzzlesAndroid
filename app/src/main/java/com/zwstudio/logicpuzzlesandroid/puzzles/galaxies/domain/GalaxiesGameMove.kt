package com.zwstudio.logicpuzzlesandroid.puzzles.galaxies.domain

import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position

open class GalaxiesGameMove {
    var p: Position? = null
    var dir = 0
    var obj: GridLineObject? = null
}