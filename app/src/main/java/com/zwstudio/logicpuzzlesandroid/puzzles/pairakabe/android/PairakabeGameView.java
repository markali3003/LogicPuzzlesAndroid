package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeHintObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeWallObject;

public class PairakabeGameView extends CellsGameView {

    private PairakabeGameActivity activity() {return (PairakabeGameActivity)getContext();}
    private PairakabeGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public PairakabeGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public PairakabeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PairakabeGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                PairakabeObject o = game().getObject(r, c);
                if (o instanceof PairakabeHintObject) {
                    PairakabeHintObject o2 = (PairakabeHintObject) o;
                    int n = game().pos2hint.get(new Position(r, c));
                    if (n >= 0) {
                        textPaint.setColor(
                                o2.state == HintState.Complete ? Color.GREEN :
                                o2.state == HintState.Error ? Color.RED :
                                Color.WHITE
                        );
                        String text = String.valueOf(n);
                        drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                    }
                } else if (o instanceof PairakabeWallObject) {
                    PairakabeWallObject o2 = (PairakabeWallObject) o;
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                } else if (o instanceof PairakabeMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, wallPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            PairakabeGameMove move = new PairakabeGameMove() {{
                p = new Position(row, col);
                obj = new PairakabeEmptyObject();
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
