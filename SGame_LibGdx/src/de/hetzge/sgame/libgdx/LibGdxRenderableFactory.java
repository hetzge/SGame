package de.hetzge.sgame.libgdx;

import java.util.List;
import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.geometry.ComplexRectangle;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableContext;
import de.hetzge.sgame.libgdx.renderable.LibGdxRenderableTexture;
import de.hetzge.sgame.map.GroundType;
import de.hetzge.sgame.map.IF_Ground;
import de.hetzge.sgame.map.TileDefinition;
import de.hetzge.sgame.map.TileLayer;
import de.hetzge.sgame.map.TileOrientation;
import de.hetzge.sgame.render.IF_PixelAccess;
import de.hetzge.sgame.render.IF_RenderableFactory;
import de.hetzge.sgame.render.IF_RenderableWrapper;

public class LibGdxRenderableFactory implements IF_RenderableFactory, ApplicationListener {

	private IF_RenderableWrapper<LibGdxRenderableContext> renderable;
	private SpriteBatch spriteBatch;
	private LibGdxRenderableContext libGdxRenderableContext;

	public IF_RenderableWrapper<LibGdxRenderableContext> by(TileDefinition tileDefinition) {

		long timeBefore = System.currentTimeMillis();

		List<TileLayer> tileLayers = tileDefinition.getTileLayers();
		int maxTemplateWidthInPx = 0;
		int maxTemplateHeightInPx = 0;
		for (TileLayer tileLayer : tileLayers) {
			IF_PixelAccess templatePixelAccess = tileLayer.getGround().getTemplatePixelAccess();
			if (templatePixelAccess.getWidth() > maxTemplateWidthInPx) {
				maxTemplateWidthInPx = templatePixelAccess.getWidth();
			}
			if (templatePixelAccess.getHeight() > maxTemplateHeightInPx) {
				maxTemplateHeightInPx = templatePixelAccess.getHeight();
			}
		}

		Pixmap[] pixmaps = new Pixmap[tileLayers.size()];

		int tileLayerI = 0;
		for (TileLayer tileLayer : tileLayers) {
			pixmaps[tileLayerI] = new Pixmap(maxTemplateWidthInPx, maxTemplateHeightInPx, Format.RGBA8888);
			Pixmap.setBlending(Blending.None);

			IF_PixelAccess templatePixelAccess = tileLayer.getGround().getTemplatePixelAccess();
			TileOrientation tileOrientation = tileLayer.getTileOrientation();

			IF_Ground ground = tileLayer.getGround();
			GroundType groundType = ground.getGroundType();

			int widthInAreas = tileOrientation.calculateSideLength();
			int heightInAreas = tileOrientation.calculateSideLength();
			int areaWidthInPx = templatePixelAccess.getWidth() / widthInAreas;
			int areaHeightInPx = templatePixelAccess.getHeight() / heightInAreas;

			for (int x = 0; x < widthInAreas; x++) {
				for (int y = 0; y < heightInAreas; y++) {
					if (tileOrientation.isX(x, y)) {
						int originX = x * areaWidthInPx + areaWidthInPx / 2;
						int originY = y * areaHeightInPx + areaHeightInPx / 2;
						int radius = areaWidthInPx > areaHeightInPx ? areaWidthInPx : areaHeightInPx;

						for (int ry = -radius; ry <= radius; ry++) {
							for (int rx = -radius; rx <= radius; rx++) {
								if (rx * rx + ry * ry <= radius * radius) {
									if (originX + x <= templatePixelAccess.getWidth() && originX + x >= 0) {
										if (originY + x <= templatePixelAccess.getHeight() && originY + ry >= 0) {
											Color color = LibGdxUtil.convertAwtColor(templatePixelAccess.getColor(originX + rx, originY + ry));
											pixmaps[tileLayerI].drawPixel(originX + rx, originY + ry, Color.rgba8888(color));
										}
									}
								}
							}
						}
					}
				}
			}

			for (int i = 0; i < 5; i++) {
				LibGdxRenderableFactory.noiseEdges(pixmaps[tileLayerI]);
				LibGdxRenderableFactory.darkenEdges(pixmaps[tileLayerI]);
			}

			for (int i = 0; i < 2; i++) {
				LibGdxRenderableFactory.noise(pixmaps[tileLayerI]);
			}

			tileLayerI++;
		}

		Pixmap result = new Pixmap(maxTemplateWidthInPx, maxTemplateHeightInPx, Format.RGBA8888);
		for (int i = 0; i < pixmaps.length; i++) {
			Pixmap pixmap = pixmaps[i];
			Pixmap.setBlending(Blending.SourceOver);
			for (int x = 0; x < pixmap.getWidth(); x++) {
				for (int y = 0; y < pixmap.getHeight(); y++) {
					result.drawPixel(x, y, Color.rgba8888(new Color(pixmap.getPixel(x, y))));
				}
			}
		}

		System.out.println("Time: " + (System.currentTimeMillis() - timeBefore));

		return new LibGdxRenderableTexture(new Texture(result));
	}

