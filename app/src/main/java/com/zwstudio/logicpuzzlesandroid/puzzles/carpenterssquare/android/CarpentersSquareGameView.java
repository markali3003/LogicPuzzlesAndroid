package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.android;

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
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpenterSquareHint;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareCornerHint;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareDownHint;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareLeftHint;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareRightHint;
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareUpHint;

public class CarpentersSquareGameView extends CellsGameView {

    private CarpentersSquareGameActivity activity() {return (CarpentersSquareGameActivity)getContext();}
    private CarpentersSquareGame game() {return activity().game;}
    private int rows() {return isInEditMode() ? 5 : game().rows() - 1;}
    private int cols() {return isInEditMode() ? 5 : game().cols() - 1;}
    @Override protected int rowsInView() {return rows();}
    @Override protected int colsInView() {return cols();}
    private Paint gridPaint = new Paint();
    private Paint line1Paint = new Paint();
    private Paint line2Paint = new Paint();
    private Paint markerPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private Paint fixedPaint = new Paint();

    public CarpentersSquareGameView(Context context) {
        super(context);
        init(null, 0);
    }

    public CarpentersSquareGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CarpentersSquareGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        gridPaint.setColor(Color.GRAY);
        gridPaint.setStyle(Paint.Style.STROKE);
        line1Paint.setColor(Color.WHITE);
        line1Paint.setStyle(Paint.Style.STROKE);
        line1Paint.setStrokeWidth(20);
        line2Paint.setColor(Color.YELLOW);
        line2Paint.setStyle(Paint.Style.STROKE);
        line2Paint.setStrokeWidth(20);
        markerPaint.setColor(Color.YELLOW);
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(5);
        textPaint.setAntiAlias(true);
        fixedPaint.setColor(Color.WHITE);
        fixedPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.BLACK);
        for (int r = 0; r < rows(); r++)
            for (int c = 0; c < cols(); c++) {
                canvas.drawRect(cwc(c), chr(r), cwc(c + 1), chr(r + 1), gridPaint);
                if (isInEditMode()) continue;
                Position p = new Position(r, c);
                CarpenterSquareHint o = game().pos2hint.get(p);
                if (o == null) continue;
                if (o instanceof CarpentersSquareCornerHint) {
                    int n = ((CarpentersSquareCornerHint) o).tiles;
                    HintState state = game().pos2State(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    String text = n == 0 ? "?" : String.valueOf(n);
                    drawTextCentered(text, cwc(c), chr(r), canvas, textPaint);
                    canvas.drawArc(cwc(c), chr(r), cwc(c + 1), chr(r + 1), 0, 360, true, fixedPaint);
                } else if (o instanceof CarpentersSquareLeftHint) {
                    HintState state = game().pos2State(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered("<", cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof CarpentersSquareRightHint) {
                    HintState state = game().pos2State(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered(">", cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof CarpentersSquareUpHint) {
                    HintState state = game().pos2State(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered("^", cwc(c), chr(r), canvas, textPaint);
                } else if (o instanceof CarpentersSquareDownHint) {
                    HintState state = game().pos2State(p);
                    textPaint.setColor(
                            state == HintState.Complete ? Color.GREEN :
                            state == HintState.Error ? Color.RED :
                            Color.WHITE
                    );
                    drawTextCentered("v", cwc(c), chr(r), canvas, textPaint);
                }
            }
        if (isInEditMode()) return;
        int markerOffset = 20;
        for (int r = 0; r < rows() + 1; r++)
            for (int c = 0; c < cols() + 1; c++) {
                GridLineObject[] dotObj = game().getObject(r, c);
                switch (dotObj[1]){
                case Line:
                    canvas.drawLine(cwc(c), chr(r), cwc(c + 1), chr(r),
                            game().get(r, c)[1] == GridLineObject.Line ? line1Paint : line2Paint);
                    break;
                case Marker:
                    canvas.drawLine(cwc2(c) - markerOffset, chr(r) - markerOffset, cwc2(c) + markerOffset, chr(r) + markerOffset, markerPaint);
                    canvas.drawLine(cwc2(c) - markerOffset, chr(r) + markerOffset, cwc2(c) + markerOffset, chr(r) - markerOffset, markerPaint);
                    break;
                }
                switch (dotObj[2]){
                case Line:
                    canvas.drawLine(cwc(c), chr(r), cwc(c), chr(r + 1),
                            game().get(r, c)[2] == GridLineObject.Line ? line1Paint :  line2Paint);
                    break;
                case Marker:
                    canvas.drawLine(cwc(c) - markerOffset, chr2(r) - markerOffset, cwc(c) + markerOffset, chr2(r) + markerOffset, markerPaint);
                    canvas.drawLine(cwc(c) - markerOffset, chr2(r) + markerOffset, cwc(c) + markerOffset, chr2(r) - markerOffset, markerPaint);
                    break;
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !game().isSolved()) {
            int offset = 30;
            int col = (int)((event.getX() + offset) / cellWidth);
            int row = (int)((event.getY() + offset) / cellHeight);
            int xOffset = (int)event.getX() - col * cellWidth - 1;
            int yOffset = (int)event.getY() - row * cellHeight - 1;
            if (!(xOffset >= -offset && xOffset <= offset || yOffset >= -offset && yOffset <= offset)) return true;
            CarpentersSquareGameMove move = new CarpentersSquareGameMove() {{
                p = new Position(row, col);
                obj = GridLineObject.Empty;
                dir = yOffset >= -offset && yOffset <= offset ? 1 : 2;
            }};
            if (game().switchObject(move))
                activity().app.soundManager.playSoundTap();
        }
        return true;
    }

}
