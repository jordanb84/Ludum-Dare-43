package com.ld.game.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class StateManager {

    private HashMap<String, State> states = new HashMap<String, State>();

    private State activeState;

    public StateManager() {

    }

    public void render(SpriteBatch batch) {
        this.getActiveState().render(batch);
    }

    public void update(OrthographicCamera camera) {
        this.getActiveState().update(camera);
    }

    public void setActiveState(String name) {
        this.activeState = this.states.get(name);
    }

    public State getActiveState() {
        return this.activeState;
    }

    public void registerState(String name, State state) {
        this.states.put(name, state);
    }

}