	private static void noiseEdges(Pixmap pixmap) {
		LibGdxRenderableFactory.iterateEdges(pixmap, (color) -> {
			if (Math.random() > 0.5f) {
				Color newColor = color.add(0f, 0f, 0f, (float) -Math.random());
				return newColor;
			} else {
				return color;
			}
		});

	}

	private static void darkenEdges(Pixmap pixmap) {
		LibGdxRenderableFactory.iterateEdges(pixmap, (color) -> {
			if (Math.random() > 0.2f) {
				color.sub(0.1f, 0.1f, 0.1f, 0f);
			}
			return color;
		});
	}

	private static void noise(Pixmap pixmap) {
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				Color color = new Color(pixmap.getPixel(x, y));
				if (Math.random() > 0.5f) {
					color.sub(0.1f, 0.1f, 0.1f, 0f);
				} else {
					color.add(0.1f, 0.1f, 0.1f, 0f);
				}
				pixmap.drawPixel(x, y, Color.rgba8888(color));
			}
		}
	}

	private static void iterateEdges(Pixmap pixmap, Function<Color, Color> function) {
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				Color color = new Color(pixmap.getPixel(x, y));
				if (color.a > 0f) {
					boolean noAlphaAround = LibGdxRenderableFactory.isNoAlphaAround(pixmap, x, y);
					if (noAlphaAround) {
						Color newColor = function.apply(color);
						pixmap.drawPixel(x, y, Color.rgba8888(newColor));
					}
				}
			}
		}

	}

	private static boolean isNoAlphaAround(Pixmap pixmap, int x, int y) {
		Position position = new Position(x, y);
		boolean noAlphaAround = false;
		for (Orientation orientation : Orientation.values()) {
			Position positionAround = position.copy().add(orientation.orientationFactor);
			if (positionAround.getX() > 0 && positionAround.getY() > 0 && positionAround.getX() < pixmap.getWidth() && positionAround.getY() < pixmap.getHeight()) {
				Color color = new Color(pixmap.getPixel((int) positionAround.getX(), (int) positionAround.getY()));
				if (color.a <= 0.2) {
					noAlphaAround = true;
					break;
				}
			}
		}
		return noAlphaAround;
	}

	@Override
	public void create() {

		TileDefinition tileDefinition = new TileDefinition();

		TileLayer tileLayer = new TileLayer(new IF_Ground() {
			@Override
			public GroundType getGroundType() {
				return GroundType.SMOOTH;
			}

			@Override
			public IF_PixelAccess getTemplatePixelAccess() {
				return new IF_PixelAccess() {
					@Override
					public java.awt.Color getColor(int x, int y) {
						return java.awt.Color.BLUE;
					}

					@Override
					public int getWidth() {
						return 32;
					}

					@Override
					public int getHeight() {
						return 32;
					}
				};
			}
		}, TileOrientation.CROSS);

		TileLayer tileLayer2 = new TileLayer(new IF_Ground() {
			@Override
			public GroundType getGroundType() {
				return GroundType.SMOOTH;
			}

			@Override
			public IF_PixelAccess getTemplatePixelAccess() {
				return new IF_PixelAccess() {
					@Override
					public java.awt.Color getColor(int x, int y) {
						return java.awt.Color.RED;
					}

					@Override
					public int getWidth() {
						return 32;
					}

					@Override
					public int getHeight() {
						return 32;
					}
				};
			}
		}, TileOrientation.HALF_DOWN);

		TileLayer tileLayer3 = new TileLayer(new IF_Ground() {
			@Override
			public GroundType getGroundType() {
				return GroundType.SMOOTH;
			}

			@Override
			public IF_PixelAccess getTemplatePixelAccess() {
				return new IF_PixelAccess() {
					@Override
					public java.awt.Color getColor(int x, int y) {
						return java.awt.Color.GREEN;
					}

					@Override
					public int getWidth() {
						return 32;
					}

					@Override
					public int getHeight() {
						return 32;
					}
				};
			}
		}, TileOrientation.WAY_UP_DOWN);

		tileDefinition.addTileLayer(tileLayer);
		tileDefinition.addTileLayer(tileLayer2);
		tileDefinition.addTileLayer(tileLayer3);

		this.renderable = new LibGdxRenderableFactory().by(tileDefinition);
		this.spriteBatch = new SpriteBatch();

		this.libGdxRenderableContext = new LibGdxRenderableContext(this.spriteBatch, null, null);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT);

		this.spriteBatch.begin();
		this.renderable.render(this.libGdxRenderableContext, new IF_RenderInformation() {

			@Override
			public ComplexRectangle getRenderedRectangle() {
				return new ComplexRectangle(new Position(100, 100), new Dimension(100, 100));
			}

			@Override
			public int getRenderableKey() {
				return 0;
			}
		});
		this.spriteBatch.end();
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

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = LibGdxConfig.INSTANCE.gameTitle;
		cfg.useGL30 = false;
		cfg.width = 480;
		cfg.height = 320;
		new LwjglApplication(new LibGdxRenderableFactory(), cfg);
	}

}
