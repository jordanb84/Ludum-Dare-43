package com.ld.game.entity.impl;

import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

public class EntityEnemySeven extends EntityTest {

    public EntityEnemySeven(Map map, TileType startType, TileType endType) {
        super(map, startType, endType, "entityseven.png", 14);
    }

    @Override
    public float getMaxHealth() {
        return 48;
    }
}
