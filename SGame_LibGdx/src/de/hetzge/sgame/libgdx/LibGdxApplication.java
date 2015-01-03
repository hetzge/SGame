package de.hetzge.sgame.libgdx;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableContext;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderUtil;

public class LibGdxApplication implements ApplicationListener {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private LibGdxRenderableContext libGdxRenderableContext;
	private final FPSLogger fpsLogger;

	// for reuse
	private Position viewportPosition = new Position();
	private Dimension viewportDimension = new Dimension();

	public LibGdxApplication() {
		this.fpsLogger = new FPSLogger();
	}

	@Override
	public void create() {
		this.camera = new OrthographicCamera();
		this.batch = new SpriteBatch();
		this.libGdxRenderableContext = new LibGdxRenderableContext(this.batch);
		this.camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		RenderConfig.INSTANCE.initRenderablePool.accept(RenderConfig.INSTANCE.renderablePool);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		System.out.println(RenderUtil.renderCounter);
		RenderUtil.renderCounter = 0;

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

		this.camera.update();
		this.batch.setProjectionMatrix(this.camera.combined);

		this.batch.begin();

		RenderConfig.INSTANCE.renderablePool.render(this.libGdxRenderableContext);

		this.batch.end();

		// sync viewport with camera

		this.viewportPosition.setX(this.camera.position.x);
		this.viewportPosition.setY(this.camera.position.y);

		this.viewportDimension.setWidth(this.camera.viewportWidth);
		this.viewportDimension.setHeight(this.camera.viewportHeight);

		RenderConfig.INSTANCE.viewport.setPosition(this.viewportPosition);
		RenderConfig.INSTANCE.viewport.setDimension(this.viewportDimension);

		// TEMP
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			this.camera.translate(1f, 0f);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			this.camera.translate(-1f, 0f);
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
			this.camera.translate(0f, -1f);
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			this.camera.translate(0f, 1f);

		this.fpsLogger.log();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
