package com.zwstudio.logicpuzzlesandroid.puzzles.taparow.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.data.TapARowDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGame
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.taparow.domain.TapARowGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_options)
class TapARowOptionsActivity : GameOptionsActivity<TapARowGame?, TapARowDocument?, TapARowGameMove?, TapARowGameState?>() {
    @Bean
    protected var document: TapARowDocument? = null
    override fun doc() = document!!
}