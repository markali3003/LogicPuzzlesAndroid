package com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.android

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
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.castlebailey.domain.CastleBaileyObject

class CastleBaileyGameView : CellsGameView {

    private fun activity() = context as CastleBaileyGameActivity
    private fun game() = activity().game
    private fun rows() = if (isInEditMode) 5 else game().rows()
    private fun cols() = if (isInEditMode) 5 else game().cols()
    override fun rowsInView() = rows()
    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val markerPaint = Paint()
    private val forbiddenPaint = Paint()
    private val textPaint = TextPaint()
    private val wallPaint = Paint()
    private val mathPaint1 = Paint()
    private val mathPaint2 = Paint()

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
        gridPaint.color = Color.GRAY
        gridPaint.style = Paint.Style.STROKE
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL_AND_STROKE
        markerPaint.strokeWidth = 5f
        forbiddenPaint.color = Color.RED
        forbiddenPaint.style = Paint.Style.FILL_AND_STROKE
        forbiddenPaint.strokeWidth = 5f
        textPaint.isAntiAlias = true
        wallPaint.color = Color.LTGRAY
        wallPaint.style = Paint.Style.FILL_AND_STROKE
        mathPaint1.style = Paint.Style.STROKE
        mathPaint1.color = Color.WHITE
        mathPaint2.style = Paint.Style.FILL
        mathPaint2.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        //        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows())
            for (c in 0 until cols()) {
                canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
                if (isInEditMode) continue
                val p = Position(r, c)
                val o = game().getObject(p)
                when (o) {
                    CastleBaileyObject.Marker ->
                        canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, markerPaint)
                    CastleBaileyObject.Wall ->
                        canvas.drawRect((cwc(c) + 4).toFloat(), (chr(r) + 4).toFloat(), (cwc(c + 1) - 4).toFloat(), (chr(r + 1) - 4).toFloat(), wallPaint)
                    CastleBaileyObject.Forbidden ->
                        canvas.drawArc((cwc2(c) - 20).toFloat(), (chr2(r) - 20).toFloat(), (cwc2(c) + 20).toFloat(), (chr2(r) + 20).toFloat(), 0f, 360f, true, forbiddenPaint)
                    else -> {}
                }
            }
        if (isInEditMode) return
        for ((p, value) in game().pos2hint) {
            val r = p.row
            val c = p.col
            canvas.drawArc((cwc(c) - cellWidth / 4).toFloat(), (chr(r) - cellHeight / 4).toFloat(), (cwc(c) + cellWidth / 4).toFloat(), (chr(r) + cellHeight / 4).toFloat(), 0f, 360f, true, mathPaint1)
            canvas.drawArc((cwc(c) - cellWidth / 4).toFloat(), (chr(r) - cellHeight / 4).toFloat(), (cwc(c) + cellWidth / 4).toFloat(), (chr(r) + cellHeight / 4).toFloat(), 0f, 360f, true, mathPaint2)
            val text = value.toString()
            val s = game().getPosState(p)
            textPaint.color = when (s) {
                HintState.Complete -> Color.GREEN
                HintState.Error -> Color.RED
                else -> Color.WHITE
            }
            drawTextCentered(text, cwc(c) - cellWidth / 4, chr(r) - cellHeight / 4, cellWidth / 2, cellHeight / 2, canvas, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game().isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move = CastleBaileyGameMove(Position(row, col))
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap()
        }
        return true
    }

}
