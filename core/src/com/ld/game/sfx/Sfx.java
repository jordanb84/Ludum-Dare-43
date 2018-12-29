package com.ld.game.sfx;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ld.game.assets.Assets;

import java.util.Random;

public enum Sfx {
    FlySwatter("flyswatter2.wav"), FlySwatter1("flyswatter3.wav"), FlySwatter2("flyswatter4.wav"),
    Pop("pop1.ogg"), Pop1("pop1.ogg"), Pop2("pop3.ogg"),

    Bg("bg.mp3", true)
    ;

    Sfx(String name) {
        this.sound = Assets.getInstance().getSound("sfx/" + name);
    }

    Sfx(String name, boolean music) {
        this.music = Assets.getInstance().getMusic("sfx/" + name);
    }

    public Sound sound;
    public Music music;

    public static void playSound(Sfx sound) {
        sound.sound.play(0.4f);
    }

    public static void playMusic(Sfx music) {
        music.music.play();
        music.music.setLooping(true);
    }

    public static void playSound(Sfx sound, float volume) {
        sound.sound.play(volume);
    }

    public static final Random random = new Random();

    public static void playRandom(Sfx[] choices, float volume) {
        playSound(choices[random.nextInt(choices.length)], volume);
    }

    public static final Sfx[] POP_SOUNDS = {Pop, Pop1, Pop2};
    public static final Sfx[] SQUISH_SOUNDS = {FlySwatter, FlySwatter1, FlySwatter2};
}
