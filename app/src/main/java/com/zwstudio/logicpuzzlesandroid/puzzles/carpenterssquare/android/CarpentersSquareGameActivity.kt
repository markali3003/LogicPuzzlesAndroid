package com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.android

import android.view.View
import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.data.CarpentersSquareDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGame
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.carpenterssquare.domain.CarpentersSquareGameState
import fj.data.List
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class CarpentersSquareGameActivity : GameGameActivity<CarpentersSquareGame?, CarpentersSquareDocument?, CarpentersSquareGameMove?, CarpentersSquareGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: CarpentersSquareDocument? = null
    override fun doc() = document!!

    protected var gameView: CarpentersSquareGameView? = null
    override fun getGameView() = gameView!!

    @AfterViews
    override fun init() {
        gameView = CarpentersSquareGameView(this)
        super.init()
    }

    override fun startGame() {
        val selectedLevelID = doc().selectedLevelID
        val level = doc().levels[List.iterableList(doc().levels).toStream().indexOf { o: GameLevel -> o.id == selectedLevelID }.orSome(0)]
        tvLevel.text = selectedLevelID
        updateSolutionUI()
        levelInitilizing = true
        game = CarpentersSquareGame(level.layout, this, doc())
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
        CarpentersSquareHelpActivity_.intent(this).start()
    }
}