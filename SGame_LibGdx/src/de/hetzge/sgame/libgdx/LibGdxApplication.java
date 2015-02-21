package de.hetzge.sgame.libgdx;

import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.hetzge.sgame.common.newgeometry.XY;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableContext;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderPool;
import de.hetzge.sgame.render.RenderService;
import de.hetzge.sgame.render.RenderableRessourcePool;
import de.hetzge.sgame.render.Viewport;

public class LibGdxApplication implements ApplicationListener {
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private ShapeRenderer filledShapeRenderer;
	private OrthographicCamera camera;
	private LibGdxRenderableContext libGdxRenderableContext;

	private final FPSLogger fpsLogger;

	private final RenderConfig renderConfig;
	private final RenderPool renderPool;
	private final Viewport mapViewport;
	private final RenderableRessourcePool renderableRessourcePool;

	public LibGdxApplication(RenderConfig renderConfig, RenderPool renderPool, Viewport mapViewport, RenderableRessourcePool renderableRessourcePool) {
		this.fpsLogger = new FPSLogger();
		this.renderConfig = renderConfig;
		this.renderPool = renderPool;
		this.mapViewport = mapViewport;
		this.renderableRessourcePool = renderableRessourcePool;
	}

	@Override
	public void create() {
		this.camera = new OrthographicCamera();
		this.batch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
		this.filledShapeRenderer = new ShapeRenderer();
		this.libGdxRenderableContext = new LibGdxRenderableContext(this.batch, this.shapeRenderer, this.filledShapeRenderer);
		this.camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		for (Consumer<RenderableRessourcePool> consumer : this.renderConfig.initRenderableConsumers) {
			consumer.accept(this.renderableRessourcePool);
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
		this.renderPool.render(this.libGdxRenderableContext);
		this.batch.end();

		this.filledShapeRenderer.setProjectionMatrix(this.camera.combined);
		this.filledShapeRenderer.begin(ShapeType.Filled);
		this.renderPool.renderFilledShapes(this.libGdxRenderableContext);
		this.filledShapeRenderer.end();

		this.shapeRenderer.setProjectionMatrix(this.camera.combined);
		this.shapeRenderer.begin(ShapeType.Line);
		this.renderPool.renderShapes(this.libGdxRenderableContext);
		this.shapeRenderer.end();

		// sync viewport with camera
		this.mapViewport.setCenteredPosition(new XY(this.camera.position.x, this.camera.position.y));
		this.mapViewport.setDimension(new XY(this.camera.viewportWidth, this.camera.viewportHeight));

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

		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			IF_Position_ImmutableView mouseClickPosition = new XY(Gdx.input.getX(), Gdx.input.getY()).add(this.mapViewport.getPositionA());
			System.out.println("You clicked " + mouseClickPosition.getX() + "/" + mouseClickPosition.getY());
		}

		this.fpsLogger.log();
		// System.out.println(RenderUtil.renderCount);
		RenderService.renderCount = 0;

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
