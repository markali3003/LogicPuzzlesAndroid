package com.zwstudio.logicpuzzlesandroid.puzzles.bootyisland.domain;

import com.zwstudio.logicpuzzlesandroid.home.domain.HintState;

public class BootyIslandHintObject extends BootyIslandObject {
    public HintState state = HintState.Normal;
    public String objAsString() {
        return "hint";
    }
}
