package com.ld.game.entity.impl;

import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

public class EntityEnemyThree extends EntityTest {

    public EntityEnemyThree(Map map, TileType startType, TileType endType) {
        super(map, startType, endType, "entitythree.png", 12);
    }

    @Override
    public float getMaxHealth() {
        return 16;
    }
}
