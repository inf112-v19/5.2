package inf112.skeleton.app.GameMechanics;

import java.util.Objects;

public class Position {
	private final int x;
	private final int y;

	/**
	 *
	 * @param x coordinate
	 * @param y coordinate
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 *
	 * @return position coordinates in array form [x, y]
	 */
	public int[] getPosition() {
		return new int[]{this.x, this.y};
	}

	/**
	 *
	 * @return x coordinate
	 */
	public int getX() {
		return this.x;
	}

	/**
	 *
	 * @return y coodinate
	 */
	public int getY() {
		return this.y;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return x == position.x &&
				y == position.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}