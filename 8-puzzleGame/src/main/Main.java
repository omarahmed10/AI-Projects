package main;

import java.util.Arrays;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		int[] testSeq = { 1, 2, 5, 3, 4, 0, 6, 7, 8 };
		int[] testSeq1 = { 1, 2, 5, 3, 4, 0, 6, 7, 8 };
		State parent = new State(testSeq);
		List<State> n = parent.getNeighbors();
		for (State s : n) {
			System.out.print(s);
			System.out.println("===========================");
		}

		State s1 = new State(testSeq);
		State s2 = new State(testSeq);

		System.out.println(s1.equals(s2));
		System.out.println(Arrays.equals(testSeq,testSeq1));
	}
}
