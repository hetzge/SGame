package de.hetzge.sgame.libgdx;

import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableContext;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderUtil;
import de.hetzge.sgame.render.RenderableRessourcePool;

public class LibGdxApplication implements ApplicationListener {
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private ShapeRenderer filledShapeRenderer;
	private OrthographicCamera camera;
	private LibGdxRenderableContext libGdxRenderableContext;
	private final FPSLogger fpsLogger;

	public LibGdxApplication() {
		this.fpsLogger = new FPSLogger();
	}

	@Override
	public void create() {
		this.camera = new OrthographicCamera();
		this.batch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.filledShapeRenderer = new ShapeRenderer();
		this.libGdxRenderableContext = new LibGdxRenderableContext(this.batch, this.shapeRenderer, this.filledShapeRenderer);
		this.camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		for (Consumer<RenderableRessourcePool> consumer : RenderConfig.INSTANCE.initRenderableConsumers) {
			consumer.accept(RenderConfig.INSTANCE.renderableRessourcePool);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

		this.camera.update();

		this.batch.setProjectionMatrix(this.camera.combined);
		this.batch.begin();
		RenderConfig.INSTANCE.renderPool.render(this.libGdxRenderableContext);
		this.batch.end();

		this.filledShapeRenderer.setProjectionMatrix(this.camera.combined);
		this.filledShapeRenderer.begin(ShapeType.Filled);
		RenderConfig.INSTANCE.renderPool.renderFilledShapes(this.libGdxRenderableContext);
		this.filledShapeRenderer.end();

		this.shapeRenderer.setProjectionMatrix(this.camera.combined);
		this.shapeRenderer.begin(ShapeType.Line);
		RenderConfig.INSTANCE.renderPool.renderShapes(this.libGdxRenderableContext);
		this.shapeRenderer.end();

		// sync viewport with camera

		RenderConfig.INSTANCE.viewport.setX(this.camera.position.x);
		RenderConfig.INSTANCE.viewport.setY(this.camera.position.y);

		RenderConfig.INSTANCE.viewport.setWidth(this.camera.viewportWidth);
		RenderConfig.INSTANCE.viewport.setHeight(this.camera.viewportHeight);

		// TEMP
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.camera.translate(3f, 0f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.camera.translate(-3f, 0f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			this.camera.translate(0f, -3f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.camera.translate(0f, 3f);
		}

		this.fpsLogger.log();
		// System.out.println(RenderUtil.renderCount);
		RenderUtil.renderCount = 0;

		LibGdxConfig.INSTANCE.stateTime += Gdx.graphics.getDeltaTime();
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
