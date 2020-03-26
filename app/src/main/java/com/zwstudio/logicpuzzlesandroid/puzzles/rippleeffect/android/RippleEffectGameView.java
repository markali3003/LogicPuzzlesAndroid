package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.GridLineObject;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove;

public class RippleEffectGameView extends CellsGameView {

    private RippleEffectGameActivity activity() {return (RippleEffectGameActivity)getContext();}
    private RippleEffectGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint linePaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public RippleEffectGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public RippleEffectGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RippleEffectGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.YELLOW);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(20);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Position p = new Position(r, c);
                int n = game().getObject(p);
                if (n == 0) continue;
                HintState s = game().pos2State(p);
                textPaint.setColor(
                        game().get(p) == n ? Color.GRAY :
                        s == HintState.Complete ? Color.GREEN :
                        s == HintState.Error ? Color.RED :
                        Color.WHITE
                );
                String text = String.valueOf(n);
                drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
            }
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                if (game().dots.get(r, c, 1) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r), linePaint);
                if (game().dots.get(r, c, 2) == GridLineObject.Line)
                    canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1), linePaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            RippleEffectGameMove move = new RippleEffectGameMove() {{
                p = new Position(row, col);
                obj = 0;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
