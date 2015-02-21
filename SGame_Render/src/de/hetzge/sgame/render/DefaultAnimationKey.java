package de.hetzge.sgame.render;


public enum DefaultAnimationKey implements IF_AnimationKey {
	IDLE, WALK, WORK, FIGHT;

	public static final DefaultAnimationKey DEFAULT = DefaultAnimationKey.IDLE;
}
