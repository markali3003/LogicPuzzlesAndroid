package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove

class RippleEffectGameView : CellsGameView {
    private fun activity() = context as RippleEffectGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode) 5 else game()!!.rows()

    private fun cols() = if (isInEditMode) 5 else game()!!.cols()

    override fun rowsInView() = rows()

    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val textPaint = TextPaint()

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
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 20f
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode) continue
            val p = Position(r, c)
            val n = game()!!.getObject(p)
            if (n == 0) continue
            val s = game()!!.pos2State(p)
            textPaint.color = if (game()!![p] == n) Color.GRAY else if (s == HintState.Complete) Color.GREEN else if (s == HintState.Error) Color.RED else Color.WHITE
            val text = n.toString()
            drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
        }
        for (r in 0 until rows() + 1) for (c in 0 until cols() + 1) {
            if (game()!!.dots[r, c, 1] == GridLineObject.Line) canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r).toFloat(), linePaint)
            if (game()!!.dots[r, c, 2] == GridLineObject.Line) canvas.drawLine(cwc(c).toFloat(), chr(r).toFloat(), cwc(c).toFloat(), chr(r + 1).toFloat(), linePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game()!!.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move: RippleEffectGameMove = object : RippleEffectGameMove() {
                init {
                    p = Position(row, col)
                    obj = 0
                }
            }
            if (game()!!.switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}