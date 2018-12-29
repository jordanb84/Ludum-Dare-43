package com.ld.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.ld.game.assets.Assets;
import com.ld.game.entity.EnemyJourney;
import com.ld.game.entity.Entity;
import com.ld.game.entity.impl.EntityTest;
import com.ld.game.entity.impl.EntityTower;
import com.ld.game.particle.EntityParticle;
import com.ld.game.tile.*;
import com.ld.game.ui.MessageRenderer;
import com.ld.game.ui.Shop;
import com.ld.game.ui.ShopHumans;
import com.ld.game.wave.EnemyWaves;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {

    private List<MapLayer> layers = new ArrayList<MapLayer>();

    private MapDefinition mapDefinition;

    private List<Entity> entities = new ArrayList<Entity>();

    private List<Entity> entitySpawnQueue = new ArrayList<Entity>();
    private List<Entity> entityDespawnQueue = new ArrayList<Entity>();

    private IndexedAStarPathFinder pathFinder;

    private TileNode[][] collisionLayerTiles;

    private TileGraph tileGraph;

    private DefaultGraphPath<TileNode> graphPath;

    private ManhattanHeuristic heuristic = new ManhattanHeuristic();

    private Array<TileNode> testPathTiles;

    private boolean drawTestPath;

    private Sprite pathSprite;

    //TODO Render the connections for tiles when you hover them

    private List<TileNode> adjacentNodes = new ArrayList<TileNode>();

    private Array<TileNode> allNodes = new Array<TileNode>();

    private Shop shop;

    private List<EnemyJourney> enemyJourneys = new ArrayList<EnemyJourney>();

    private EnemyWaves enemyWaves;

    private boolean generateEnemyJourneys;

    private ShopHumans shopHumans;

    private int houseHealth = 100;

    private boolean lost;

    private boolean centered;

    private boolean won;

    private float wonElapsed;

    private MessageRenderer messageRenderer;

    private boolean madeSacrifice;

    public Map(List<MapLayer> layers, MapDefinition mapDefinition, boolean generateEnemyJourneys) {
        this.layers = layers;
        this.mapDefinition = mapDefinition;

        //this.generatePathfindingMap();

        this.pathSprite = new Sprite(Assets.getInstance().getSprite("path.png"));

        this.graphPath = new DefaultGraphPath<TileNode>();

        //this.spawnEntity(new EntityParticle(this, "testparticle", 15, new Vector2(128, 128)));

        /**TileNode startNode = null;
        TileNode endNode = null;

        for(TileNode node : this.getAllNodes()) {
            if(node.getTileType() == TileType.Start) {
                startNode = node;
            }

            if(node.getTileType() == TileType.End) {
                endNode = node;
            }
        }**/

        this.pathFinder = this.generatePathfindingMap();

        this.shop = new Shop(this);

        if(generateEnemyJourneys) {
            this.addEnemyJourney(TileType.Start, TileType.End);
            this.addEnemyJourney(TileType.Start1, TileType.End);
            this.addEnemyJourney(TileType.Start2, TileType.End);
            //this.addEnemyJourney(TileType.Start1, TileType.End1);
            //this.addEnemyJourney(TileType.Start2, TileType.End2);

            this.enemyWaves = new EnemyWaves(this);
        }

        this.generateEnemyJourneys = generateEnemyJourneys;

        this.shopHumans = new ShopHumans(this);

        //this.generatePath(TileType.Start, TileType.End, this.graphPath);
    }

    public IndexedAStarPathFinder generatePathfindingMap() {
        this.collisionLayerTiles = new TileNode[this.mapDefinition.getMapHeight()][this.mapDefinition.getMapWidth()];

        Array<TileNode> allNodes = new Array<TileNode>();

        List<TileType> baseNodes = this.getLayers().get(1).getTiles();

        int totalTiles = 0;
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int tile = 0; tile < this.mapDefinition.getMapWidth(); tile++) {
                int x = tile;
                int y = row;

                TileType type = baseNodes.get(totalTiles);

                //System.out.println("Made for type " + type.name() + " with SOLID set to " + type.SOLID);

                TileNode node = new TileNode(totalTiles, type.SOLID, new Vector2(tile * this.mapDefinition.getTileWidth(), row * this.mapDefinition.getTileHeight()), type, 1);
                //System.out.println("Solid: " + node.isSolid());
                allNodes.add(node);

                this.collisionLayerTiles[y][x] = node;

                totalTiles++;
            }
        }

        //this.tileGraph = new TileGraph(allNodes);
        this.allNodes = allNodes;
        this.tileGraph = this.generateUpdatedTileGraph();

        System.out.println("------Start initial tileGraph values-------\n");
        for(TileNode node : this.tileGraph.getNodes()) {
            System.out.print(node.getTileType().name() + ",");
        }
        System.out.println("\n------End initial tileGraph values-------");

        int totalAdjacentCheckedTiles = 0;
        for(int row = 0; row < this.mapDefinition.getMapHeight(); row++) {
            for(int tile = 0; tile < this.mapDefinition.getMapWidth(); tile++) {
                int x = tile;
                int y = row;

                TileNode node = this.collisionLayerTiles[y][x];

                if(node != null) {
                    /**node.addAdjacentTile(this.collisionLayerTiles[y - 1][x]);
                    node.addAdjacentTile(this.collisionLayerTiles[y + 1][x]);
                    node.addAdjacentTile(this.collisionLayerTiles[y][x - 1]);
                    node.addAdjacentTile(this.collisionLayerTiles[y][x + 1]);**/

                    this.addAdjacentNode(node, x, y - 1);
                    this.addAdjacentNode(node, x, y + 1);
                    this.addAdjacentNode(node, x - 1, y);
                    this.addAdjacentNode(node, x + 1, y);
                }
            }
        }

        return new IndexedAStarPathFinder(this.tileGraph, true);
    }

    public void addAdjacentNode(TileNode rootNode, int x, int y) {
        if(x >= 0 && x < this.mapDefinition.getMapWidth() && y >= 0 && y < this.mapDefinition.getMapHeight()) {
            //System.out.println("added adjacent for node " + rootNode.getTileMapIndex());
            rootNode.addAdjacentTile(this.collisionLayerTiles[y][x]);
        }
    }

    public void generatePath(TileType startType, TileType endType, DefaultGraphPath<TileNode> graphPath) {
        /**TileNode startNode = this.tileGraph.getNodes().get(15);
        TileNode endNode = this.tileGraph.getNodes().get(456);

        for(TileNode node : this.tileGraph.getNodes()) {
            if(node.getTileType() == TileType.Start) {
                startNode = node;
            }

            if(node.getTileType() == TileType.End) {
                endNode = node;
            }
        }**/

        TileNode startNode = null;
        TileNode endNode = null;
        //System.out.println("searching for start type " + startType.name() + " end type " + endType.name());
        for(TileNode node : this.tileGraph.getNodes()) {
            if(node.getTileType() == startType) {
                startNode = node;
                //System.out.println("Found start type");
            }

            if(node.getTileType() == endType) {
                endNode = node;
                //System.out.println("Found end type");
            }

            //System.out.print(node.getTileType().name() + ",");
        }

        //System.out.println("Start node: " + startNode + " end node: " + endNode + " Graph size: " + this.tileGraph.getNodes().size);


        //this.graphPath.clear();
        graphPath.clear();

        this.pathFinder.searchNodePath(startNode, endNode, heuristic, graphPath);

        if(this.graphPath.nodes.size > 0) {
            this.drawTestPath = true;
            this.testPathTiles = this.graphPath.nodes;
        }

        //System.out.println("Path nodes: " + this.graphPath.nodes.size);
    }

    private OrthographicCamera camera;

    public void render(SpriteBatch batch, OrthographicCamera camera) {
       // System.out.println("rendering " + this.layers.size());
        for(MapLayer layer : this.layers) {
            layer.render(batch);
        }

        for(Entity entity : this.getEntities()) {
            entity.render(batch);
        }

        for(Entity entity : this.getEntities()) {
            if(entity instanceof EntityTower) {
                EntityTower tower = ((EntityTower) entity);

                tower.renderText(batch);
            }
        }

        if(this.drawTestPath) {
            for(TileNode node : this.testPathTiles) {
                this.pathSprite.setPosition(node.getPosition().x, node.getPosition().y);
                this.pathSprite.draw(batch);
            }
        }

        if(this.generateEnemyJourneys) {
            this.enemyWaves.render(batch, this.camera);
        }

        this.shop.render(batch, camera);
        this.shopHumans.render(batch, camera);

        /**for(TileNode node : this.adjacentNodes) {
            this.pathSprite.setPosition(node.getPosition().x, node.getPosition().y);
            this.pathSprite.draw(batch);
        }**/
    }

    private float elapsed;

    private boolean spawnedWinParticles;

    public void update(OrthographicCamera camera) {
        this.camera = camera;

        if(this.won) {
            this.wonElapsed += 1 * Gdx.graphics.getDeltaTime();

            if(!this.spawnedWinParticles) {
                for(int i = 0; i < 3; i++) {
                    Vector2 position = new Vector2(100 + 200 * i, 100);

                    EntityParticle particle = new EntityParticle(this, "hearts3", 3, position);
                    particle.scale(0.5f);
                    this.spawnEntity(particle);
                }
                this.spawnedWinParticles = true;
            }
        }

        if(!this.centered) {
            camera.position.set(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 16, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 16, 0);
            camera.update();
            this.centered = true;
        }

        for(MapLayer layer : this.layers) {
            layer.update(camera);
        }

        this.shop.update(camera);
        this.shopHumans.update(camera);

        this.entities.removeAll(this.entityDespawnQueue);
        this.entities.addAll(this.entitySpawnQueue);

        this.entityDespawnQueue.clear();
        this.entitySpawnQueue.clear();

        for(Entity entity : this.getEntities()) {
            entity.update(camera);
        }

        /**this.elapsed += 1 * Gdx.graphics.getDeltaTime();

        if(this.elapsed >= 3) {
            this.elapsed = 0;
            this.spawnEntity(new EntityTest(this, TileType.Start, TileType.End));
        }**/

        this.adjacentNodes.clear();

        //System.out.println("Nodes in graph: " + this.tileGraph.getNodeCount());
        for(TileNode node : this.tileGraph.getNodes()) {
            Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mouse);

            Rectangle mouseBody = new Rectangle(mouse.x, mouse.y, 0, 0);

            if(mouseBody.overlaps(new Rectangle(node.getPosition().x, node.getPosition().y, 16, 16))) {
                String positions = ("");

                for(Connection<TileNode> connection : node.getConnections()) {
                    this.adjacentNodes.add(connection.getToNode());
                    positions += (",X" + connection.getToNode().getPosition().x + ",Y" + connection.getToNode().getPosition().y);

                }

                //System.out.println("Found " + node.getConnections().size + " connections for node. Positions: " + positions);
            }
        }

        if(this.generateEnemyJourneys) {
            this.enemyWaves.update(camera);
        }
    }

    public void spawnEntity(Entity entity) {
        this.entitySpawnQueue.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entityDespawnQueue.add(entity);
    }

    public List<MapLayer> getLayers() {
        return layers;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public TileGraph generateUpdatedTileGraph() {
        return new TileGraph(this.allNodes);
    }

    public TileGraph getUpdatedTileGraph() {
        return this.tileGraph;
    }

    public Array<TileNode> getAllNodes() {
        return allNodes;
    }

    public IndexedAStarPathFinder getPathFinder() {
        return this.pathFinder;
    }

    public TileNode getNodeForType(TileType type) {
        for(TileNode node : this.getAllNodes()) {
            if(node.getTileType() == type) {
                return node;
            }
        }

        return null;
    }

    public MapDefinition getMapDefinition() {
        return mapDefinition;
    }

    public TileNode getNodeAtPosition(Vector2 position) {
        for(TileNode tileNode : this.allNodes) {
            if(new Rectangle(position.x, position.y, 0, 0).overlaps(new Rectangle(tileNode.getPosition().x, tileNode.getPosition().y, this.mapDefinition.getTileWidth(), this.mapDefinition.getTileHeight()))) {
                return tileNode;
            }
        }

        return null;
    }

    public Shop getShop() {
        return shop;
    }

    public void addEnemyJourney(TileType startType, TileType endType) {
        TileNode startNode = this.getNodeForType(startType);
        TileNode endNode = this.getNodeForType(endType);

        this.enemyJourneys.add(new EnemyJourney(startNode, endNode));
    }

    public EnemyJourney getRandomJourney() {
        System.out.println("Journeys available: " + this.enemyJourneys.size());
        return this.enemyJourneys.get(new Random().nextInt(this.enemyJourneys.size() - 1));
    }

    public int getHouseHealth() {
        return houseHealth;
    }

    public void setHouseHealth(int houseHealth) {
        this.houseHealth = houseHealth;
    }

    public void damage() {
        this.houseHealth = this.houseHealth - (new Random().nextInt((13 - 8)) + 8);
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public void win() {
        this.won = true;
    }

    public boolean hasWon() {
        return this.won;
    }

    public float getWonElapsed() {
        return wonElapsed;
    }

    public void setMessageRenderer(MessageRenderer messageRenderer) {
        this.messageRenderer = messageRenderer;
    }

    public MessageRenderer getMessageRenderer() {
        return this.messageRenderer;
    }

    public boolean hasMadeSacrifice() {
        return this.madeSacrifice;
    }

    public void makeSacrifice() {
        this.madeSacrifice = true;
    }
}
