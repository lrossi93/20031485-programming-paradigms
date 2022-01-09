package utils;

import java.io.Serializable;

/**
 * Class for representing a couple of coordinates as objects.
 * @author 20027017 & 20031485
 *
 */
public class Coordinates implements Serializable{
	private static final long serialVersionUID = 1L;

	private int row;
	private int column;
	
	/**
	 * Constructor for a {@link Coordinates} object.
	 * @param row The row coordinate.
	 * @param column The column coordinate.
	 */
	public Coordinates(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Getter for the row of the {@link Coordinates} object.
	 * @return Integer representing the row of the {@link Coordinates} object.
	 */
	public int getRow() {
		return this.row;
	}
	
	/**
	 * Getter for the column of the {@link Coordinates} object.
	 * @return Integer representing the column of the {@link Coordinates} object.
	 */
	public int getColumn() {
		return this.column;
	}
}
