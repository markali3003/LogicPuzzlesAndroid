package com.zwstudio.logicpuzzlesandroid.puzzles.snake.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.data.SnakeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.snake.domain.SnakeGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class SnakeMainActivity extends GameMainActivity<SnakeGame, SnakeDocument, SnakeGameMove, SnakeGameState> {
    @Bean
    protected SnakeDocument document;
    public SnakeDocument doc() {return document;}

    @Click
    void btnOptions() {
        SnakeOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        SnakeGameActivity_.intent(this).start();
    }
}
