package com.ld.game.state.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld.game.assets.Assets;
import com.ld.game.io.MapExporter;
import com.ld.game.io.MapImporter;
import com.ld.game.map.Map;
import com.ld.game.map.MapDefinition;
import com.ld.game.map.MapLayer;
import com.ld.game.state.State;
import com.ld.game.state.StateManager;
import com.ld.game.tile.TileType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class StateEditor extends State {

    private TileType placeType = TileType.Wall;

    private Map map;

    private MapDefinition mapDefinition;

    private int layer = 0;
    private int maxLayers;

    private Sprite hoverSprite;

    private boolean hovering;

    private Vector2 hoverPosition = new Vector2();

    private MapExporter mapExporter;

    private boolean selecting;

    private Rectangle mouseBody = new Rectangle();

    public StateEditor(StateManager stateManager) {
        super(stateManager);

        //MapDefinition mapDefinition = new MapDefinition(32, 26, 16, 16, 3);
        MapDefinition mapDefinition = new MapDefinition(44, 34, 16, 16, 3);

        this.mapDefinition = mapDefinition;

        this.maxLayers = this.mapDefinition.getLayers() - 1;

        this.map = this.generateMap(TileType.Ground, mapDefinition);

        this.hoverSprite = new Sprite(Assets.getInstance().getSprite("hover.png"));

        this.mapExporter = new MapExporter();
    }

    @Override
    public void create() {

    }

    private OrthographicCamera camera;

    @Override
    public void render(SpriteBatch batch) {
        this.map.render(batch, this.camera);

        if(this.hovering) {
            this.hoverSprite.setPosition(this.hoverPosition.x, this.hoverPosition.y);
            this.hoverSprite.draw(batch);
        }

        if(!this.selecting) {

        } else {
            int row = 0;
            int tile = 0;

            for(int tileOption = 0; tileOption < TileType.values().length; tileOption++) {
                Vector2 position = new Vector2(tile * this.mapDefinition.getTileWidth() - 100, (row * this.mapDefinition.getTileHeight()) + Gdx.graphics.getHeight() / 4);
                TileType tileType = TileType.values()[tileOption];

                tileType.SPRITE.setPosition(position.x, position.y);
                tileType.SPRITE.draw(batch);

                if(this.mouseBody.overlaps(new Rectangle(position.x, position.y, this.mapDefinition.getTileWidth(), this.mapDefinition.getTileHeight()))) {
                    System.out.println("Type: " + tileType.name() + " ID: " + tileType.ordinal());
                    if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        this.selecting = false;
                        this.placeType = tileType;
                    }
                }

                tile++;

                if(tile >= 5) {
                    tile = 0;
                    row++;
                }
            }
        }
    }

    @Override
    public void update(OrthographicCamera camera) {
        this.camera = camera;

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        this.mouseBody.set(mouse.x, mouse.y, 0, 0);

        if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            this.selecting = !this.selecting;
        }

        int speed = 1;

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.position.add(speed, 0, 0);
            camera.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.position.add(-speed, 0, 0);
            camera.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.position.add(0, speed, 0);
            camera.update();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.position.add(0, -speed, 0);
            camera.update();
        }

        if(!this.selecting) {
            //this.map.update(camera);

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                //this.layer = layer > 0 ? layer-- : 0;
                this.layer--;

                if(this.layer < 0) {
                    this.layer = 0;
                }
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                //this.layer = layer > this.maxLayers ? this.maxLayers : layer++;
                this.layer++;

                if(this.layer >= this.maxLayers) {
                    this.layer = this.maxLayers;
                }
            }

            Gdx.graphics.setTitle("Layer " + this.layer);

            this.checkTileHover(camera);

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                JFileChooser chooser = new JFileChooser();

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    MapExporter.exportMap(this.map, Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath()));
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
                JFileChooser chooser = new JFileChooser();

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    this.map = MapImporter.importMap(Gdx.files.absolute(chooser.getSelectedFile().getAbsolutePath()), this.mapDefinition, false);
                }
            }
        } else {

        }
    }

    public void checkTileHover(OrthographicCamera camera) {
        this.hovering = false;

        List<TileType> layerTiles = this.map.getLayers().get(this.layer).getTiles();

        int totalTiles = 0;
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int tile = 0; tile < this.mapDefinition.getMapWidth(); tile++) {
                Rectangle tileBody = new Rectangle(tile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight(), this.mapDefinition.getTileWidth(), this.mapDefinition.getTileHeight());

                if(this.mouseBody.overlaps(tileBody)) {
                    this.hovering = true;
                    this.hoverPosition = new Vector2(tileBody.x, tileBody.y);

                    if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        layerTiles.set(totalTiles, this.placeType);
                    }
                }

                totalTiles++;
            }
        }
    }

    public Map generateMap(TileType groundType, MapDefinition mapDefinition) {
        List<MapLayer> layers = new ArrayList<MapLayer>();
        //System.out.println("setup based on " + mapDefinition.getLayers() + " layers");
        for(int layerIndex = 0; layerIndex < mapDefinition.getLayers(); layerIndex++) {
            List<TileType> layerTiles = new ArrayList<TileType>();

            for(int row = 0; row < mapDefinition.getMapHeight(); row++) {
                for(int tile = 0; tile < mapDefinition.getMapWidth(); tile++) {
                    if(layerIndex == 0) {
                        layerTiles.add(groundType);
                    } else {
                        layerTiles.add(TileType.Air);
                    }
                }
            }

            layers.add(new MapLayer(layerTiles, mapDefinition));
        }

        return new Map(layers, mapDefinition, false);
    }

    //you're pulling us apart, zenneth...

}
