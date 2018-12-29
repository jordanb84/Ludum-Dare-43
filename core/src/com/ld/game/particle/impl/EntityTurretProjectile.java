package com.ld.game.particle.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.entity.Entity;
import com.ld.game.entity.EntityEnemy;
import com.ld.game.map.Map;
import com.ld.game.particle.EntityParticle;
import com.ld.game.sfx.Sfx;

public class EntityTurretProjectile extends EntityParticle {

    private EntityEnemy target;

    //should they be homing missiles, or just fire toward a single static position?
    //should reselect its target each bullet

    private float damage;

    public EntityTurretProjectile(Map map, String particleName, int duration, Vector2 position, EntityEnemy target, float damage) {
        super(map, particleName, duration, position);
        this.target = target;

        //this.getEffect().scaleEffect(0.2f);
        this.scale(0.3f);
        this.damage = damage;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.moveToward(new Vector2(this.target.getX(), this.target.getY()));

        if(this.getBody().overlaps(this.target.getBody())) {
            this.getMap().removeEntity(this);
            this.scale(0);
            this.target.hurt(this.damage);

            if(this.target.getHealth() <= 0) {
                Sfx.playRandom(Sfx.SQUISH_SOUNDS, 0.1f);
            }
        }
    }

}
