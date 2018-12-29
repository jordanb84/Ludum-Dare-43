package com.ld.game.entity.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.entity.Direction;
import com.ld.game.entity.Entity;
import com.ld.game.entity.EntityEnemy;
import com.ld.game.map.Map;
import com.ld.game.tile.TileGraph;
import com.ld.game.tile.TileNode;
import com.ld.game.tile.TileType;

public class EntityTest extends EntityEnemy {

    private TileGraph tileGraph;

    private DefaultGraphPath<TileNode> graphPath = new DefaultGraphPath<TileNode>();

    private int tile = 0;

    private Sprite destSprite;

    private ShapeRenderer shapeRenderer;

    public EntityTest(Map map, TileType startType, TileType endType, String spriteName, float movementVelocity) {
        super(map, spriteName, new Vector2(), movementVelocity, 2);

        this.tileGraph = this.getMap().generateUpdatedTileGraph();

        Vector2 startPosition = this.getMap().getNodeForType(startType).getPosition();
        this.setPosition(new Vector2(startPosition.x, startPosition.y));

        this.getMap().generatePath(startType, endType, this.graphPath);

        //System.out.println("Path distance boi: " + this.graphPath.nodes.size);

        this.destSprite = new Sprite(new Texture(Gdx.files.internal("path.png")));

        this.shapeRenderer = new ShapeRenderer();
        this.shapeRenderer.setAutoShapeType(true);
    }

    private TileNode destNode;

    private TileNode finalNode;

    private Rectangle finalRect = new Rectangle();

    @Override
    public void update(OrthographicCamera camera) {
        super.update(camera);
        /**if(Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            this.tile++;
            Vector2 position = this.graphPath.get(this.tile).getPosition();

            this.setPosition(position);
        }**/
        this.destNode = this.graphPath.get(this.tile);

        boolean currentTileDestReached = this.moveToward(destNode.getPosition());

        if(currentTileDestReached) {
            if(!(this.tile >= this.graphPath.nodes.size-1)) {
                this.tile++;
            }
        }

        //System.out.println(this.tile + "/" + this.graphPath.nodes.size);

        this.finalNode = this.graphPath.nodes.get(this.graphPath.nodes.size - 1);
        this.finalRect.set(this.finalNode.getPosition().x, this.finalNode.getPosition().y, this.getMap().getMapDefinition().getTileWidth(), this.getMap().getMapDefinition().getTileHeight());

        if(this.getBody().overlaps(this.finalRect) && !(this.getMap().isLost())) {
            this.getMap().removeEntity(this);
            this.getMap().damage();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        this.getSprite().setPosition(this.getX(), this.getY());
        this.getSprite().setAlpha(this.getHealthPercent());
        this.getSprite().draw(batch);

        //this.destSprite.setPosition(this.destNode.getPosition().x, this.destNode.getPosition().y);
        //this.destSprite.draw(batch);

        /**batch.end();
        this.shapeRenderer.begin();
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.shapeRenderer.setColor(Color.YELLOW);
        Rectangle destRectangle = new Rectangle(this.destNode.getPosition().x + this.getWidth() / 2 + this.getWidth() / 4, this.destNode.getPosition().y - this.getWidth() / 4, this.getWidth() / 4, this.getWidth() / 2);

        this.shapeRenderer.rect(destRectangle.x, destRectangle.y, destRectangle.width, destRectangle.height);

        //why aren't they immediately going to a new dest once they intersect? I think my renderings
        //here are slightly incorrect from the actual thing: as defined in Entity#moveToward
        this.shapeRenderer.end();
        batch.begin();**/
    }

    @Override
    public float getMaxHealth() {
        return 3;
    }
}
