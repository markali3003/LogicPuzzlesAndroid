package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterswall.domain.*

class CarpentersWallGameView : CellsGameView {
    private fun activity() = context as CarpentersWallGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode) 5 else game()!!.rows()

    private fun cols() = if (isInEditMode) 5 else game()!!.cols()

    override fun rowsInView() = rows()

    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val textPaint = TextPaint()
    private val fixedPaint = Paint()

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        fixedPaint.color = Color.WHITE
        fixedPaint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode) continue
            val o = game()!!.getObject(r, c)
            if (o is CarpentersWallCornerObject) {
                val o2 = o
                val n = o2.tiles
                textPaint.color = if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                val text = if (n == 0) "?" else n.toString()
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                canvas.drawArc(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), 0f, 360f, true, fixedPaint)
            } else if (o is CarpentersWallLeftObject) {
                val o2 = o
                textPaint.color = if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                drawTextCentered("<", cwc(c), chr(r), canvas, textPaint)
            } else if (o is CarpentersWallRightObject) {
                val o2 = o
                textPaint.color = if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                drawTextCentered(">", cwc(c), chr(r), canvas, textPaint)
            } else if (o is CarpentersWallUpObject) {
                val o2 = o
                textPaint.color = if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                drawTextCentered("^", cwc(c), chr(r), canvas, textPaint)
            } else if (o is CarpentersWallDownObject) {
                val o2 = o
                textPaint.color = if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.WHITE
                drawTextCentered("v", cwc(c), chr(r), canvas, textPaint)
            } else if (o is CarpentersWallWallObject) canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), wallPaint) else if (o is CarpentersWallMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, wallPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game()!!.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move: CarpentersWallGameMove = object : CarpentersWallGameMove() {
                init {
                    p = Position(row, col)
                    obj = CarpentersWallEmptyObject()
                }
            }
            if (game()!!.switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}