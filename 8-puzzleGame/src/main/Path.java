package main;

import java.util.LinkedList;

public class Path {
	private LinkedList<State> stateList;

	public Path(State start) {
		this.stateList = new LinkedList<State>();
		this.stateList.add(start);
	}

	public Path(LinkedList<State> stateList) {
		this.stateList = stateList;
	}

	public Path(Path path) {
		this.stateList = new LinkedList<State>(path.stateList);
	}

	public void addState(State arg0) {
		this.stateList.add(arg0);
	}

	public State lastState() {
		return this.stateList.getLast();
	}

	public LinkedList<State> getStates() {
		return stateList;
	}

	@Override
	public boolean equals(Object obj) {
		return this.stateList.getLast().equals(((Path) obj).stateList.getLast());
	}
}
