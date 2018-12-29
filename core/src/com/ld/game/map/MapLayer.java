package com.ld.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class MapLayer {

    private List<TileType> tiles = new ArrayList<TileType>();

    private MapDefinition mapDefinition;

    public MapLayer(List<TileType> tiles, MapDefinition mapDefinition) {
        this.tiles = tiles;
        this.mapDefinition = mapDefinition;
    }

    public void render(SpriteBatch batch, float scale, float alpha, Vector2 cameraOffset) {
        //System.out.println("Drawing");
        int totalTiles = 0;

        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int tile = 0; tile < this.mapDefinition.getMapWidth(); tile++) {
                Vector2 position = new Vector2(tile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());

                position.set(position.x * scale, position.y * scale);
                position.add(cameraOffset.x, cameraOffset.y);

                this.tiles.get(totalTiles).SPRITE.setScale(scale);
                this.tiles.get(totalTiles).SPRITE.setAlpha(alpha);
                this.tiles.get(totalTiles).TILE.render(batch, position);
                this.tiles.get(totalTiles).SPRITE.setScale(1);
                this.tiles.get(totalTiles).SPRITE.setAlpha(1);
                totalTiles++;
                //System.out.println("drawn " + totalTiles);
            }
        }
    }

    public void render(SpriteBatch batch) {
        this.render(batch, 1, 1, new Vector2(0, 0));
    }

    public void update(OrthographicCamera camera) {
        int totalTiles = 0;

        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int tile = 0; tile < this.mapDefinition.getMapWidth(); tile++) {
                Vector2 position = new Vector2(tile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight());

                this.tiles.get(totalTiles).TILE.update(camera, position);
                totalTiles++;
            }
        }
    }

    public List<TileType> getTiles() {
        return tiles;
    }
}
