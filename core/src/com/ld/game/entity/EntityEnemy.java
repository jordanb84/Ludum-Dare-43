package com.ld.game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.map.Map;

public abstract class EntityEnemy extends Entity {

    private float health;

    private float maxHealth;

    private int value;

    public EntityEnemy(Map map, String spriteName, Vector2 position, float movementVelocity, int value) {
        super(map, spriteName, position, movementVelocity);
        this.health = this.getMaxHealth();
        this.value = value;
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);

        if(this.health <= 0) {
            this.getMap().removeEntity(this);
            this.getMap().getShop().addFunds(this.getValue());
        }
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void hurt(float damage) {
        this.health = this.health - damage;
    }

    public int getValue() {
        return value;
    }

    public abstract float getMaxHealth();

    public float getHealthPercent() {
        float percent = (this.getHealth() * 100.0f) / this.getMaxHealth();
        return percent / 100;
    }
}
