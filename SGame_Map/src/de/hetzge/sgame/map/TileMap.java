package de.hetzge.sgame.map;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.function.Consumer;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.hetzge.sgame.common.IF_XYFunction;
import de.hetzge.sgame.common.Stopwatch;
import de.hetzge.sgame.common.activemap.ActiveCollisionMap;
import de.hetzge.sgame.common.definition.IF_Collision;
import de.hetzge.sgame.common.definition.IF_Map;
import de.hetzge.sgame.common.definition.IF_RenderInformation;
import de.hetzge.sgame.common.geometry.ComplexRectangle;
import de.hetzge.sgame.common.geometry.Dimension;
import de.hetzge.sgame.common.geometry.IF_ImmutablePrimitivRectangle;
import de.hetzge.sgame.common.geometry.Position;
import de.hetzge.sgame.common.serializer.Serializer;
import de.hetzge.sgame.render.IF_Renderable;
import de.hetzge.sgame.render.IF_RenderableContext;
import de.hetzge.sgame.render.PredefinedRenderId;
import de.hetzge.sgame.render.RenderConfig;
import de.hetzge.sgame.render.RenderUtil;

public class TileMap<CONTEXT extends IF_RenderableContext> implements IF_Map, IF_Renderable<IF_RenderableContext>, Serializable {

	private class Tile implements IF_RenderInformation, Serializable, IF_ImmutablePrimitivRectangle {

		// Rectangle interface auch verwenden

		private final int renderId;

		private final int x;
		private final int y;

		public Tile(int x, int y, int renderId) {
			this.renderId = renderId;
			this.x = x;
			this.y = y;
		}

		@Override
		public IF_ImmutablePrimitivRectangle getRenderedRectangle() {
			return this;
		}

		@Override
		public int getRenderableKey() {
			return this.renderId;
		}

		@Override
		public float getX() {
			return this.x * TileMap.this.tileSize;
		}

		@Override
		public float getY() {
			return this.y * TileMap.this.tileSize;
		}

		@Override
		public float getWidth() {
			return TileMap.this.tileSize;
		}

		@Override
		public float getHeight() {
			return TileMap.this.tileSize;
		}
	}

	private class MapCollision implements IF_Collision, Serializable {

		@Override
		public int getWidthInTiles() {
			return TileMap.this.widthInTiles;
		}

		@Override
		public int getHeightInTiles() {
			return TileMap.this.heightInTiles;
		}

		@Override
		public boolean isCollision(int x, int y) {
			return this.isFixedCollision(x, y) || this.isFlexibleCollision(x, y);
		}

		public boolean isFixedCollision(int x, int y) {
			int tileX = (x - (x % TileMap.this.collisionTileFactor)) / TileMap.this.collisionTileFactor;
			int tileY = (y - (y % TileMap.this.collisionTileFactor)) / TileMap.this.collisionTileFactor;

			// TODO resolve tile and tile collision
			// boolean isMapCollision =
			// TileMap.this.tiles[tileX][tileY].tileDefinition.getCollision().isCollision(x
			// % TileMap.this.collisionTileFactor, y %
			// TileMap.this.collisionTileFactor);
			// if (isMapCollision)
			// return true;

			Collection<Boolean> flexibleConnectedObjects = TileMap.this.flexibleEntityCollisionMap.getConnectedObjects(x, y);
			for (Boolean aBoolean : flexibleConnectedObjects) {
				if (aBoolean) {
					return true;
				}
			}
			return false;
		}

		public boolean isFlexibleCollision(int x, int y) {
			Collection<Boolean> fixedConnectedObjects = TileMap.this.fixEntityCollisionMap.getConnectedObjects(x, y);
			for (Boolean aBoolean : fixedConnectedObjects) {
				if (aBoolean) {
					return true;
				}
			}

			return false;
		}

		@Override
		public void setCollision(int x, int y, boolean collision) {
			throw new IllegalAccessError("Don't set collision direct on map");
		}

	}

	private final Tile[][] tiles;
	private final int tileSize;
	private final int collisionTileFactor;
	private final int widthInTiles;
	private final int heightInTiles;
	private final MapCollision mapCollision;
	private final ActiveCollisionMap fixEntityCollisionMap;
	private final ActiveCollisionMap flexibleEntityCollisionMap;

