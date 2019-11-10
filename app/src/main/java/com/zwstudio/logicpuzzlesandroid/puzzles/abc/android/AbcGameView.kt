package com.zwstudio.logicpuzzlesandroid.puzzles.abc.android

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
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGame
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.domain.AbcGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.wallsentinels2.android.WallSentinels2GameActivity

/**
 * TODO: document your custom view class.
 */
class AbcGameView : CellsGameView {

    private fun activity() = context as AbcGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val markerPaint = Paint()
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
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 1 until rows() - 1)
            for (c in 1 until cols() - 1) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            }
        if (isInEditMode) return
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                val ch = game().getObject(r, c)
                if (ch == ' ') continue
                if (ch == '.')
                    canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, markerPaint)
                else {
                    val s = game().getState(r, c)
                    textPaint.color = if (s == HintState.Complete)
                        Color.GREEN
                    else if (s == HintState.Error)
                        Color.RED
                    else if (!game().isValid(r, c)) Color.GRAY else Color.WHITE
                    val text = ch.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            val move = AbcGameMove(Position(row, col), ' ')
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }

}