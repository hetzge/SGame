package de.hetzge.sgame.libgdx;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.hetzge.sgame.common.newgeometry.views.IF_Dimension_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Position_ImmutableView;
import de.hetzge.sgame.common.newgeometry.views.IF_Rectangle_ImmutableView;
import de.hetzge.sgame.render.IF_DrawService;

public class LibGdxDrawService implements IF_DrawService {

	private ThreadLocal<ShapeRenderer> lineShapeRendererThreadLocal = new ThreadLocal<>();
	private ThreadLocal<ShapeRenderer> filledShapeRendererThreadLocal = new ThreadLocal<>();

	public void setFilledShapeRenderer(ShapeRenderer filledShapeRenderer) {
		this.filledShapeRendererThreadLocal.set(filledShapeRenderer);
	}

	public void setLineShapeRenderer(ShapeRenderer lineShapeRenderer) {
		this.lineShapeRendererThreadLocal.set(lineShapeRenderer);
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

}
