package com.ld.game.ui.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ld.game.assets.Assets;
import com.ld.game.ui.ShopCell;

public class ShopCellEmpty extends ShopCell {

    public ShopCellEmpty() {
        super(new Sprite(Assets.getInstance().getSprite("inventorycell.png")), 10, 0);
    }

}
