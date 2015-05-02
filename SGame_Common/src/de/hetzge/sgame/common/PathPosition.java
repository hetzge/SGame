package de.hetzge.sgame.common;

import java.io.Serializable;
import java.util.function.Consumer;

public class PathPosition implements Serializable {

	private final Path path;
	private int positionOnPath;

	private transient Consumer<PathPosition> onPathPositionChangedCallback;

	public PathPosition(Path path) {
		this(path, 0);
	}

	public PathPosition(Path path, int positionOnPath) {
		this.path = path;
		this.positionOnPath = positionOnPath;
	}

	public int getPositionOnPath() {
		return this.positionOnPath;
	}

	public Path getPath() {
		return this.path;
	}

	public boolean isOnEndOfPath() {
		return this.positionOnPath >= this.path.getPathLength() - 1;
	}

	public boolean isOnStartOfPath() {
		return this.positionOnPath <= 0;
	}

	public void moveForward() {
		if (this.positionOnPath + 1 >= this.path.getPathLength()) {
			this.positionOnPath = this.path.getPathLength() - 1;
		} else {
			this.positionOnPath++;
		}
		this.callOnPathPositionChangedCallback();
	}

	public void moveBackward() {
		if (this.positionOnPath - 1 < 0) {
			this.positionOnPath = 0;
		} else {
			this.positionOnPath--;
		}
		this.callOnPathPositionChangedCallback();
	}

	public void setOnPathPositionChangedCallback(Consumer<PathPosition> onPathPositionChangedCallback) {
		this.onPathPositionChangedCallback = onPathPositionChangedCallback;
	}

	private void callOnPathPositionChangedCallback() {
		if(this.onPathPositionChangedCallback != null) {
			this.onPathPositionChangedCallback.accept(this);
		}
	}

}
