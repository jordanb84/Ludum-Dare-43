package com.ld.game.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld.game.entity.impl.EntityTest;
import com.ld.game.entity.impl.EntityTower;
import com.ld.game.io.MapImporter;
import com.ld.game.map.Map;
import com.ld.game.map.MapDefinition;
import com.ld.game.map.MapLayer;
import com.ld.game.particle.EntityParticle;
import com.ld.game.sfx.Sfx;
import com.ld.game.state.State;
import com.ld.game.state.StateManager;
import com.ld.game.tile.TileNode;
import com.ld.game.tile.TileType;
import com.ld.game.ui.MessageRenderer;
import com.ld.game.ui.MiniMap;
import com.ld.game.ui.Shop;

public class StateTest extends State {

    private Map map;

    private MiniMap miniMap;

    private MessageRenderer messageRenderer;

    private boolean paused;

    private float elapsedLost;

    private float elapsedControlsText;

    private float wonElapsed;

    public StateTest(StateManager stateManager) {
        super(stateManager);
        //MapDefinition mapDefinition = new MapDefinition(32, 26, 16, 16, 3);

        this.reset(false);

        Sfx.playMusic(Sfx.Bg);

        Gdx.graphics.setTitle("Assault on The Sacrificial Humans");

        //this.map.spawnEntity(new EntityTest(this.map, new Vector2(128, 128)));
        //this.map.spawnEntity(new EntityTest(this.map, TileType.Start, TileType.End));

        //this.map.spawnEntity(new EntityParticle(this.map, "endexplode", 150, new Vector2(350, 300)));
    }

    public void reset(boolean retry) {
        MapDefinition mapDefinition = new MapDefinition(44, 34, 16, 16, 3);

        this.map = MapImporter.importMap(Gdx.files.internal("map/map13.map"), mapDefinition, true);

        this.miniMap = new MiniMap(this.map);

        this.messageRenderer = new MessageRenderer();

        this.map.setMessageRenderer(this.messageRenderer);

        String[] messages = {"Welcome! Your goal is to defend your base in the center of the map." +
                " You can purchase     turrets on the right and place them down in range of enemies. New turrets will become   available to you as waves progress. If you see an enemy that is RED, it is an advanced  type of enemy which will not be stopped by turrets. The only way to kill them is by     sacrificing one of the humans from the top bar: Click the human, then click the advancedenemy to destroy him. Good luck! [SPACE]"};

        this.paused = false;

        if(!retry) {
            this.messageRenderer.addMessage(messages);
        }

        this.elapsedLost = 0;

        this.elapsedControlsText = 0;

        this.wonElapsed = 0;
    }

    @Override
    public void create() {

    }

    private OrthographicCamera camera;

    @Override
    public void render(SpriteBatch batch) {
        this.map.render(batch, this.camera);
        this.miniMap.render(batch, this.camera);

        if(!this.map.isLost()) {
            //idk, it works
            this.messageRenderer.renderSmall(batch, "Base (" + this.map.getHouseHealth() + "%)", new Vector2(Gdx.graphics.getWidth() / 4 + 845 - camera.position.x * 2, Gdx.graphics.getHeight() / 4 + 800 - camera.position.y * 2));

            if(!this.messageRenderer.isRunning() && this.elapsedControlsText <= 5) {
                messageRenderer.renderMedium(batch, "(WASD or Arrows to move view)", new Vector2(Gdx.graphics.getWidth() / 2 - 154, Gdx.graphics.getHeight() / 2 + 160));
            }
        } else {
            this.messageRenderer.renderLarge(batch, "Base destroyed! Restarting...", new Vector2(Gdx.graphics.getWidth() / 2 - 174, Gdx.graphics.getHeight() / 2 + 32));
        }

        if(this.map.hasWon()) {
            this.messageRenderer.renderLarge(batch, "You win. Congrats! Restarting...", new Vector2(Gdx.graphics.getWidth() / 2 - 174, Gdx.graphics.getHeight() / 2 + 32));
            this.messageRenderer.renderLarge(batch, "(Press ESC to exit the game)", new Vector2(Gdx.graphics.getWidth() / 2 - 174, Gdx.graphics.getHeight() / 2));

            this.wonElapsed += 1 * Gdx.graphics.getDeltaTime();

            if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                System.exit(0);
            }
        }

        if(!this.paused) {
            batch.end();
            this.messageRenderer.render();
            batch.begin();
        } else {
            this.messageRenderer.renderLarge(batch, "Paused (P)", new Vector2(10, Gdx.graphics.getHeight() - 100));
        }
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.camera = camera;

        if(!this.paused) {
            this.messageRenderer.update(camera);

            if(this.wonElapsed >= 8) {
                this.reset(true);
            }

            if (!this.messageRenderer.isRunning()) {
                this.map.update(camera);

                this.miniMap.update(camera);

                this.camera = camera;

                this.elapsedControlsText += 1 * Gdx.graphics.getDeltaTime();
            }
            /**for(TileNode node : this.map.getAllNodes()) {
             if(!node.hasTower()) {
             Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
             camera.unproject(mouse);

             Rectangle mouseBody = new Rectangle(mouse.x, mouse.y, 0, 0);

             if (mouseBody.overlaps(new Rectangle(node.getPosition().x, node.getPosition().y, 16, 16))) {
             if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
             node.setTower(new EntityTower(this.map, "testtower.png", new Vector2(node.getPosition().x, node.getPosition().y)));
             }
             }
             }
             }**/

            if (!this.messageRenderer.isRunning()) {
                float speed = 100 * Gdx.graphics.getDeltaTime();

                if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    camera.position.add(speed, 0, 0);
                    camera.update();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    camera.position.add(-speed, 0, 0);
                    camera.update();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    camera.position.add(0, speed, 0);
                    camera.update();
                }
                if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    camera.position.add(0, -speed, 0);

                    camera.update();
                }

                if (camera.position.y < 157) {
                    camera.position.set(camera.position.x, 157, 0);
                }

                if (camera.position.y > 387) {
                    camera.position.set(camera.position.x, 387, 0);
                }

                if (camera.position.x < 201) {
                    camera.position.set(201, camera.position.y, 0);
                }

                if (camera.position.x > 502) {
                    camera.position.set(502, camera.position.y, 0);
                }

                camera.update();

                if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    this.paused = true;
                }
            }

            if(!this.map.isLost()) {
                if(this.map.getHouseHealth() <= 0) {
                    this.map.spawnEntity(new EntityParticle(this.map, "endexplode", 150, new Vector2(350, 300)));
                    this.map.setLost(true);

                    for(MapLayer layer : this.map.getLayers()) {
                        int tileIndex = 0;
                        for(TileType type : layer.getTiles()) {
                            if(type.name().toLowerCase().contains("building")) {
                                layer.getTiles().set(tileIndex, TileType.Broke);
                            }
                            tileIndex++;
                        }
                    }
                }
            } else {
                this.elapsedLost += 1 * Gdx.graphics.getDeltaTime();

                if(this.elapsedLost >= 5) {
                    this.reset(true);
                }
            }
        } else {
            if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                this.paused = false;
            }
        }

        //System.out.println("Pos " + camera.position.x + "/" + camera.position.y);

        /**if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            this.reset(true);
        }**/

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(!this.messageRenderer.isRunning()) {
                System.out.println("User quit");
                System.exit(0);
            }
        }
    }

}
