package com.zwstudio.logicpuzzlesandroid.puzzles.snake.domainimport

import com.zwstudio.logicpuzzlesandroid.common.domain.Position

enum class SnakeObject {
    Empty, Forbidden, Marker, Snake
}

class SnakeGameMove(val p: Position, var obj: SnakeObject = SnakeObject.Empty)
