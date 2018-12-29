package com.ld.game.ui.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.assets.Assets;
import com.ld.game.entity.impl.EntityTower;
import com.ld.game.map.Map;
import com.ld.game.tile.TileNode;
import com.ld.game.ui.ShopCell;

public class ShopCellTestTower extends ShopCell {

    public ShopCellTestTower() {
        super(new Sprite(Assets.getInstance().getSprite("testtowericon.png")), 10, 1);
    }

    @Override
    public void onPurchased(Vector2 clickPosition, Map map, TileNode node) {
        super.onPurchased(clickPosition, map, node);
        System.out.println("Purchased");
        float damage = 1;
        node.setTower(new EntityTower(this.getPrice(), map, "testtower.png", new Vector2(node.getPosition().x, node.getPosition().y), damage, "1particle"));
    }
}
