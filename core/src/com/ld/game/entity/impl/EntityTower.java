package com.ld.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld.game.assets.Assets;
import com.ld.game.entity.Entity;
import com.ld.game.entity.EntityEnemy;
import com.ld.game.map.Map;
import com.ld.game.particle.impl.EntityTurretProjectile;
import com.ld.game.sfx.Sfx;
import org.w3c.dom.css.Rect;

public class EntityTower extends Entity {

    private float range;

    private Rectangle rangeBody = new Rectangle();

    //if distance < range

    //towers should indicate their range while you're placing em, maybe if you hover them too

    private Sprite rangeSprite;

    private float damage;

    private String particle;

    private boolean hovered;

    private int cost;

    public EntityTower(int cost, Map map, String spriteName, Vector2 position, float damage, String particle) {
        super(map, spriteName, position, 0);
        this.range = range;
        this.damage = damage;

        this.cost = cost;

        this.particle = particle;

        float rangeX = position.x - (map.getMapDefinition().getTileWidth() * 2);
        float rangeY = position.y - (map.getMapDefinition().getTileHeight() * 2);
        float rangeWidth = map.getMapDefinition().getTileWidth() * 5;
        float rangeHeight = map.getMapDefinition().getTileHeight() * 5;

        this.rangeBody = new Rectangle(rangeX, rangeY, rangeWidth, rangeHeight);

        this.rangeSprite = new Sprite(Assets.getInstance().getSprite("enemypath.png"));
        this.rangeSprite.setSize(rangeWidth, rangeHeight);
    }

    private OrthographicCamera camera;

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        this.rangeSprite.setPosition(this.rangeBody.getX(), this.rangeBody.getY());
        this.rangeSprite.setAlpha(0.7f);
        this.rangeSprite.draw(batch);
        this.rangeSprite.setAlpha(1);

    }

    public void renderText(SpriteBatch batch) {
        if(this.hovered) {
            Vector2 pos = new Vector2(Gdx.graphics.getWidth() / 2 + this.getX() * 2 - camera.position.x * 2 - 64, Gdx.graphics.getHeight() / 2 + this.getY() * 2 + 48 - camera.position.y * 2);
            this.getMap().getMessageRenderer().renderSmall(batch, "Dmg: " + this.damage, pos);
            this.getMap().getMessageRenderer().renderSmall(batch, "(Right click to sell tower)", new Vector2(pos.x, pos.y + 32));
        }
    }

    private float elapsedSinceFire;

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        this.camera = camera;

        this.elapsedSinceFire += 1 * Gdx.graphics.getDeltaTime();

        if(this.elapsedSinceFire >= 1) {

            EntityEnemy target = this.findEntityInRange();

            if(target != null) {
                EntityTurretProjectile particle = new EntityTurretProjectile(this.getMap(), this.particle, 5, new Vector2(this.getX(), this.getY()), target, this.damage);
                this.getMap().spawnEntity(particle);
                this.elapsedSinceFire = 0;
            }
        }

        this.hovered = false;

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        Rectangle mouseRect = new Rectangle(mouse.x, mouse.y, 0, 0);

        if(mouseRect.overlaps(this.getBody())) {
            this.hovered = true;
        }

        if(this.hovered && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            this.getMap().removeEntity(this);
            this.getMap().getShop().addFunds(this.cost);

            Sfx.playRandom(Sfx.POP_SOUNDS, 0.4f);
        }

        //System.out.println(this.elapsedSinceFire);
    }

    private EntityEnemy findEntityInRange() {
        for(Entity entity : this.getMap().getEntities()) {
            if(entity instanceof EntityEnemy && !(entity instanceof EntitySacrifice)) {
                EntityEnemy enemy = ((EntityEnemy) entity);

                if(enemy.getBody().overlaps(this.rangeBody)) {
                    return enemy;
                }
            }
        }

        return null;
    }
}
