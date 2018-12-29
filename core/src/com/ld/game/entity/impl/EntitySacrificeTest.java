package com.ld.game.entity.impl;

import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

public class EntitySacrificeTest extends EntitySacrifice {

    public EntitySacrificeTest(Map map, TileType startType, TileType endType) {
        super(map, startType, endType, "entityred.png");
    }

}
