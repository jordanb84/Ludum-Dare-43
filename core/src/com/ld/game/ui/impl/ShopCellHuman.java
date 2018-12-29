package com.ld.game.ui.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld.game.assets.Assets;
import com.ld.game.entity.Entity;
import com.ld.game.entity.impl.EntitySacrifice;
import com.ld.game.map.Map;
import com.ld.game.particle.EntityParticle;
import com.ld.game.tile.TileNode;
import com.ld.game.ui.ShopCell;

public class ShopCellHuman extends ShopCell {

    public ShopCellHuman(String sprite) {
        super(new Sprite(Assets.getInstance().getSprite(sprite)), 0, 0);
    }

    @Override
    public void onPurchased(Vector2 clickPosition, Map map, TileNode node) {
        super.onPurchased(clickPosition, map, node);

        float rangeWidth = map.getMapDefinition().getTileWidth() * 5;
        float rangeHeight = map.getMapDefinition().getTileHeight() * 5;

        for(Entity entity : map.getEntities()) {
            if(entity instanceof EntitySacrifice) {
                Rectangle clickBody = new Rectangle(clickPosition.x - rangeWidth / 2, clickPosition.y - rangeHeight / 2, rangeWidth, rangeHeight);

                if(entity.getBody().overlaps(clickBody)) {
                    this.setUsed(true);
                    this.setItemSprite(new Sprite(Assets.getInstance().getSprite("inventorycell.png")));
                    map.removeEntity(entity);
                    map.spawnEntity(new EntityParticle(map, "blood", 3, new Vector2(entity.getX(), entity.getY())));

                    map.makeSacrifice();
                }
            }
        }
    }
}
