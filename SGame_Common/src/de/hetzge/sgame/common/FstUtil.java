package de.hetzge.sgame.common;

public final class FstUtil {

	private FstUtil() {
	}

	public static void registerClass(Class<?>... clazz) {
		for (Class<?> class1 : clazz) {
			CommonConfig.INSTANCE.fst.registerClass(clazz);
		}
	}

}
