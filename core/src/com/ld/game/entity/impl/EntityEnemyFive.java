package com.ld.game.entity.impl;

import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

public class EntityEnemyFive extends EntityTest {

    public EntityEnemyFive(Map map, TileType startType, TileType endType) {
        super(map, startType, endType, "entityfive.png", 12);
    }

    @Override
    public float getMaxHealth() {
        return 28;
    }
}
