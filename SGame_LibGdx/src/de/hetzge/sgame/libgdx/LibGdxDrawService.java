package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.render.IF_DrawService;

public class LibGdxDrawService implements IF_DrawService {

	private final ThreadLocal<ShapeRenderer> lineShapeRendererThreadLocal = new ThreadLocal<>();
	private final ThreadLocal<ShapeRenderer> filledShapeRendererThreadLocal = new ThreadLocal<>();
	private final ThreadLocal<SpriteBatch> fontSpriteBatchThreadLocal = new ThreadLocal<>(); 

	// TODO auslagern
	private BitmapFont bitmapFont;

	public void setFilledShapeRenderer(ShapeRenderer filledShapeRenderer) {
		this.filledShapeRendererThreadLocal.set(filledShapeRenderer);
	}

	public void setLineShapeRenderer(ShapeRenderer lineShapeRenderer) {
		this.lineShapeRendererThreadLocal.set(lineShapeRenderer);
	}

	public void setFontSpriteBatch(SpriteBatch spriteBatch){
		this.fontSpriteBatchThreadLocal.set(spriteBatch);
	}

	public void setBitmapFont(BitmapFont bitmapFont) {
		this.bitmapFont = bitmapFont;
	}

	@Override
	public void drawLine(IF_Position_ImmutableView from, IF_Position_ImmutableView to) {
		ShapeRenderer shapeRenderer = this.lineShapeRendererThreadLocal.get();
		this.checkShapeRenderer(shapeRenderer);
		shapeRenderer.line(from.getFX(), from.getFY(), to.getFX(), to.getFY());
	}

	@Override
	public void drawRectangle(IF_Rectangle_ImmutableView rectangle) {
		ShapeRenderer shapeRenderer = this.lineShapeRendererThreadLocal.get();
		this.checkShapeRenderer(shapeRenderer);
		IF_Position_ImmutableView positionA = rectangle.getPositionA();
		IF_Dimension_ImmutableView dimension = rectangle.getDimension();
		shapeRenderer.rect(positionA.getFX(), positionA.getFY(), dimension.getWidth(), dimension.getHeight());
	}

	private void checkShapeRenderer(ShapeRenderer shapeRenderer) {
		if (shapeRenderer == null) {
			throw new IllegalAccessError("ShapeRenderer is not set in current thread. Eventually you try to render from non render thread.");
		}
	}

	private void checkSpriteBatch(SpriteBatch spriteBatch){
		if(spriteBatch == null){
			throw new IllegalAccessError("SpriteBatch is not set in current thread. Eventually you try to render from non render thread.");
		}
	}

	@Override
	public void printText(IF_Position_ImmutableView position, String text) {
		SpriteBatch spriteBatch = this.fontSpriteBatchThreadLocal.get();
		this.checkSpriteBatch(spriteBatch);
		this.bitmapFont.draw(spriteBatch, text, position.getFX(), position.getFY());

	}

}
