package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {
	public static int SIZE = 3;
	private int[][] puzzle;
	private List<State> neighbors;
	private float cost;

	private State(int[][] puzzle) {
		this.puzzle = puzzle;
	}

	public State(int[] sequence) {
		puzzle = new int[3][3];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				puzzle[i][j] = sequence[j + SIZE * i];
			}
		}
	}

	public List<State> getNeighbors() {
		neighbors = new ArrayList<State>();
		Point blank = getBlankPosition();
		if (blank == null)
			return neighbors;
		if (blank.x + 1 < SIZE) {
			neighbors.add(new State(move(blank, puzzle, 1, 0)));
		}
		if (blank.x - 1 >= 0) {
			neighbors.add(new State(move(blank, puzzle, -1, 0)));
		}
		if (blank.y + 1 < SIZE) {
			neighbors.add(new State(move(blank, puzzle, 0, 1)));
		}
		if (blank.y - 1 >= 0) {
			neighbors.add(new State(move(blank, puzzle, 0, -1)));
		}
		return neighbors;
	}

	private int[][] clone(int[][] puzzle) {
		int[][] newPuzzle = new int[SIZE][SIZE];
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle[i].length; j++) {
				newPuzzle[i][j] = puzzle[i][j];
			}
		}
		return newPuzzle;
	}

	private int[][] move(Point blank, int[][] puzzle, int x, int y) {
		int temp = puzzle[blank.x][blank.y];
		int[][] newPuzzle = clone(puzzle);
		newPuzzle[blank.x][blank.y] = newPuzzle[blank.x + x][blank.y + y];
		newPuzzle[blank.x + x][blank.y + y] = temp;
		return newPuzzle; // because java pass by value :)
	}

	private Point getBlankPosition() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (puzzle[i][j] == 0) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) {
			return false;
		}
		return Arrays.deepEquals(this.puzzle, ((State) arg0).puzzle);
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				s += puzzle[i][j] + " ";
			}
			s += i < SIZE ? "\n" : "";
		}
		return s;
	}

	public int[][] getPuzzle() {
		return puzzle;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getCost() {
		return cost;
	}
}
