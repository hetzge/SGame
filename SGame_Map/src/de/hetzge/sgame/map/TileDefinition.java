package de.hetzge.sgame.map;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.hetzge.sgame.common.BaseCollisionImpl;
import de.hetzge.sgame.common.definition.IF_Collision;

public class TileDefinition implements Serializable {

	private final List<TileLayer> tileLayers = new LinkedList<>();

	private IF_Collision cachedCollision = null;
	private String cachedKey = null;

	public void addTileLayer(TileLayer tileLayer) {
		this.tileLayers.add(tileLayer);
		this.resetCache();
	}

	public void setTileLayer(int layer, TileLayer tileLayer) {
		this.fillTillLayer(layer);
		this.tileLayers.set(layer, tileLayer);
		this.resetCache();
	}

	private void resetCache() {
		this.cachedKey = null;
		this.cachedCollision = null;
	}

	private void fillTillLayer(int layer) {
		while (this.tileLayers.size() < layer) {
			this.tileLayers.add(null);
		}
	}

	public List<TileLayer> getTileLayers() {
		return this.tileLayers;
	}

	String calculateKey() {
		String key = "";
		for (TileLayer tileLayer : this.tileLayers) {
			key += "_" + tileLayer.calculateKey();
		}
		return key;
	}

	IF_Collision calculateCollision() {
		int collisionTileFactor = MapContext.INSTANCE.get().tileMap.getCollisionTileFactor();
		BaseCollisionImpl collision = new BaseCollisionImpl(collisionTileFactor, collisionTileFactor);
		for (TileLayer tileLayer : this.tileLayers) {
			if (tileLayer.isCollision()) {
				IF_Collision tileOrientationAsCollision = tileLayer.getTileOrientation().asCollision();
				collision.setCollision(0, 0, tileOrientationAsCollision);
			}
		}
		return collision;
	}

	public String getKey() {
		if (this.cachedKey == null) {
			this.cachedKey = this.calculateKey();
		}
		return this.cachedKey;
	}

	public IF_Collision getCollision() {
		if (this.cachedCollision == null) {
			this.cachedCollision = this.calculateCollision();
		}
		return this.cachedCollision;
	}

}
