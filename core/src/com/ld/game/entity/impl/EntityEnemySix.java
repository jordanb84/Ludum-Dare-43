package com.ld.game.entity.impl;

import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

public class EntityEnemySix extends EntityTest {

    public EntityEnemySix(Map map, TileType startType, TileType endType) {
        super(map, startType, endType, "entitysix.png", 14);
    }

    @Override
    public float getMaxHealth() {
        return 42;
    }
}
