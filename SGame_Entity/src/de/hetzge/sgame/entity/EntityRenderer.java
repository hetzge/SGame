package de.hetzge.sgame.entity;

import java.util.Collection;

import de.hetzge.sgame.common.IF_DependencyInjection;
import de.hetzge.sgame.common.Path;
import de.hetzge.sgame.entity.Entity.RenderRectangle;
import de.hetzge.sgame.render.IF_DrawService;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.RenderService;
import de.hetzge.sgame.render.Viewport;

public class EntityRenderer implements IF_Renderable<IF_RenderableContext>, IF_DependencyInjection {

	private final EntityPool entityPool;
	private final Viewport viewport;
	private final RenderService renderService;
	private final ActiveEntityMap activeEntityMap;
	private final IF_DrawService drawService;

	public EntityRenderer(EntityPool entityPool, Viewport viewport, RenderService renderService, ActiveEntityMap activeEntityMap) {
		this.entityPool = entityPool;
		this.viewport = viewport;
		this.renderService = renderService;
		this.activeEntityMap = activeEntityMap;
		this.drawService = this.getOrNull(IF_DrawService.class);
	}

	@Override
	public void render(IF_RenderableContext context) {
		this.viewport.iterateVisibleTiles((int x, int y) -> {
			Collection<Entity> entities = this.activeEntityMap.getConnectedObjects(x, y);
			for (Entity entity : entities) {
				RenderRectangle renderRectangle = entity.getRenderRectangle();
				this.renderService.render(context, renderRectangle, entity.getRenderableKey());
				this.drawService.printText(renderRectangle.getCenteredPosition(), "...");
			}

			// TODO return null weg machen
			return null;
		});
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {

		this.viewport.iterateVisibleTiles((int x, int y) -> {
			Collection<Entity> entities = this.activeEntityMap.getConnectedObjects(x, y);
			for (Entity entity : entities) {
				this.drawService.drawRectangle(entity.getRealRectangle());
				Path path = entity.getPath();
				if (path != null) {
					this.drawService.drawLine(path.getStartPosition(), path.getEndPosition());
				}
			}

			// TODO return null weg machen
			return null;
		});

	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

}
