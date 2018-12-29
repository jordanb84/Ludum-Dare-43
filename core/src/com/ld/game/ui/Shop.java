package com.ld.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ld.game.assets.Assets;
import com.ld.game.map.Map;
import com.ld.game.tile.TileNode;
import com.ld.game.ui.impl.*;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    private int balance = 20;

    private List<ShopCell> cells = new ArrayList<ShopCell>();

    private Vector2 position = new Vector2(Gdx.graphics.getWidth() / 4 - 48, Gdx.graphics.getHeight() / 4 - 48 * 3 - 24);

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

    public Shop(Map map) {
        this.setup(map);
    }

    private Rectangle mouseBody = new Rectangle();

    private OrthographicCamera camera;

    private OrthographicCamera customCamera;
    private SpriteBatch customBatch;

    private BitmapFont font;

    public void setup(Map map) {
        this.cells.add(new ShopCellEmpty());
        this.cells.add(new ShopCellEmpty());
        this.cells.add(new ShopCellEmpty());
        this.cells.add(new ShopCellEmpty());
        this.cells.add(new ShopCellEmpty());
        //this.cells.add(new ShopCellTowerFour());
        //this.cells.add(new ShopCellTowerThree());
        //this.cells.add(new ShopCellTestTower());

        this.addTower(new ShopCellTestTower());
        //this.addTower(new ShopCellTowerTwo());
        this.map = map;

        this.cellSprite = new Sprite(Assets.getInstance().getSprite("inventorycell.png"));
        this.hoverCellSprite = new Sprite(Assets.getInstance().getSprite("hovercell.png"));

        float rangeWidth = map.getMapDefinition().getTileWidth() * 5;
        float rangeHeight = map.getMapDefinition().getTileHeight() * 5;

        this.rangeSprite = new Sprite(Assets.getInstance().getSprite("enemypath.png"));
        this.rangeSprite.setSize(rangeWidth, rangeHeight);

        this.customCamera = new OrthographicCamera();
        this.customBatch = new SpriteBatch();

        this.customCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.font = new BitmapFont(Gdx.files.internal("font8.fnt"));
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        this.hoveringShop = false;

        batch.end();
        this.customBatch.begin();
        this.font.draw(this.customBatch, "Shop", Gdx.graphics.getWidth() - 92, 288);
        this.customBatch.end();
        batch.begin();

        int cells = 0;
        for(ShopCell cell : this.cells) {
            Vector2 cellPosition = new Vector2(this.position.x, this.position.y + cells * this.cellHeight);

            cellPosition.add(camera.position.x, camera.position.y);

            if(!(cell instanceof ShopCellEmpty)) {
                this.cellSprite.setPosition(cellPosition.x, cellPosition.y);
                this.cellSprite.draw(batch);
            }

            if(mouseBody.overlaps(new Rectangle(cellPosition.x, cellPosition.y, this.cellWidth, this.cellHeight))) {
                this.hoveringShop = true;

                this.hoverCellSprite.setPosition(cellPosition.x, cellPosition.y);
                this.hoverCellSprite.draw(batch);

                batch.end();
                this.customBatch.begin();
                Vector2 pos = new Vector2(Gdx.graphics.getWidth() - 176, 330);
                this.font.draw(this.customBatch, "Price " + cell.getPrice(), pos.x, pos.y);
                this.font.draw(this.customBatch, "Damage " + cell.getDamage(), pos.x, pos.y - 24);
                this.customBatch.end();
                batch.begin();

                if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && this.balance >= cell.getPrice()) {
                    if(!(cell instanceof ShopCellEmpty)) {
                        this.selectedCell = cell;
                        this.buying = true;
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

                    this.balance -= selectedCell.getPrice();

                    if(this.balance < selectedCell.getPrice()) {
                        this.buying = false;
                        this.selectedCell = null;
                    }
                }
            }
        }

        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            this.buying = false;
            this.selectedCell = null;
        }

        if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            this.release = true;
        }

        //Gdx.graphics.setTitle("Cash: " + this.balance);
    }

    public void addFunds(int amount) {
        this.balance += amount;
    }

    public List<ShopCell> getCells() {
        return cells;
    }

    public void addTower(ShopCell shopCell) {
        int totalCells = 0;
        for(ShopCell cell : this.cells) {
            if(cell instanceof ShopCellEmpty) {
                this.cells.set(totalCells, shopCell);
                break;
            }
            totalCells++;
        }
    }

    public int getBalance() {
        return balance;
    }
}