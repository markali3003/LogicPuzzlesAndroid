package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView
import com.zwstudio.logicpuzzlesandroid.common.domain.AllowedObjectState
import com.zwstudio.logicpuzzlesandroid.common.domain.Position
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.*

class LightenUpGameView : CellsGameView {
    private fun activity() = context as LightenUpGameActivity

    private fun game() = activity().game

    private fun rows() = if (isInEditMode) 5 else game()!!.rows()

    private fun cols() = if (isInEditMode) 5 else game()!!.cols()

    override fun rowsInView() = rows()

    override fun colsInView() = cols()

    private val gridPaint = Paint()
    private val wallPaint = Paint()
    private val lightPaint = Paint()
    private val textPaint = TextPaint()
    private var dLightbulb: Drawable? = null

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
        lightPaint.color = Color.YELLOW
        lightPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.isAntiAlias = true
        dLightbulb = fromImageToDrawable("images/lightbulb.png")
    }

    override fun onDraw(canvas: Canvas) {
//        canvas.drawColor(Color.BLACK);
        for (r in 0 until rows()) for (c in 0 until cols()) {
            canvas.drawRect(cwc(c).toFloat(), chr(r).toFloat(), cwc(c + 1).toFloat(), chr(r + 1).toFloat(), gridPaint)
            if (isInEditMode) continue
            val o = game()!!.getObject(r, c)
            if (o!!.lightness > 0) canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), lightPaint)
            if (o is LightenUpWallObject) {
                val o2 = o
                canvas.drawRect(cwc(c) + 4.toFloat(), chr(r) + 4.toFloat(), cwc(c + 1) - 4.toFloat(), chr(r + 1) - 4.toFloat(), wallPaint)
                val n = game()!!.pos2hint[Position(r, c)]!!
                if (n >= 0) {
                    textPaint.color = if (o2.state == HintState.Complete) Color.GREEN else if (o2.state == HintState.Error) Color.RED else Color.BLACK
                    val text = n.toString()
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint)
                }
            } else if (o is LightenUpLightbulbObject) {
                dLightbulb!!.setBounds(cwc(c), chr(r), cwc(c + 1), chr(r + 1))
                val alpaha = if (o.state == AllowedObjectState.Error) 50 else 0
                dLightbulb!!.setColorFilter(Color.argb(alpaha, 255, 0, 0), PorterDuff.Mode.SRC_ATOP)
                dLightbulb!!.draw(canvas)
            } else if (o is LightenUpMarkerObject) canvas.drawArc(cwc2(c) - 20.toFloat(), chr2(r) - 20.toFloat(), cwc2(c) + 20.toFloat(), chr2(r) + 20.toFloat(), 0f, 360f, true, wallPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && !game()!!.isSolved) {
            val col = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (col >= cols() || row >= rows()) return true
            val move: LightenUpGameMove = object : LightenUpGameMove() {
                init {
                    p = Position(row, col)
                    obj = LightenUpEmptyObject()
                }
            }
            if (game()!!.switchObject(move)) activity().app.soundManager.playSoundTap()
        }
        return true
    }
}