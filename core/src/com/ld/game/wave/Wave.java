package com.ld.game.wave;

import com.ld.game.entity.EntityEnemy;
import com.ld.game.map.Map;

import java.util.ArrayList;
import java.util.List;

public abstract class Wave {

    private List<EntityEnemy> spawnQueue;

    private int totalSpawed = 0;

    private float spawnInterval = 1.5f;

    private Map map;

    public Wave(Map map) {
        this.spawnQueue = this.getEnemyQueue(map);
        this.map = map;
    }

    public abstract List<EntityEnemy> getEnemyQueue(Map map);

    public List<EntityEnemy> getSpawnQueue() {
        return spawnQueue;
    }

    public float getSpawnInterval() {
        return spawnInterval;
    }

    public boolean spawnNextEnemy(Map map) {
        if(this.totalSpawed < this.spawnQueue.size()) {
            map.spawnEntity(this.spawnQueue.get(this.totalSpawed));
            this.totalSpawed++;

            System.out.println("Spawned " + this.totalSpawed + "/" + this.spawnQueue.size());

            return false;
        }

        return true;
    }

    public Map getMap() {
        return map;
    }
}
