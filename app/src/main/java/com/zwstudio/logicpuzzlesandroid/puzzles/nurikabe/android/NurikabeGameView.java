package com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zwstudio.logicpuzzlesandroid.common.android.CellsGameView;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeEmptyObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeWallObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeMarkerObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeMarkerOptions;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeObject;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.domain.NurikabeHintObject;
import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;
import com.zwstudio.logicpuzzlesandroid.common.domain.Position;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO: document your custom view class.
 */
// http://stackoverflow.com/questions/24842550/2d-array-grid-on-drawing-canvas
public class NurikabeGameView extends CellsGameView {

    private NurikabeGameActivity activity() {return (NurikabeGameActivity)getContext();}
    private NurikabeGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows();}
    private int cols() {return isInEditMode() ? 5 : game().cols();}
    private Paint gridPaint = new Paint();
    private Paint wallPaint = new Paint();
    private TextPaint textPaint = new TextPaint();

    public NurikabeGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public NurikabeGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public NurikabeGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.WHITE);
        gridPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (cols() < 1 || rows() < 1) return;
        cellWidth = getWidth() / cols() - 1;
        cellHeight = getHeight() / rows() - 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                NurikabeObject o = game().getObject(r, c);
                if (o instanceof NurikabeHintObject) {
                    NurikabeHintObject o2 = (NurikabeHintObject) o;
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
                } else if (o instanceof NurikabeWallObject) {
                    NurikabeWallObject o2 = (NurikabeWallObject) o;
                    canvas.drawRect(cwc(c) + 4, chr(r) + 4, cwc(c + 1) - 4, chr(r + 1) - 4, wallPaint);
                } else if (o instanceof NurikabeMarkerObject)
                    canvas.drawArc(cwc2(c) - 20, chr2(r) - 20, cwc2(c) + 20, chr2(r) + 20, 0, 360, true, wallPaint);
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int col = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);
            if (col >= cols() || row >= rows()) return true;
            NurikabeGameProgress rec = activity().doc().gameProgress();
            NurikabeGameMove move = new NurikabeGameMove();
            move.p = new Position(row, col);
            move.obj = new NurikabeEmptyObject();
            // http://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
            if (game().switchObject(move, NurikabeMarkerOptions.values()[rec.markerOption]))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}