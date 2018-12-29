package com.ld.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.assets.Assets;
import com.ld.game.entity.Entity;
import com.ld.game.entity.EntityEnemy;
import com.ld.game.entity.impl.EntitySacrifice;
import com.ld.game.entity.impl.EntityTower;
import com.ld.game.map.Map;
import com.ld.game.map.MapLayer;

public class MiniMap {

    private Map map;

    private Sprite hudSprite;

    private Sprite viewSprite;

    public MiniMap(Map map) {
        this.map = map;
        this.hudSprite = new Sprite(Assets.getInstance().getSprite("map.png"));
        this.viewSprite = new Sprite(Assets.getInstance().getSprite("view.png"));
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float scale = 0.2f;
        float alpha = 0.4f;

        Vector2 cameraOffset = new Vector2(48 + camera.position.x, -160 + camera.position.y);

        for(MapLayer layer : this.map.getLayers()) {
            layer.render(batch, 0.2f, alpha, cameraOffset);
        }

        for(Entity entity : this.map.getEntities()) {
            if(entity instanceof EntityEnemy || entity instanceof EntityTower) {

                Vector2 position = new Vector2(entity.getX() * scale, entity.getY() * scale);
                position.add(cameraOffset.x, cameraOffset.y);

                float entityScale = scale * 1.4f;

                if(entity instanceof EntitySacrifice) {
                    EntitySacrifice sacrifice = ((EntitySacrifice) entity);
                    entityScale = sacrifice.getScale() * 2f * scale;
                }

                entity.render(batch, position, entityScale, 1);
            }
        }

        this.hudSprite.setPosition(cameraOffset.x + this.map.getMapDefinition().getTileWidth() / 2 - this.map.getMapDefinition().getTileWidth() / 8, cameraOffset.y + this.map.getMapDefinition().getTileHeight() / 2 - this.map.getMapDefinition().getTileHeight() / 8);
        this.hudSprite.draw(batch);

        /**
         * Pos = cameraOffset + cameraOffset * scale
         */

        this.viewSprite.setPosition(cameraOffset.x + camera.position.x * scale - Gdx.graphics.getWidth() / 2 * scale + 6, cameraOffset.y + camera.position.y * scale - Gdx.graphics.getHeight() / 2 * scale + 6);
        this.viewSprite.setScale(scale * 2.5f);
        this.viewSprite.setAlpha(alpha / 2);
        this.viewSprite.draw(batch);
    }

    public void update(OrthographicCamera camera) {

    }

}
