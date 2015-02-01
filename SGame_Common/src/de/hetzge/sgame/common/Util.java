package de.hetzge.sgame.common;

import java.util.ArrayList;

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

	public static Integer[] valuesInsideOut(int start, int end) {
		if (start == end) {
			return new Integer[] { start };
		}

		ArrayList<Integer> result = new ArrayList<Integer>();
		int range = Math.abs(start - end);
		boolean even = range % 2 == 0;
		int halfRange = (int) Math.floor(range / 2f);
		int center = start + halfRange;

		if (even) {
			for (int i = 0; i < halfRange; i++) {
				result.add(center + (even ? -1 : 0) - i);
				result.add(center + (even ? 0 : 1) + i);
			}
		} else {
			result.add(center);
			for (int i = 1; i <= halfRange; i++) {
				result.add(center + (even ? -1 : 0) - i);
				result.add(center + (even ? 0 : 1) + i);
			}
		}

		return result.toArray(new Integer[result.size()]);
	}

	public static String toMapString(int[][] array, int markX, int markY) {
		if (array.length == 0) {
			return "";
		}
		if (array[0].length == 0) {
			return "";
		}

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
		Integer[] valuesInsideOut = Util.valuesInsideOut(20, 15);
		for (Integer integer : valuesInsideOut) {
			System.out.println(integer);
		}
	}
}
