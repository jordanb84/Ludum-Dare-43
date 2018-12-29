package com.ld.game.entity.impl;

import com.ld.game.map.Map;
import com.ld.game.tile.TileType;

public class EntityEnemyTwo extends EntityTest {

    public EntityEnemyTwo(Map map, TileType startType, TileType endType) {
        super(map, startType, endType, "entitytwo.png", 10);
    }

    @Override
    public float getMaxHealth() {
        return 6;
    }
}
