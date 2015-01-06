package de.hetzge.sgame.entity.ki;

import de.hetzge.sgame.entity.Entity;

public interface IF_KI {
	public boolean condition(Entity entity);

	public boolean init(Entity entity);

	public boolean call(Entity entity);

	public void finish(Entity entity);
}
