package com.ld.game.ui.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.assets.Assets;
import com.ld.game.entity.impl.EntityTower;
import com.ld.game.map.Map;
import com.ld.game.tile.TileNode;
import com.ld.game.ui.ShopCell;

public class ShopCellTowerTwo extends ShopCell {

    public ShopCellTowerTwo() {
        super(new Sprite(Assets.getInstance().getSprite("towertwoicon.png")), 18, 2);
    }

    @Override
    public void onPurchased(Vector2 clickPosition, Map map, TileNode node) {
        super.onPurchased(clickPosition, map, node);
        System.out.println("Purchased");
        float damage = 2;
        node.setTower(new EntityTower(this.getPrice(), map, "towertwo.png", new Vector2(node.getPosition().x, node.getPosition().y), damage, "2particle"));
    }
}
