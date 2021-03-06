package de.hetzge.sgame.map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.hetzge.sgame.common.BaseCollisionImpl;
import de.hetzge.sgame.common.Orientation;
import de.hetzge.sgame.common.definition.IF_Collision;

public enum TileOrientation implements Serializable {

	/**
	 * XXX XXX XXX
	 */
	FULL("XXXXXXXXX"),
	/**
	 * OOO OXO OOO
	 */
	CENTER("OOOOXOOOO"),
	/**
	 * OXO XXX OXO
	 */
	CROSS("OXOXXXOXO"),
	/**
	 * XXO XXO XXO
	 */
	HALF_LEFT("XXOXXOXXO"),
	/**
	 * OXX OXX OXX
	 */
	HALF_RIGHT("OXXOXXOXX"),
	/**
	 * XXX XXX OOO
	 */
	HALF_UP("XXXXXXOOO"),
	/**
	 * OOO XXX XXX
	 */
	HALF_DOWN("OOOXXXXXXXXX"),
	/**
	 * OXO XXX XXX
	 */
	HALF_DOWN_WAY_UP("OXOXXXXXX"),
	/**
	 * XXX XXX OXO
	 */
	HALF_UP_WAY_DOWN("XXXXXXOXO"),
	/**
	 * XXO XXX XXO
	 */
	HALF_LEFT_WAY_RIGHT("XXOXXXXXO"),
	/**
	 * OXX XXX OXX
	 */
	HALF_RIGHT_WA_LEFT("OXXXXXOXX"),
	/**
	 * OOO XOO XXO
	 */
	QUATER_LEFT_DOWN("OOOXOOXXO"),
	/**
	 * XXO XOO OOO
	 */
	QUATER_LEFT_UP("XXOXOOOOO"),
	/**
	 * OXX OOX OOO
	 */
	QUATER_RIGHT_UP("OXXOOXOOO"),
	/**
	 * OOO OOX OXX
	 */
	QUATER_RIGHT_DOWN("OOOOOXOXX"),
	/**
	 * OXO OXO OXO
	 */
	WAY_UP_DOWN("OXOOXOOXO"),
	/**
	 * OOO XXX OOO
	 */
	WAY_LEFT_RIGHT("OOOXXXOOO"),
	/**
	 * OXO XXO OOO
	 */
	WAY_LEFT_UP("OXOXXOOOO"),
	/**
	 * OOO XXO OXO
	 */
	WAY_LEFT_DOWN("OOOXXOOXO"),
	/**
	 * OXO OXX OOO
	 */
	WAY_RIGHT_UP("OXOOXXOOO"),
	/**
	 * OOO OXX OXO
	 */
	WAY_RIGHT_DOWN("OOOOXXOXO"),
	/**
	 * OOO XXO OOO
	 */
	WAY_LEFT_END("OOOXXOOOO"),
	/**
	 * OOO OXX OOO
	 */
	WAY_RIGHT_END("OOOOXXOOO"),
	/**
	 * OXO OXO OOO
	 */
	WAY_UP_END("OXOOXOOOO"),
	/**
	 * OOO OXO OXO
	 */
	WAY_DOWN_END("OOOOXOOXO"),
	/**
	 * OXX XXX XXX
	 */
	QUATER_LEFT_UP_OPPOSIT("OXXXXXXXX"),
	/**
	 * XXX XXX OXX
	 */
	QUATER_LEFT_DOWN_OPPOSIT("XXXXXXOXX"),
	/**
	 * XXO XXX XXX
	 */
	QUATER_RIGHT_UP_OPPOSIT("XXOXXXXXX"),
	/**
	 * XXX XXX XXO
	 */
	QUATER_RIGHT_DOWN_OPPOSIT("XXXXXXXXO");

	public static final char CHAR_1 = 'X';
	public static final char CHAR_2 = 'O';

	public static final Map<Orientation, Integer[]> EDGES = new HashMap<Orientation, Integer[]>() {
		{
			this.put(Orientation.NORTH, new Integer[] { 0, 1, 2 });
			this.put(Orientation.SOUTH, new Integer[] { 6, 7, 8 });
			this.put(Orientation.WEST, new Integer[] { 0, 3, 6 });
			this.put(Orientation.EAST, new Integer[] { 2, 5, 8 });
		}
	};

	public final String rules;

	private TileOrientation(String rules) {
		this.rules = rules;
	}

	public int calculateSideLength() {
		return (int) Math.sqrt(this.rules.length());
	}

	public boolean isX(int index) {
		return this.rules.charAt(index) == TileOrientation.CHAR_1;
	}

	public boolean isX(int x, int y) {
		return this.isX(x + y * this.calculateSideLength());
	}

	public IF_Collision asCollision() {
		BaseCollisionImpl collision = new BaseCollisionImpl(this.calculateSideLength(), this.calculateSideLength());
		for (int i = 0; i < this.rules.length(); i++) {
			if (this.isX(i)) {
				collision.setCollision(i % this.calculateSideLength(), i - (i % this.calculateSideLength()) / this.calculateSideLength(), true);
			}
		}
		return collision;
	}
}
