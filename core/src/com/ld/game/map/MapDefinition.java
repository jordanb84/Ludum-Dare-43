package com.ld.game.map;

public class MapDefinition {

    private int mapWidth;
    private int mapHeight;

    private int tileWidth;
    private int tileHeight;

    private int layers;

    public MapDefinition(int mapWidth, int mapHeight, int tileWidth, int tileHeight, int layers) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.layers = layers;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getLayers() {
        return layers;
    }
}
