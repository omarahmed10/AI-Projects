package main;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		int[] testSeq = { 1, 2, 5, 3, 4, 0, 6, 7, 8 };
		State parent = new State(testSeq);
		List<State> n = parent.getNeighbors();
		for(State s: n) {
			System.out.print(s);
			System.out.println("===========================");
		}
	}
}
