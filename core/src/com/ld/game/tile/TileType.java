package com.ld.game.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ld.game.assets.Assets;

public enum TileType {
    Air(), Ground(), Wall(true), EnemyPath(), Start(), End(),
    Start1("start"), End1("end"), Start2("start"), End2("end"), Start3("start"), End3("end"),

    Wall1(true), Wall2(true), Water(), WaterEdge(true), WaterEdge1(true),

    Building(true), Building1(true), Building2(true), Building3(true), Building4(true), Building5(true),
    Building6(true), Building7(true), Building8(true), Building9(true), Building10(true), Building11(true),
    Building12(true), Building13(true), Building14(true), Building15(true), Building16(true), Building17(true),
    Building18(true), Building19(true), Building20(true), Building21(true),

    Broke(false),

    LavaLamp(false)
    ;

    TileType() {
        this.SPRITE = new Sprite(Assets.getInstance().getSprite(this.name().toLowerCase() + ".png"));
        this.TILE = new Tile(this);
        this.SOLID = false;
    }

    TileType(boolean solid) {
        this.SPRITE = new Sprite(Assets.getInstance().getSprite(this.name().toLowerCase() + ".png"));
        this.TILE = new Tile(this);
        this.SOLID = solid;
    }

    TileType(String spriteName) {
        this.SPRITE = new Sprite(Assets.getInstance().getSprite(spriteName + ".png"));
        this.TILE = new Tile(this);
        this.SOLID = false;
    }

    public final Sprite SPRITE;
    public final Tile TILE;
    public final boolean SOLID;
}
