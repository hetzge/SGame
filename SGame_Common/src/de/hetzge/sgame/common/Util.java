package de.hetzge.sgame.common;

import de.hetzge.sgame.common.geometry.IF_ImmutablePosition;

public final class Util {

	private Util() {
	}

	public static float calculateDistance(IF_ImmutablePosition<?> position, IF_ImmutablePosition<?> otherPosition) {
		float a = Math.abs(position.getX() - otherPosition.getX());
		float b = Math.abs(position.getY() - otherPosition.getY());
		return (float) Math.sqrt(a * a + b * b);
	}

	public static void sleep(long timeInMs) {
		try {
			Thread.sleep(timeInMs);
		} catch (InterruptedException e) {
		}
	}

	public static String toMapString(int[][] array, int markX, int markY) {
		if (array.length == 0)
			return "";
		if (array[0].length == 0)
			return "";

		int[] maxLengthPerColumn = new int[array.length];
		for (int x = 0; x < array.length; x++) {
			for (int y = 0; y < maxLengthPerColumn.length; y++) {
				maxLengthPerColumn[x] = maxLengthPerColumn[x] < String.valueOf(array[x][y]).length() ? String.valueOf(array[x][y]).length() : maxLengthPerColumn[x];
			}
		}

		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < array[0].length; y++) {
			for (int x = 0; x < array.length; x++) {
				if (x == markX && y == markY) {
					builder.append('[');
				} else {
					builder.append(' ');
				}
				String current = String.valueOf(array[x][y]);
				builder.append(current);
				for (int i = current.length(); i < maxLengthPerColumn[x]; i++) {
					builder.append(' ');
				}
				if (x == markX && y == markY) {
					builder.append(']');
				} else {
					builder.append(' ');
				}
				builder.append('|');
			}
			builder.append("\n");
		}

		return builder.toString();
	}

	public static void main(String[] args) {
		int[][] array = { { 1, 2, 3 }, { 1, 211, 3 }, { 100, 254454654, 3 } };
		String mapString = Util.toMapString(array, 2, 2);
		System.out.println(mapString);
	}
}