	public static void main(String[] args) {

		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			for (int i = 0; i < 1; i++) {
				TileMap<IF_RenderableContext> tileMap = new TileMap<>("C:\\SGame Workspace\\SGame_Game\\assets\\map.json");
				fout = new FileOutputStream("cache/" + i + ".map");
				oos = new ObjectOutputStream(fout);
				oos.write(Serializer.toByteArray(tileMap));

				oos.close();
				fout.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TileMap(int widthInTiles, int heightInTiles) {
		this.tileSize = 32;
		this.collisionTileFactor = 3;
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
		this.tiles = new TileMap.Tile[widthInTiles][heightInTiles];

		Stopwatch stopwatch = new Stopwatch("init tile map tiles array");
		for (int x = 0; x < widthInTiles; x++) {
			for (int y = 0; y < heightInTiles; y++) {
				this.tiles[x][y] = new Tile(x, y, 0);
			}
		}
		stopwatch.stop();

		this.mapCollision = new MapCollision();
		this.fixEntityCollisionMap = new ActiveCollisionMap(widthInTiles * this.collisionTileFactor, heightInTiles * this.collisionTileFactor);
		this.flexibleEntityCollisionMap = new ActiveCollisionMap(widthInTiles * this.collisionTileFactor, heightInTiles * this.collisionTileFactor);
	}

	public TileMap(String path) {
		try {
			if (!path.endsWith(".json")) {
				throw new IllegalArgumentException("path must end with .json");
			}

			String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

			JsonObject jsonMap = JsonObject.readFrom(json);

			this.tileSize = 32;
			this.collisionTileFactor = 3;
			this.widthInTiles = jsonMap.get("width").asInt();
			this.heightInTiles = jsonMap.get("height").asInt();
			JsonArray layers = jsonMap.get("layers").asArray();

			JsonArray tilesets = jsonMap.get("tilesets").asArray();
			String[] tileSetImages = new String[tilesets.size()];
			int i = 0;
			for (JsonValue jsonValue : tilesets) {
				JsonObject tileSet = jsonValue.asObject();
				String tileSetImage = tileSet.get("image").asString();
				tileSetImages[i++] = tileSetImage;
			}
			int[] tileRenderIds = RenderConfig.INSTANCE.renderableLoader.loadTilesets(tileSetImages, this.tileSize);

			// TODO other layers
			JsonObject firstLayer = layers.get(0).asObject();
			JsonArray firstLayerDatas = firstLayer.get("data").asArray();

			this.tiles = new TileMap.Tile[this.widthInTiles][this.heightInTiles];
			for (int x = 0; x < this.widthInTiles; x++) {
				for (int y = 0; y < this.heightInTiles; y++) {
					this.tiles[x][y] = new Tile(x, y, tileRenderIds[firstLayerDatas.get(y * this.widthInTiles + x).asInt()]);
				}
			}

			this.mapCollision = new MapCollision();
			this.fixEntityCollisionMap = new ActiveCollisionMap(this.widthInTiles * this.collisionTileFactor, this.heightInTiles * this.collisionTileFactor);
			this.flexibleEntityCollisionMap = new ActiveCollisionMap(this.widthInTiles * this.collisionTileFactor, this.heightInTiles * this.collisionTileFactor);
		} catch (IOException e) {
			throw new IllegalStateException();
		}
	}

	@Override
	public void render(IF_RenderableContext context) {
		this.iterateVisibleTiles((tile) -> {
			RenderUtil.render(context, tile);
		});
	}

	@Override
	public void renderShapes(IF_RenderableContext context) {

		this.iterateVisibleTiles((tile) -> {
			for (int cx = 0; cx < this.collisionTileFactor; cx++) {
				for (int cy = 0; cy < this.collisionTileFactor; cy++) {
					int x = tile.x * this.collisionTileFactor + cx;
					int y = tile.y * this.collisionTileFactor + cy;
					if (this.mapCollision.isCollision(x, y)) {
						RenderUtil.render(context, new IF_RenderInformation() {
							@Override
							public ComplexRectangle getRenderedRectangle() {
								return new ComplexRectangle(new Position(x * TileMap.this.getCollisionTileSize(), y * TileMap.this.getCollisionTileSize()), new Dimension(TileMap.this.getCollisionTileSize(), TileMap.this.getCollisionTileSize()));
							}

							@Override
							public int getRenderableKey() {
								return PredefinedRenderId.RECTANGLE;
							}
						});
					}
				}
			}

		});
	}

	@Override
	public void renderFilledShapes(IF_RenderableContext context) {
	}

	public void iterateVisibleTiles(Consumer<Tile> consumer) {
		RenderConfig.INSTANCE.viewport.iterateVisibleTiles((IF_XYFunction<Void>) (int x, int y) -> {
			TileMap<CONTEXT>.Tile tile = this.tiles[x][y];
			consumer.accept(tile);
			return null;
		});
	}

	@Override
	public int getWidthInTiles() {
		return this.widthInTiles;
	}

	@Override
	public int getHeightInTiles() {
		return this.heightInTiles;
	}

	@Override
	public int getCollisionTileFactor() {
		return 3;
	}

	@Override
	public IF_Collision getCollision() {
		return null;
	}

	@Override
	public ActiveCollisionMap getFixEntityCollisionMap() {
		return this.fixEntityCollisionMap;
	}

	@Override
	public ActiveCollisionMap getFlexibleEntityCollisionMap() {
		return this.flexibleEntityCollisionMap;
	}

	@Override
	public float getTileSize() {
		return this.tileSize;
	}

}
