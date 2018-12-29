package com.ld.game.entity.impl;

import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

public class EntityEnemyFour extends EntityTest {

    public EntityEnemyFour(Map map, TileType startType, TileType endType) {
        super(map, startType, endType, "entityfour.png", 14);
    }

    @Override
    public float getMaxHealth() {
        return 24;
    }
}
