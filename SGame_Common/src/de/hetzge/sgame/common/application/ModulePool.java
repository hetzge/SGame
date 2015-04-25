package de.hetzge.sgame.common.application;

import java.util.Set;

import javolution.util.FastSet;
import de.hetzge.sgame.common.definition.IF_Module;

public class ModulePool {

	private final FastSet<IF_Module> modules = new FastSet<>();

	public void registerModule(IF_Module module) {
		this.modules.add(module);
	}

	public void registerModules(IF_Module... modules) {
		for (IF_Module if_Module : modules) {
			this.registerModule(if_Module);
		}
	}

	public Set<IF_Module> getModules() {
		return this.modules.unmodifiable();
	}

	public void init() {
		for (IF_Module if_Module : this.modules) {
			if_Module.init();
		}
	}

	public void postInit() {
		for (IF_Module if_Module : this.modules) {
			if_Module.postInit();
		}
	}

	public void update() {
		for (IF_Module if_Module : this.modules) {
			if_Module.update();
		}
	}
}
