package com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.android;

import android.view.View;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.data.FenceSentinelsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.fencesentinels.domain.FenceSentinelsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import static fj.data.List.iterableList;

@EActivity(R.layout.activity_game_game)
public class FenceSentinelsGameActivity extends GameGameActivity<FenceSentinelsGame, FenceSentinelsDocument, FenceSentinelsGameMove, FenceSentinelsGameState> {
    @Bean
    protected FenceSentinelsDocument document;
    public FenceSentinelsDocument doc() {return document;}

    protected FenceSentinelsGameView gameView;
    protected View getGameView() {return gameView;}

    @AfterViews
    protected void init() {
        gameView = new FenceSentinelsGameView(this);
        super.init();
    }

    protected void startGame() {
        String selectedLevelID = doc().selectedLevelID;
        List<String> layout = doc().levels.get(iterableList(doc().levels).toStream().indexOf(o -> o._1().equals(selectedLevelID)).orSome(0))._2();
        tvLevel.setText(selectedLevelID);
        updateSolutionUI();

        levelInitilizing = true;
        game = new FenceSentinelsGame(layout, this, doc());
        try {
            // restore game state
            for (MoveProgress rec : doc().moveProgress()) {
                FenceSentinelsGameMove move = doc().loadMove(rec);
                game.setObject(move);
            }
            int moveIndex = doc().levelProgress().moveIndex;
            if (!(moveIndex >= 0 && moveIndex < game.moveCount())) return;
            while (moveIndex != game.moveIndex())
                game.undo();
        } finally {
            levelInitilizing = false;
        }
    }

    @Click
    protected void btnHelp() {
        FenceSentinelsHelpActivity_.intent(this).start();
    }
}