package de.hetzge.sgame.application;

import java.util.HashSet;
import java.util.Set;

import de.hetzge.sgame.common.definition.IF_Module;

public class ModulePool {

	private final Set<IF_Module> modules = new HashSet<>();

	public void registerModule(IF_Module module) {
		this.modules.add(module);
	}

	public void registerModules(IF_Module... modules) {
		for (IF_Module if_Module : modules) {
			this.registerModule(if_Module);
		}
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
