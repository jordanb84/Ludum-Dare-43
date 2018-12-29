package com.ld.game.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.map.Map;
import com.ld.game.sfx.Sfx;
import com.ld.game.tile.TileNode;

public class ShopCell {

    private Sprite itemSprite;

    private int price;

    private boolean used;

    private int damage;

    public ShopCell(Sprite itemSprite, int price, int damage) {
        this.itemSprite = itemSprite;
        this.price = price;
        this.damage = damage;
    }

    public void render(SpriteBatch batch, Vector2 position) {
        this.itemSprite.setPosition(position.x, position.y);
        this.itemSprite.draw(batch);
    }

    public void update(OrthographicCamera camera, Vector2 position) {

    }

    public void onPurchased(Vector2 clickPosition, Map map, TileNode node) {
        Sfx.playRandom(Sfx.POP_SOUNDS, 0.4f);
    }

    public int getPrice() {
        return price;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void setItemSprite(Sprite itemSprite) {
        this.itemSprite = itemSprite;
    }

    public int getDamage() {
        return damage;
    }
}
