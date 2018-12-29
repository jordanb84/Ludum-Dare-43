package com.ld.game.wave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld.game.entity.Entity;
import com.ld.game.entity.EntityEnemy;
import com.ld.game.entity.impl.EntitySacrifice;
import com.ld.game.map.Map;
import com.ld.game.ui.ShopCell;
import com.ld.game.ui.impl.ShopCellTowerFour;
import com.ld.game.ui.impl.ShopCellTowerThree;
import com.ld.game.ui.impl.ShopCellTowerTwo;
import com.ld.game.wave.impl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EnemyWaves {

    private List<Wave> waves = new ArrayList<Wave>();

    int currentWave = 0;

    private Map map;

    private float elapsedSinceSpawn;

    private SpriteBatch customBatch;
    private OrthographicCamera customCamera;

    private BitmapFont font;

    private HashMap<Integer, ShopCell> waveUnlocks = new HashMap<Integer, ShopCell>();

    private boolean wavesDone;

    public EnemyWaves(Map map) {
        this.map = map;

        System.out.println("Passing map " + map);

        this.waves.add(new WaveOne(map));
        this.waves.add(new WaveTwo(map));
        this.waves.add(new WaveThree(map));
        this.waves.add(new WaveFour(map));
        this.waves.add(new WaveFive(map));
        this.waves.add(new WaveSix(map));
        this.waves.add(new WaveSeven(map));

        this.customBatch = new SpriteBatch();
        this.customCamera = new OrthographicCamera();

        this.customCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.font = new BitmapFont(Gdx.files.internal("font16.fnt"));

        this.waveUnlocks.put(1, new ShopCellTowerTwo());
        this.waveUnlocks.put(3, new ShopCellTowerThree());
        this.waveUnlocks.put(5, new ShopCellTowerFour());
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.end();
        this.customBatch.begin();
        int waveNumberHuman = this.currentWave + 1;
        int totalWavesHuman = this.waves.size();

        this.font.draw(this.customBatch, "Wave " + waveNumberHuman + "/" + totalWavesHuman, 10, Gdx.graphics.getHeight() - 10);

        this.font.draw(this.customBatch, "Coins " + this.map.getShop().getBalance(), 10, Gdx.graphics.getHeight() - 56);
        this.customBatch.end();
        batch.begin();
    }

    public void update(OrthographicCamera camera) {
        this.elapsedSinceSpawn += 1 * Gdx.graphics.getDeltaTime();

        if(this.elapsedSinceSpawn >= this.getCurrentWave().getSpawnInterval()) {
            this.elapsedSinceSpawn = 0;
            boolean waveComplete = this.getCurrentWave().spawnNextEnemy(this.map);

            if(waveComplete) {
                if(!(this.currentWave >= this.waves.size() - 1)) {
                    this.nextWave();
                } else {
                    System.out.println("All waves completed");
                    this.wavesDone = true;
                }
            }
        }

        if(this.wavesDone) {
            boolean enemiesRemain = false;

            for(Entity entity : this.map.getEntities()) {
                if(entity instanceof EntityEnemy || entity instanceof EntitySacrifice) {
                    enemiesRemain = true;
                }
            }

            if(!enemiesRemain) {
                this.map.win();
            }
        }
    }

    public Wave getCurrentWave() {
        return this.waves.get(this.currentWave);
    }

    public void nextWave() {
        this.currentWave++;
        System.out.println("Moved to wave " + this.currentWave);

        int removeId = 0;

        for(HashMap.Entry<Integer, ShopCell> entry : this.waveUnlocks.entrySet()) {
            if(entry.getKey() == this.currentWave) {
                this.map.getShop().addTower(entry.getValue());

                removeId = entry.getKey();
            }
        }

        if(removeId > 0) {
            this.waveUnlocks.remove(removeId);
        }
    }

}
