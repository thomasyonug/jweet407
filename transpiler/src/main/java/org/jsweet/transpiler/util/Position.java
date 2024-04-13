/* 
 * JSweet transpiler - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * aint with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.jsweet.transpiler.util;

/**
 * Represents a generic immutable position.
 * 
 * @author Renaud Pawlak
 */
public final class Position implements Comparable<Position> {
	private final int position;
	private final int line;
	private final int column;

	/**
	 * Creates a new position.
	 */
	public Position(int position, int line, int column) {
		super();
		this.position = position;
		this.line = line;
		this.column = column;
	}

	/**
	 * Creates a new position.
	 */
	public Position(int line, int column) {
		this(-1, line, column);
	}

	/**
	 * Creates a new position.
	 */
	public Position(Position position) {
		this(position.position, position.line, position.column);
	}

	@Override
	public int compareTo(Position position) {
		if (this.line != position.line) {
			return (this.line - position.line);
		} else {
			return (this.column - position.column);
		}
	}

	@Override
	public String toString() {
		return "(" + line + "," + column + ")[" + position + "]";
	}

	/**
	 * The position in the stream (-1 if not used).
	 */
	public final int getPosition() {
		return position;
	}

	/**
	 * The position's line.
	 */
	public final int getLine() {
		return line;
	}

	/**
	 * The position's column.
	 */
	public final int getColumn() {
		return column;
	}
}
