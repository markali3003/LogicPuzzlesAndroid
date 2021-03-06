package com.zwstudio.logicpuzzlesandroid.puzzles.clouds.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.domain.CloudsGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CloudsGameActivity : GameGameActivity<CloudsGame?, CloudsDocument?, CloudsGameMove?, CloudsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: CloudsDocument? = null
    override fun doc() = document!!

    protected var gameView: CloudsGameView? = null
    override fun getGameView() = gameView!!

    @AfterViews
    override fun init() {
        gameView = CloudsGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = CloudsGame(level.layout, this, doc())
        try {
            // restore game state
            for (rec in doc().moveProgress()) {
                val move = doc().loadMove(rec)
                game!!.setObject(move)
            }
            val moveIndex = doc().levelProgress().moveIndex
            if (moveIndex >= 0 && moveIndex < game!!.moveCount()) while (moveIndex != game!!.moveIndex()) game!!.undo()
        } finally {
            levelInitilizing = false
        }
    }

    @Click
    protected fun btnHelp() {
        CloudsHelpActivity_.intent(this).start()
    }
}