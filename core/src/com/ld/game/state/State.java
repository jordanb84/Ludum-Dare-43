package com.ld.game.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {

    private StateManager stateManager;

    public State(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public abstract void create();

    public abstract void render(SpriteBatch batch);

    public abstract void update(OrthographicCamera camera);

    public StateManager getStateManager() {
        return stateManager;
    }
}
