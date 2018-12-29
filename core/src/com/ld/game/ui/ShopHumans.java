package com.ld.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld.game.assets.Assets;
import com.ld.game.map.Map;
import com.ld.game.tile.TileNode;
import com.ld.game.ui.impl.ShopCellEmpty;
import com.ld.game.ui.impl.ShopCellHuman;
import com.ld.game.ui.impl.ShopCellTestTower;

import java.util.ArrayList;
import java.util.List;

public class ShopHumans {

    private List<ShopCell> cells = new ArrayList<ShopCell>();

    private Vector2 position = new Vector2(-24 * 7 / 2, Gdx.graphics.getHeight() / 4 - 24);

    private int cellWidth = 24; //maybe make em 24x24 and just put 16x16 textures inside
    private int cellHeight = 24;

    private Map map;

    private Sprite cellSprite;

    private Sprite hoverCellSprite;

    private ShopCell selectedCell;

    private boolean buying;

    private float timeSincePlacing;

    private boolean hoveringShop;

    private boolean release = true;

    private Sprite rangeSprite;

    public ShopHumans(Map map) {
        this.setup(map);
    }

    private Rectangle mouseBody = new Rectangle();

    private OrthographicCamera camera;

    public void setup(Map map) {
        this.cells.add(new ShopCellHuman("humanicon.png"));
        this.cells.add(new ShopCellHuman("humanicon1.png"));
        this.cells.add(new ShopCellHuman("humanicon2.png"));
        this.cells.add(new ShopCellHuman("humanicon2.png"));
        this.cells.add(new ShopCellHuman("humanicon1.png"));
        this.cells.add(new ShopCellHuman("humanicon.png"));
        this.cells.add(new ShopCellHuman("humanicon2.png"));

        this.map = map;

        this.cellSprite = new Sprite(Assets.getInstance().getSprite("inventorycell.png"));
        this.hoverCellSprite = new Sprite(Assets.getInstance().getSprite("hovercell.png"));

        float rangeWidth = map.getMapDefinition().getTileWidth() * 5;
        float rangeHeight = map.getMapDefinition().getTileHeight() * 5;

        this.rangeSprite = new Sprite(Assets.getInstance().getSprite("enemypath.png"));
        this.rangeSprite.setSize(rangeWidth, rangeHeight);
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.hoveringShop = false;

        int cells = 0;
        for(ShopCell cell : this.cells) {
            Vector2 cellPosition = new Vector2(this.position.x + cells * cellWidth, this.position.y);

            cellPosition.add(camera.position.x, camera.position.y);

            if(!(cell instanceof ShopCellEmpty)) {
                this.cellSprite.setPosition(cellPosition.x, cellPosition.y);
                this.cellSprite.draw(batch);
            }

            if(mouseBody.overlaps(new Rectangle(cellPosition.x, cellPosition.y, this.cellWidth, this.cellHeight))) {
                this.hoveringShop = true;

                this.hoverCellSprite.setPosition(cellPosition.x, cellPosition.y);
                this.hoverCellSprite.draw(batch);

                if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    if(!(cell instanceof ShopCellEmpty)) {
                        if(!(cell.isUsed())) {
                            this.selectedCell = cell;
                            this.buying = true;
                        }
                    }
                }
            }

            cell.render(batch, cellPosition);
            cells++;

        }

        if(this.buying) {
            Vector2 cellPos = new Vector2(this.mouseBody.x, this.mouseBody.y);
            this.selectedCell.render(batch, new Vector2(cellPos.x - this.map.getMapDefinition().getTileWidth(), cellPos.y - this.map.getMapDefinition().getTileHeight()));

            this.rangeSprite.setPosition(cellPos.x - this.map.getMapDefinition().getTileWidth() * 2.5f, cellPos.y - this.map.getMapDefinition().getTileHeight() * 2.5f);
            this.rangeSprite.draw(batch);
        }
    }

    public void update(OrthographicCamera camera) {
        this.camera = camera;

        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);

        this.mouseBody.set(mouse.x, mouse.y, 0, 0);

        this.timeSincePlacing += 1 * Gdx.graphics.getDeltaTime();

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (this.buying && !this.hoveringShop && this.release) {
                this.release = false;

                Vector2 clickPosition = new Vector2(this.mouseBody.x, this.mouseBody.y);

                TileNode node = map.getNodeAtPosition(clickPosition);
                if(node != null) {
                    selectedCell.onPurchased(clickPosition, this.map, node);
                        this.buying = false;
                        this.selectedCell = null;
                }
            }
        }

        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            this.release = true;
        }

        //Gdx.graphics.setTitle("Cash: " + this.balance);
    }

    public List<ShopCell> getCells() {
        return cells;
    }
}