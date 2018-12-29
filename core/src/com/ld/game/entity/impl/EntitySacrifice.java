package com.ld.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld.game.entity.Direction;
import com.ld.game.entity.EntityEnemy;
import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

//can only be killed via sacrifice
public abstract class EntitySacrifice extends EntityTest {

    private float scale = 1;

    private Direction direction;

    private boolean hovered;

    private OrthographicCamera camera;

    public EntitySacrifice(Map map, TileType startType, TileType endType, String spriteName) {
        super(map, startType, endType, spriteName, 12);
        this.direction = Direction.UP;
    }

    @Override
    public void render(SpriteBatch batch) {
        this.getSprite().setPosition(this.getX(), this.getY());
        this.getSprite().setAlpha(1);
        this.getSprite().setScale(this.scale);
        this.getSprite().draw(batch);

        if(this.hovered && !(this.getMap().hasMadeSacrifice())) {
            Vector2 pos = new Vector2(Gdx.graphics.getWidth() / 2 + this.getX() * 2 - camera.position.x * 2 + 38, Gdx.graphics.getHeight() / 2 + this.getY() * 2 + 48 - camera.position.y * 2);
            this.getMap().getMessageRenderer().renderSmall(batch, "You must sacrifice a human to kill him!", pos);
            this.getMap().getMessageRenderer().renderSmall(batch, "(Click a human then click the advanced enemy)", new Vector2(pos.x, pos.y - 32));
        }
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.camera = camera;

        if(this.direction == Direction.DOWN) {
            this.scale -= 1 * Gdx.graphics.getDeltaTime();

            if(this.scale <= 1.1f) {
                this.direction = Direction.UP;
            }
        }

        if(this.direction == Direction.UP) {
            this.scale += 1 * Gdx.graphics.getDeltaTime();

            if(this.scale >= 1.4f) {
                this.direction = Direction.DOWN;
            }
        }

        this.hovered = false;

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        Rectangle mouseRect = new Rectangle(mouse.x, mouse.y, 0, 0);

        if(mouseRect.overlaps(this.getBody())) {
            this.hovered = true;
        }
    }

    @Override
    public void hurt(float damage) {

    }

    public float getScale() {
        return this.scale;
    }
}
