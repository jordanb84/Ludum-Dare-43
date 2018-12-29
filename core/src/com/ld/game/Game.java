package com.ld.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ld.game.state.StateManager;
import com.ld.game.state.impl.StateEditor;
import com.ld.game.state.impl.StateTest;

public class Game extends ApplicationAdapter {

	private SpriteBatch batch;

	private OrthographicCamera camera;

	private StateManager stateManager;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();

		this.camera = new OrthographicCamera();
		//this.camera.setToOrtho(false, Gdx.graphics.getWidth() / 2 * 1.5f, Gdx.graphics.getHeight() / 2 * 1.5f);
		this.camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		//this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );

		this.stateManager = new StateManager();

		this.stateManager.registerState("test", new StateTest(this.stateManager));
		this.stateManager.registerState("editor", new StateEditor(this.stateManager));

		this.stateManager.setActiveState("test");
	}

	@Override
	public void render () {
		this.stateManager.update(this.camera);

		this.batch.setProjectionMatrix(this.camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.batch.begin();
		this.stateManager.render(this.batch);
		this.batch.end();

		//System.out.println(Gdx.graphics.getFramesPerSecond());
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
	}
}
