package com.ld.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ld.game.sfx.Sfx;

public class Assets {

    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public Assets() {
        this.assetManager = new AssetManager();

        this.loadAssets();
    }

    public void loadAssets() {
        load("air.png");
        load("ground.png");
        load("hover.png");
        load("wall.png");
        load("entity.png");
        load("enemypath.png");
        load("start.png");
        load("end.png");
        load("path.png");
        load("particle.png");
        load("testtower.png");
        load("testtowericon.png");
        load("inventorycell.png");
        load("hovercell.png");
        load("entitytwo.png");
        load("map.png");
        load("view.png");
        load("human.png");
        load("humanicon.png");
        load("message.png");
        load("entityred.png");

        load("wall1.png");
        load("wall2.png");

        load("water.png");
        load("wateredge.png");
        load("wateredge1.png");

        load("building.png");
        load("building1.png");
        load("building2.png");
        load("building3.png");
        load("building4.png");
        load("building5.png");
        load("building6.png");
        load("building7.png");
        load("building8.png");
        load("building9.png");
        load("building10.png");
        load("building11.png");
        load("building12.png");
        load("building13.png");
        load("building14.png");
        load("building15.png");
        load("building16.png");
        load("building17.png");
        load("building18.png");
        load("building19.png");
        load("building20.png");
        load("building21.png");

        load("entitythree.png");
        load("entityfour.png");
        load("entityfive.png");

        load("towertwoicon.png");
        load("towertwo.png");

        load("humanicon1.png");
        load("humanicon2.png");

        load("towerthree.png");
        load("towerthreeicon.png");

        load("broke.png");

        this.assetManager.load("sfx/flyswatter2.wav", Sound.class);
        this.assetManager.load("sfx/flyswatter3.wav", Sound.class);
        this.assetManager.load("sfx/flyswatter4.wav", Sound.class);
        this.assetManager.load("sfx/pop1.ogg", Sound.class);
        this.assetManager.load("sfx/pop2.ogg", Sound.class);
        this.assetManager.load("sfx/pop3.ogg", Sound.class);

        this.assetManager.load("sfx/bg.mp3", Music.class);

        load("towerfouricon.png");
        load("towerfour.png");

        load("entitysix.png");

        load("entityseven.png"); //of 9

        load("lavalamp.png"); //<3

        this.assetManager.finishLoading();

        Gdx.graphics.setTitle("Game");
    }

    public Texture getSprite(String name) {
        return this.assetManager.get(name);
    }

    public void load(String path) {
        this.assetManager.load(path, Texture.class);
    }

    public Sound getSound(String name) {
        return this.assetManager.get(name);
    }

    public Music getMusic(String name) {
        return this.assetManager.get(name);
    }

    public static Assets getInstance() {
        return instance;
    }

}
