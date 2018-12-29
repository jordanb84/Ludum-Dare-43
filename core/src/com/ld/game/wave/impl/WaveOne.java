package com.ld.game.wave.impl;

import com.ld.game.entity.EnemyJourney;
import com.ld.game.entity.EntityEnemy;
import com.ld.game.entity.impl.EntityEnemyTwo;
import com.ld.game.entity.impl.EntitySacrificeTest;
import com.ld.game.entity.impl.EntityTest;
import com.ld.game.map.Map;
import com.ld.game.tile.TileType;
import com.ld.game.wave.Wave;

import java.util.ArrayList;
import java.util.List;

public class WaveOne extends Wave {

    public WaveOne(Map map) {
        super(map);
    }

    @Override
    public List<EntityEnemy> getEnemyQueue(Map map) {
        List<EntityEnemy> enemyQueue = new ArrayList<EntityEnemy>();

        for(int i = 0; i < 4; i++) {
            EnemyJourney randomJourney = map.getRandomJourney();
            enemyQueue.add(new EntityTest(map, randomJourney.getStartType(), randomJourney.getEndType(), "entity.png", 10));
        }

        enemyQueue.add(new EntitySacrificeTest(map, TileType.Start, TileType.End));

        for(int i = 0; i < 6; i++) {
            EnemyJourney randomJourney = map.getRandomJourney();
            enemyQueue.add(new EntityTest(map, randomJourney.getStartType(), randomJourney.getEndType(), "entity.png", 10));
        }

        for(int i = 0; i < 6; i++) {
            EnemyJourney randomJourney = map.getRandomJourney();
            enemyQueue.add(new EntityEnemyTwo(map, randomJourney.getStartType(), randomJourney.getEndType()));
        }

        return enemyQueue;
    }
}
