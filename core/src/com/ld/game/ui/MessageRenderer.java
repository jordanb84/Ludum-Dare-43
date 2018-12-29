package com.ld.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ld.game.assets.Assets;

import java.util.ArrayList;
import java.util.List;

public class MessageRenderer {

    private SpriteBatch customBatch;
    private OrthographicCamera customCamera;

    private Sprite messageBackground;

    private String[] messages;
    private boolean rendering;

    private int messageIndex = 0;

    private float elapsedSinceNextChar;
    private int renderChars = 0;

    private BitmapFont font;
    private BitmapFont fontLarge;
    private BitmapFont fontSmall;
    private BitmapFont fontMedium;

    private boolean messageItemComplete;

    private boolean messageComplete;

    private boolean waitingForNextMessageItem;
    private float elapsedSinceItemWait;

    private boolean waitComplete;

    private int maxLineChars = 88;

    private boolean running;

    public MessageRenderer() {
        this.messageBackground = new Sprite(Assets.getInstance().getSprite("message.png"));

        this.customBatch = new SpriteBatch();
        this.customCamera = new OrthographicCamera();
        this.customCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.font = new BitmapFont(Gdx.files.internal("font10.fnt"));
        this.fontLarge = new BitmapFont(Gdx.files.internal("font12.fnt"));
        this.fontSmall = new BitmapFont(Gdx.files.internal("font8.fnt"));
    }

    public void render() {
        if(!this.messageComplete) {
            if (this.rendering) {
                this.customBatch.begin();

                this.messageBackground.draw(this.customBatch);

                List<String> messageContentAvailable = new ArrayList<String>();
                messageContentAvailable.add("");

                int line = 0;

                int lineChars = 0;

                String currentMessage = this.getCurrentMessage();
                for (int messageCharacter = 0; messageCharacter < this.renderChars; messageCharacter++) {
                    messageContentAvailable.set(line, messageContentAvailable.get(line) + currentMessage.toCharArray()[messageCharacter]);
                    //messageContentAvailable += currentMessage.toCharArray()[messageCharacter];

                    if (lineChars >= this.maxLineChars) {
                        line++;
                        messageContentAvailable.add("");
                        lineChars = 0;
                    }

                    lineChars++;
                }

                int lineIndex = 0;
                for (String lineContent : messageContentAvailable) {
                    this.font.draw(this.customBatch, messageContentAvailable.get(lineIndex), 10, this.messageBackground.getHeight() - 12 - lineIndex * 22);
                    lineIndex++;
                }

                this.fontLarge.draw(this.customBatch, "[Paused: Introduction]", Gdx.graphics.getWidth() / 2 - 128, Gdx.graphics.getHeight() / 2 + 32);

                this.customBatch.end();
            }
        }
    }

    public void update(OrthographicCamera camera) {
        if(this.isRunning()) {
            if (!this.waitingForNextMessageItem) {
                if (!this.messageComplete) {
                    this.elapsedSinceNextChar += 1 * Gdx.graphics.getDeltaTime();

                    if (this.elapsedSinceNextChar >= 0.05f) {
                        this.renderChars++;
                        this.elapsedSinceNextChar = 0;
                    }

                    if (this.renderChars == this.getCurrentMessage().length()) {
                        this.elapsedSinceItemWait = 0;
                        this.waitingForNextMessageItem = true;
                        this.waitComplete = false;
                    }
                }
            } else {
                this.elapsedSinceItemWait += 1 * Gdx.graphics.getDeltaTime();

                if (/**this.elapsedSinceItemWait >= 2 ||**/Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    this.waitingForNextMessageItem = false;
                    this.waitComplete = true;

                    this.endMessageItem();
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                this.waitingForNextMessageItem = false;
                this.waitComplete = true;

                this.endMessageItem();
            }
        }
    }

    public void renderText(SpriteBatch batch, String message, Vector2 position, BitmapFont font) {
        batch.end();
        this.customBatch.begin();
        font.draw(this.customBatch, message, position.x, position.y);
        this.customBatch.end();
        batch.begin();
    }

    public void renderLarge(SpriteBatch batch, String message, Vector2 position) {
        this.renderText(batch, message, position, this.fontLarge);
    }

    public void renderSmall(SpriteBatch batch, String message, Vector2 position) {
        this.renderText(batch, message, position, this.fontSmall);
    }

    public void renderMedium(SpriteBatch batch, String message, Vector2 position) {
        this.renderText(batch, message, position, this.font);
    }

    public void endMessageItem() {
        this.messageItemComplete = true;

        this.renderChars = 0;

        this.messageIndex++;

        if(this.messageIndex >= this.messages.length && !this.messageComplete) {
            this.messageComplete = true;
            this.messageIndex--;
            this.running = false;
        }
    }

    public void addMessage(String ... messages) {
        this.rendering = true;
        this.messages = messages;
        this.messageIndex = 0;
        this.elapsedSinceNextChar = 0;

        this.running = true;
    }

    public String getCurrentMessage() {
        return this.messages[this.messageIndex];
    }

    public boolean isRunning() {
        return running;
    }
}
