package com.zwstudio.logicpuzzlesandroid.home.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.zwstudio.logicpuzzlesandroid.common.data.GameProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.LevelProgress;
import com.zwstudio.logicpuzzlesandroid.common.data.MoveProgress;
import com.zwstudio.logicpuzzlesandroid.home.data.DBHelper;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeDocument;
import com.zwstudio.logicpuzzlesandroid.home.data.HomeGameProgress;
import com.zwstudio.logicpuzzlesandroid.puzzles.abc.data.AbcDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.battleships.data.BattleShipsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data.BoxItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.bridges.data.BridgesDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.clouds.data.CloudsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.linesweeper.data.LineSweeperDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.mosaik.data.MosaikDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.nurikabe.data.NurikabeDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.parks.data.ParksDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data.RoomsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.skyscrapers.data.SkyscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.slitherlink.data.SlitherLinkDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.sumscrapers.data.SumscrapersDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.tents.data.TentsDocument;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.ormlite.annotations.OrmLiteDao;


@EApplication
public class LogicPuzzlesApplication extends Application {

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<HomeGameProgress, Integer> daoHomeGameProgress;

    @OrmLiteDao(helper = DBHelper.class)
    public Dao<GameProgress, Integer> daoGameProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<LevelProgress, Integer> daoLevelProgress;
    @OrmLiteDao(helper = DBHelper.class)
    public Dao<MoveProgress, Integer> daoMoveProgress;

    @Bean
    public HomeDocument homeDocument;
    @Bean
    public AbcDocument abcDocument;
    @Bean
    public BattleShipsDocument battleshipsDocument;
    @Bean
    public BridgesDocument bridgesDocument;
    @Bean
    public BoxItUpDocument boxitupDocument;
    @Bean
    public CloudsDocument cloudsDocument;
    @Bean
    public HitoriDocument hitoriDocument;
    @Bean
    public LightenUpDocument lightenupDocument;
    @Bean
    public LineSweeperDocument linesweeperDocument;
    @Bean
    public LoopyDocument loopyDocument;
    @Bean
    public MagnetsDocument magnetsDocument;
    @Bean
    public MasyuDocument masyuDocument;
    @Bean
    public MosaikDocument mosaikDocument;
    @Bean
    public NurikabeDocument nurikabeDocument;
    @Bean
    public ParksDocument parksDocument;
    @Bean
    public RoomsDocument roomsDocument;
    @Bean
    public SkyscrapersDocument skyscrapersDocument;
    @Bean
    public SlitherLinkDocument slitherlinkDocument;
    @Bean
    public SumscrapersDocument sumscrapersDocument;
    @Bean
    public TentsDocument tentsDocument;

    @Bean
    public SoundManager soundManager;

    @Override
    public void onCreate() {
        super.onCreate();

        abcDocument.init();
        battleshipsDocument.init();
        bridgesDocument.init();
        boxitupDocument.init();
        cloudsDocument.init();
        hitoriDocument.init();
        lightenupDocument.init();
        linesweeperDocument.init();
        loopyDocument.init();
        magnetsDocument.init();
        masyuDocument.init();
        mosaikDocument.init();
        nurikabeDocument.init();
        parksDocument.init();
        roomsDocument.init();
        skyscrapersDocument.init();
        slitherlinkDocument.init();
        sumscrapersDocument.init();
        tentsDocument.init();

        soundManager.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        soundManager.doUnbindService();
        OpenHelperManager.releaseHelper();
    }
}
