package com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.data.RippleEffectDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGame
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.rippleeffect.domain.RippleEffectGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_help)
open class RippleEffectHelpActivity : GameHelpActivity<RippleEffectGame?, RippleEffectDocument?, RippleEffectGameMove?, RippleEffectGameState?>() {
    @Bean
    protected var document: RippleEffectDocument? = null
    override fun doc(): RippleEffectDocument {
        return document!!
    }
}