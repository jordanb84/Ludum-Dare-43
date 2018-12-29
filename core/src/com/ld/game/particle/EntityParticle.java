package com.ld.game.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.entity.Entity;
import com.ld.game.map.Map;

public class EntityParticle extends Entity {

    private ParticleEffect effect;

    public EntityParticle(Map map, String particleName, int duration, Vector2 position) {
        super(map, "particle.png", position, 50);
        this.effect = new ParticleEffect();
        this.effect.load(Gdx.files.internal("particles/" + particleName + ".p"), Gdx.files.internal("particles"));

        this.effect.setDuration(duration);

        this.effect.start();
    }

    @Override
    public void render(SpriteBatch batch) {
        this.effect.setPosition(this.getX(), this.getY());
        this.effect.draw(batch);
    }

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.effect.update(Gdx.graphics.getDeltaTime());
        this.updatePosition(this.getX(), this.getY());
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public void updatePosition(float x, float y) {
        for(ParticleEmitter emitter : this.effect.getEmitters()) {
            emitter.setPosition(x, y);
        }
    }

    public void scale(float scale) {
        for(ParticleEmitter emitter : this.effect.getEmitters()) {
            emitter.scaleSize(scale);
        }
    }
}
