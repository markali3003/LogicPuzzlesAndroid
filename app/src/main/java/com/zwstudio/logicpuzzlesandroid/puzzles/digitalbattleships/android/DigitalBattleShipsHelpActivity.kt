package com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.data.DigitalBattleShipsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.digitalbattleships.domain.DigitalBattleShipsGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
class DigitalBattleShipsHelpActivity : GameHelpActivity<DigitalBattleShipsGame?, DigitalBattleShipsDocument?, DigitalBattleShipsGameMove?, DigitalBattleShipsGameState?>() {
    @kotlin.jvm.JvmField
    @Bean
    protected var document: DigitalBattleShipsDocument? = null
    override fun doc() = document!!
}