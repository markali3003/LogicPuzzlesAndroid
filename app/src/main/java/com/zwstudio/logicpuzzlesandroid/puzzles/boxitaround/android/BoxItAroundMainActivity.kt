package com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.data.BoxItAroundDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGame
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitaround.domain.BoxItAroundGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class BoxItAroundMainActivity : GameMainActivity<BoxItAroundGame?, BoxItAroundDocument?, BoxItAroundGameMove?, BoxItAroundGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: BoxItAroundDocument? = null
    override fun doc() = document!!

    @Click
    fun btnOptions() {
        BoxItAroundOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        BoxItAroundGameActivity_.intent(this).start()
    }
}