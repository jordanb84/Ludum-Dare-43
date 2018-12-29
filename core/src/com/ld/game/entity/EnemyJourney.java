package com.ld.game.entity;

import com.ld.game.tile.TileNode;
import com.ld.game.tile.TileType;

public class EnemyJourney {

    private TileNode startNode;

    private TileNode endNode;

    public EnemyJourney(TileNode startNode, TileNode endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public TileNode getStartNode() {
        return startNode;
    }

    public TileNode getEndNode() {
        return endNode;
    }

    public TileType getStartType() {
        return this.getStartNode().getTileType();
    }

    public TileType getEndType() {
        return this.getEndNode().getTileType();
    }
}
