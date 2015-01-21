package de.hetzge.sgame.map.tmx;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import de.hetzge.sgame.common.definition.IF_Tileset;

public class TMXMap {

	public class Tileset implements IF_Tileset {

		private final int firstGId;
		private final String name;
		private final String image;
		private final int imageWidth;
		private final int imageHeight;
		private final int margin;
		private final int spacing;
		private final int tileWidth;
		private final int tileHeight;

		private Tileset(JsonObject tilesetJsonObject) {
			this.firstGId = tilesetJsonObject.get("firstgid").asInt();
			this.name = tilesetJsonObject.get("name").asString();
			this.image = tilesetJsonObject.get("image").asString();
			this.imageWidth = tilesetJsonObject.get("imagewidth").asInt();
			this.imageHeight = tilesetJsonObject.get("imageheight").asInt();
			this.margin = tilesetJsonObject.get("margin").asInt();
			this.spacing = tilesetJsonObject.get("spacing").asInt();
			this.tileWidth = tilesetJsonObject.get("tilewidth").asInt();
			this.tileHeight = tilesetJsonObject.get("tileheight").asInt();
		}

		public int getFirstGId() {
			return this.firstGId;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String getImage() {
			return this.image;
		}

		@Override
		public int getImageWidth() {
			return this.imageWidth;
		}

		@Override
		public int getImageHeight() {
			return this.imageHeight;
		}

		public int getMargin() {
			return this.margin;
		}

		public int getSpacing() {
			return this.spacing;
		}

		@Override
		public int getTileWidth() {
			return this.tileWidth;
		}

		@Override
		public int getTileHeight() {
			return this.tileHeight;
		}

	}

	public class Layer {
		private final int[] data;
		private final int width;
		private final int height;
		private final int opacity;
		private final int x;
		private final int y;
		private final String name;
		private final String type;
		private final boolean visible;

		private Layer(JsonObject layerJsonObject) {
			JsonArray dataJsonArray = layerJsonObject.get("data").asArray();
			this.data = new int[dataJsonArray.size()];
			for (int i = 0; i < this.data.length; i++) {
				this.data[i] = dataJsonArray.get(i).asInt();
			}
			this.width = layerJsonObject.get("width").asInt();
			this.height = layerJsonObject.get("height").asInt();
			this.opacity = layerJsonObject.get("opacity").asInt();
			this.x = layerJsonObject.get("x").asInt();
			this.y = layerJsonObject.get("y").asInt();
			this.name = layerJsonObject.get("name").asString();
			this.type = layerJsonObject.get("type").asString();
			this.visible = layerJsonObject.get("visible").asBoolean();
		}

		public int getData(int x, int y) {
			return this.data[y * this.width + x];
		}

		public int[] getData() {
			return this.data;
		}

		public int getWidth() {
			return this.width;
		}

		public int getHeight() {
			return this.height;
		}

		public int getOpacity() {
			return this.opacity;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}

		public String getName() {
			return this.name;
		}

		public String getType() {
			return this.type;
		}

		public boolean isVisible() {
			return this.visible;
		}

	}

	private final List<Tileset> tilesets;
	private final List<Layer> layers;

	private final int width;
	private final int height;
	private final String orientation;
	private final int tileWidth;
	private final int tileHeight;
	private final int version;

	public TMXMap(String pathToJsonFile) {
		try {
			if (!pathToJsonFile.endsWith(".json")) {
				throw new IllegalArgumentException("path must end with .json");
			}

			String json = new String(Files.readAllBytes(Paths.get(pathToJsonFile)), StandardCharsets.UTF_8);
			JsonObject jsonMap = JsonObject.readFrom(json);

			JsonArray layersJsonArray = jsonMap.get("layers").asArray();
			this.layers = new ArrayList<>(layersJsonArray.size());
			for (JsonValue jsonValue : layersJsonArray) {
				this.layers.add(new Layer(jsonValue.asObject()));
			}

			JsonArray tilesetsJsonArray = jsonMap.get("tilesets").asArray();
			this.tilesets = new ArrayList<>(tilesetsJsonArray.size());
			for (JsonValue jsonValue : tilesetsJsonArray) {
				this.tilesets.add(new Tileset(jsonValue.asObject()));
			}

			this.width = jsonMap.get("width").asInt();
			this.height = jsonMap.get("height").asInt();
			this.orientation = jsonMap.get("orientation").asString();
			this.tileWidth = jsonMap.get("tilewidth").asInt();
			this.tileHeight = jsonMap.get("tileheight").asInt();
			this.version = jsonMap.get("version").asInt();

		} catch (IOException e) {
			throw new IllegalStateException("Could not load tmx tilemap from json");
		}
	}

	public List<Tileset> getTilesets() {
		return this.tilesets;
	}

	public List<Layer> getLayers() {
		return this.layers;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public String getOrientation() {
		return this.orientation;
	}

	public int getTileWidth() {
		return this.tileWidth;
	}

	public int getTileHeight() {
		return this.tileHeight;
	}

	public int getVersion() {
		return this.version;
	}

}
