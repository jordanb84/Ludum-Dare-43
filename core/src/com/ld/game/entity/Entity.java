package com.ld.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.assets.Assets;
import com.ld.game.map.Map;

public abstract class Entity {

    private Rectangle body;

    private Sprite sprite;

    private float movementVelocity;

    private Map map;

    public Entity(Map map, String spriteName, Vector2 position, float movementVelocity) {
        this.sprite = new Sprite(Assets.getInstance().getSprite(spriteName));

        this.body = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());

        this.movementVelocity = movementVelocity;

        this.map = map;
    }

    public void render(SpriteBatch batch) {
        this.sprite.setPosition(this.getX(), this.getY());
        this.sprite.draw(batch);
    }

    public void update(OrthographicCamera camera) {

    }

    public void move(Direction direction) {
        this.move(direction, this.getMovementVelocity());
    }

    public void move(Direction direction, float velocity) {
        Vector2 newPosition = new Vector2(this.getX(), this.getY());

        float delta = Gdx.graphics.getDeltaTime();

        switch(direction) {
            case UP:
                newPosition.add(0, velocity * delta);
                break;
            case DOWN:
                newPosition.add(0, -velocity * delta);
                break;
            case LEFT:
                newPosition.add(-velocity * delta, 0);
                break;
            case RIGHT:
                newPosition.add(velocity * delta, 0);
                break;
        }

        if(!this.collisionAt(newPosition)) {
            this.getBody().setPosition(newPosition.x, newPosition.y);
        }
    }

    public boolean moveToward(Vector2 destination) {
        Vector2 newPosition = new Vector2(this.getX(), this.getY());

        float x = this.getX();
        float y = this.getY();

        float dX = destination.x;
        float dY = destination.y;

        float delta = Gdx.graphics.getDeltaTime();

        if(x < dX) {
            newPosition.add(this.getMovementVelocity() * delta, 0);
        }
        if(x > dX) {
            newPosition.add(-this.getMovementVelocity() * delta, 0);
        }
        if(y < dY) {
            newPosition.add(0, this.getMovementVelocity() * delta);
        }
        if(y > dY) {
            newPosition.add(0, -this.getMovementVelocity() * delta);
        }

        if(!this.collisionAt(newPosition)) {
            this.setPosition(newPosition);
        }

        Rectangle destRectangle = new Rectangle(destination.x + this.getWidth() / 2 + this.getWidth() / 4, destination.y - this.getWidth() / 4, this.getWidth() / 4, this.getWidth() / 2);

        return this.getBody().overlaps(destRectangle);
    }

    public boolean collisionAt(Vector2 newPosition) {
        return false;
    }

    public Rectangle getBody() {
        return body;
    }

    public float getX() {
        return this.getBody().getX();
    }

    public float getY() {
        return this.getBody().getY();
    }

    public float getWidth() {
        return this.getBody().getWidth();
    }

    public float getHeight() {
        return this.getBody().getHeight();
    }

    public float getMovementVelocity() {
        return movementVelocity;
    }

    public void setMovementVelocity(float movementVelocity) {
        this.movementVelocity = movementVelocity;
    }

    public Map getMap() {
        return map;
    }

    public void setPosition(Vector2 position) {
        this.getBody().setPosition(position.x, position.y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void render(SpriteBatch batch, Vector2 position, float scale, float alpha) {
        this.getSprite().setPosition(position.x, position.y);
        this.getSprite().setScale(scale);
        this.getSprite().setAlpha(alpha);
        this.getSprite().draw(batch);
        this.getSprite().setScale(1);
        this.getSprite().setAlpha(1);
    }
}
