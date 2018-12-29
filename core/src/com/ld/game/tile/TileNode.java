package com.ld.game.tile;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ld.game.entity.impl.EntityTower;

public class TileNode {

    private int tileMapIndex;

    private boolean solid;

    private Array<Connection<TileNode>> connections = new Array<Connection<TileNode>>();

    private Vector2 position;

    private TileType tileType;

    private int layer;

    private EntityTower tower;

    public TileNode(int tileMapIndex, boolean solid, Vector2 position, TileType tileType, int layer) {
        this.tileMapIndex = tileMapIndex;
        this.solid = solid;
        this.position = position;
        this.tileType = tileType;
        this.layer = layer;
    }

    public void addAdjacentTile(TileNode node) {
        if(node != null) {
            if(node.isSolid()) {
                this.connections.add(new SolidConnection<TileNode>(this, node));
            } else {
                this.connections.add(new DefaultConnection<TileNode>(this, node));
            }
        }
    }

    public Array<Connection<TileNode>> getConnections() {
        return connections;
    }

    public boolean isSolid() {
        return solid;
    }

    public int getTileMapIndex() {
        return tileMapIndex;
    }

    public Vector2 getPosition() {
        return position;
    }

    public TileType getTileType() {
        return tileType;
    }

    public int getLayer() {
        return layer;
    }

    public EntityTower getTower() {
        return tower;
    }

    public boolean hasTower() {
        return this.getTower() != null;
    }

    public void setTower(EntityTower tower) {
        this.tower = tower;
        tower.getMap().spawnEntity(tower);
    }
}
