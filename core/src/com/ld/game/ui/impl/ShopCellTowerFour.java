package com.ld.game.ui.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.assets.Assets;
import com.ld.game.entity.impl.EntityTower;
import com.ld.game.map.Map;
import com.ld.game.tile.TileNode;
import com.ld.game.ui.ShopCell;

public class ShopCellTowerFour extends ShopCell {

    public ShopCellTowerFour() {
        super(new Sprite(Assets.getInstance().getSprite("towerfouricon.png")), 42, 6);
    }

    @Override
    public void onPurchased(Vector2 clickPosition, Map map, TileNode node) {
        super.onPurchased(clickPosition, map, node);
        System.out.println("Purchased");
        float damage = 6;
        node.setTower(new EntityTower(this.getPrice(), map, "towerfour.png", new Vector2(node.getPosition().x, node.getPosition().y), damage, "6particle"));
    }
}
