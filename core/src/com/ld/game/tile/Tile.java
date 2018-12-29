package com.ld.game.tile;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Tile {

    private TileType type;

    public Tile(TileType type) {
        this.type = type;
    }

    public void render(SpriteBatch batch, Vector2 position) {
        this.type.SPRITE.setPosition(position.x, position.y);
        this.type.SPRITE.draw(batch);
    }

    public void update(OrthographicCamera camera, Vector2 position) {

    }

}
