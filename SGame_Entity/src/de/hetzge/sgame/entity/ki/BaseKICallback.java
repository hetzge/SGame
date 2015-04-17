package de.hetzge.sgame.entity.ki;

public interface BaseKICallback {

	default void onInitialize() {
	}

	default void onFailure() {
	}

	default void onSuccess() {
	}

}
