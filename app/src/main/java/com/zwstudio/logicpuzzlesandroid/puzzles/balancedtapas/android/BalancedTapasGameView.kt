package com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.android

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
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.android.AbcGameActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasEmptyObject
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGame
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasHintObject
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasMarkerObject
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasObject
import com.zwstudio.logicpuzzlesandroid.puzzles.balancedtapas.domain.BalancedTapasWallObject

import fj.F

class BalancedTapasGameView : CellsGameView {

    private fun activity() = context as BalancedTapasGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val linePaint = Paint()
    private val wallPaint = Paint()
    private val textPaint = TextPaint()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        gridPaint.color = Color.WHITE
        gridPaint.style = Paint.Style.STROKE
        linePaint.color = Color.YELLOW
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 10f
        wallPaint.color = Color.WHITE
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val o = game().getObject(r, c)
                if (o is BalancedTapasWallObject) {
                    val o2 = o
                    canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), wallPaint)
                } else if (o is BalancedTapasHintObject) {
                    val hint = game().pos2hint[Position(r, c)]!!
                    textPaint.color = when (o.state) {
                        HintState.Complete -> Color.GREEN
                        HintState.Error -> Color.RED
                        else -> Color.WHITE
                    }
                    fun hint2Str(i: Int): String {
                        val n = hint[i]
                        return if (n == -1) "?" else n.toString()
                    }
                    when (hint.size) {
                        1 -> drawTextCentered(hint2Str(0), cwc(c), chr(r), canvas, textPaint)
                        2 -> {
                            drawTextCentered(hint2Str(0), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            drawTextCentered(hint2Str(1), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        }
                        3 -> {
                            drawTextCentered(hint2Str(0), cwc(c), chr(r), cellWidth, cellHeight / 2, canvas, textPaint)
                            drawTextCentered(hint2Str(1), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            drawTextCentered(hint2Str(2), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        }
                        4 -> {
                            drawTextCentered(hint2Str(0), cwc(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            drawTextCentered(hint2Str(1), cwc2(c), chr(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            drawTextCentered(hint2Str(2), cwc(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                            drawTextCentered(hint2Str(3), cwc2(c), chr2(r), cellWidth / 2, cellHeight / 2, canvas, textPaint)
                        }
                    }
                } else if (o is BalancedTapasMarkerObject)
                    canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, wallPaint)
            }
        for (r in 0 until rows()) {
            val c = game().left
            val x = cwc(c) - if (c > game().right) cellWidth / 2 else 0
            canvas.drawLine(x.toFloat(), chr(r).toFloat(), x.toFloat(), chr(r + 1).toFloat(), linePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move = BalancedTapasGameMove(Position(row, col), BalancedTapasEmptyObject())
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }

}
