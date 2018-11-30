package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.AdjacencyListGraph;

public class SFG {
	private Graph graph;
	private Map<Path, Integer> forwardPaths;
	private Map<Path, Integer> loops;
	private Map<Path, Integer> pathsDeltas;
	private List<ArrayList<ArrayList<Path>>> untouchedLoopsCombinations;

	public SFG(Graph graph) {
		this.graph = graph;
		forwardPaths = new HashMap<>();
		loops = new HashMap<>();
		pathsDeltas = new HashMap<>();
		untouchedLoopsCombinations = new ArrayList<>();
	}

	public Map<Path, Integer> getLoops() {
		if (loops.isEmpty()) {
			computeLoops();
			removeRedundantLoops();
		}

		return loops;
	}

	public Map<Path, Integer> getForwardPaths(Node startNode, Node endNode) {
		if (forwardPaths.isEmpty())
			computeForwardPaths(startNode, endNode);

		return forwardPaths;
	}

	public Integer computeDelta() {
		int delta = 1;
		List<Integer> allLoopsGains = getAllLoopsGains();

		for (int i = 0; i < allLoopsGains.size(); i++) {
			if (i % 2 == 0) {
				delta -= allLoopsGains.get(i);
			} else
				delta += allLoopsGains.get(i);
		}
		return delta;
	}

	public Float computeTransferFunction(Node startNode, Node endNode) {
		getPathsDeltas(startNode, endNode);

		float transferFunction = 0;
		for (Path forwardPath : forwardPaths.keySet()) {
			transferFunction += forwardPaths.get(forwardPath)
					* pathsDeltas.get(forwardPath);
		}

		return transferFunction / computeDelta();
	}

	public Map<Path, Integer> getPathsDeltas(Node startNode, Node endNode) {
		if (pathsDeltas.isEmpty())
			computePathsDeltas(startNode, endNode);

		return pathsDeltas;
	}

	public List<ArrayList<ArrayList<Path>>> getUntouchedLoopsCombinations() {
		if (untouchedLoopsCombinations.isEmpty())
			computeUntouchedLoopsCombinations();
		return untouchedLoopsCombinations;
	}

	private void computePathsDeltas(Node startNode, Node endNode) {
		getForwardPaths(startNode, endNode);

		for (Path forwardPath : forwardPaths.keySet()) {
			Graph graph = makeCopy(this.graph);

			pathsDeltas.put(forwardPath, computePathDelta(forwardPath, graph));
		}
	}

	private void computeForwardPaths(LinkedList<Node> visited, Node destination) {
		List<Node> outNeighbors = getOutNeighbours(visited.getLast());

		for (Node node : outNeighbors) {
			// if one of the neighbors is not visited and
			// destination then we have a path
			if (!visited.contains(node) && node.equals(destination)) {
				visited.add(node);
				createPath(new LinkedList<>(visited), forwardPaths);
				visited.removeLast();
				break;
			}
		}

		for (Node node : outNeighbors) {
			if (!(visited.contains(node) || node.equals(destination))) {
				visited.addLast(node);
				computeForwardPaths(visited, destination);
				visited.removeLast();
			}
		}
	}

	private void computeLoops(LinkedList<Node> visited, Node destination) {
		List<Node> outNeighbors = getOutNeighbours(visited.getLast());

		for (Node node : outNeighbors) {
			// if one of the neighbors is not visited and
			// destination then we have a path
			if (visited.contains(node) && node.equals(destination)) {
				visited.add(node);
				createPath(new LinkedList<>(visited), loops);
				visited.removeLast();
				break;
			}
		}

		// do a depth first until we reach destination
		for (Node node : outNeighbors) {
			if (!(visited.contains(node) || node.equals(destination))) {
				visited.addLast(node);
				computeLoops(visited, destination);
				visited.removeLast();
			}
		}
	}

	private void computeLoops() {
		for (Node node : graph.getEachNode()) {
			LinkedList<Node> visited = new LinkedList<>();
			visited.add(node);
			computeLoops(visited, node);
		}
	}

	private void removeRedundantLoops() {
		List<Path> newLoops = new ArrayList<>();

		for (Path loop : loops.keySet()) {
			boolean isUnique = true;
			for (int i = 0; i < newLoops.size(); i++) {
				if (loop.size() == newLoops.get(i).size()
						&& areRedundants(newLoops.get(i), loop)) {

					isUnique = false;
					if (newLoops.get(i).toString().compareTo(loop.toString()) > 0) {
						newLoops.set(i, loop);
						break;
					}
				}
			}
			if (isUnique)
				newLoops.add(loop);
		}

		// Delete redundant loops from map"loops"
		List<Path> redundantLoops = new ArrayList<>();
		for (Path loop : loops.keySet()) {
			if (!newLoops.contains(loop))
				redundantLoops.add(loop);
		}

		loops.keySet().removeAll(redundantLoops);
	}

	private boolean areRedundants(Path loop1, Path loop2) {
		List<Node> nodes1 = loop1.getNodePath();
		List<Node> nodes2 = loop2.getNodePath();
		int count = 0;

		if (nodes1.size() == nodes2.size()) {
			for (int i = 0; i < nodes1.size() - 1; i++) {
				for (int j = 0; j < nodes2.size() - 1; j++) {
					if (nodes1.get(i) == nodes2.get(j)) {
						count++;
						break;
					}
				}
			}
		}

		return count == nodes1.size() - 1;
	}

	private void computeForwardPaths(Node startNode, Node endNode) {
		LinkedList<Node> visited = new LinkedList<>();
		visited.add(startNode);
		computeForwardPaths(visited, endNode);
	}

	private List<Node> getOutNeighbours(Node node) {
		List<Node> nodes = new ArrayList<>();

		for (Edge edge : node.getLeavingEdgeSet()) {
			nodes.add(edge.getTargetNode());
		}
		return nodes;
	}

	private List<ArrayList<Path>> getUntouchedLoopsSets() {
		List<ArrayList<Path>> untouchedLoopsSets = new ArrayList<>();

		List<Path> loops = new ArrayList<>(getLoops().keySet());

		// All untouched loops
		for (int i = 0; i < loops.size(); i++) {
			ArrayList<Path> untouchedLoopsSet = new ArrayList<>();

			for (int j = 0; j < loops.size(); j++) {
				if (i != j && !isTouching(loops.get(i), loops.get(j))) {
					boolean isTouching = false;
					for (Path loop : untouchedLoopsSet) {
						if (isTouching(loop, loops.get(j))) {
							isTouching = true;
							break;
						}
					}
					if (!isTouching)
						untouchedLoopsSet.add(loops.get(j));
				}
			}

			untouchedLoopsSet.add(loops.get(i));

			for (ArrayList<Path> set : untouchedLoopsSets) {
				if (set.containsAll(untouchedLoopsSet)) {
					untouchedLoopsSet.clear();
					break;
				}
			}
			if (!untouchedLoopsSet.isEmpty())
				untouchedLoopsSets.add(untouchedLoopsSet);
		}
		return untouchedLoopsSets;
	}

	private void computeUntouchedLoopsCombinations() {
		List<ArrayList<Path>> untouchedLoopsSets = getUntouchedLoopsSets();

		for (int x = 2; x <= getMaxCombination(untouchedLoopsSets); x++) {
			ArrayList<ArrayList<Path>> combinations = new ArrayList<>();

			for (List<Path> loopsSet : untouchedLoopsSets) {
				if (loopsSet.size() >= x)
					for (int i = 0; i < loopsSet.size() - x + 1; i++) {
						for (int j = i + 1; j < loopsSet.size()
								&& j - 1 + x <= loopsSet.size(); j++) {
							ArrayList<Path> combination = new ArrayList<>();
							combination.add(loopsSet.get(i));

							combination.addAll(loopsSet.subList(j, j - 1 + x));

							Collections.sort(combination, new comparator());

							if (!combinations.contains(combination))
								combinations.add(combination);
						}
					}
			}
			untouchedLoopsCombinations.add(combinations);
		}
	}

	private int getMaxCombination(List<ArrayList<Path>> untouchedLoopsSets) {
		int max = 0;
		for (List<Path> set : untouchedLoopsSets) {
			max = Math.max(set.size(), max);
		}
		return max;
	}

	private class comparator implements Comparator<Path> {
		@Override
		public int compare(Path o1, Path o2) {
			String s1 = o1.toString();
			String s2 = o2.toString();

			return s1.compareTo(s2);
		}
	}

	private boolean isTouching(Path firstLoop, Path secondLoop) {
		if (firstLoop.size() == Math.max(firstLoop.size(), secondLoop.size())) {
			for (Node node : secondLoop.getNodeSet()) {
				if (firstLoop.contains(node))
					return true;
			}
			return false;
		}
		for (Node node : firstLoop.getNodeSet()) {
			if (secondLoop.contains(node))
				return true;
		}
		return false;
	}

	private List<Integer> getAllLoopsGains() {
		getUntouchedLoopsCombinations();
		ArrayList<Integer> gains = new ArrayList<>();

		Integer gainsSum = 0;
		for (Path p : loops.keySet()) {
			gainsSum += computeGain(p);
		}
		gains.add(gainsSum);

		for (ArrayList<ArrayList<Path>> l1 : untouchedLoopsCombinations) {
			Integer sumOfProducts = 0;
			for (ArrayList<Path> l2 : l1) {
				Integer productGain = 1;
				for (Path p : l2) {
					productGain *= computeGain(p);
				}
				sumOfProducts += productGain;
			}
			gains.add(sumOfProducts);
		}

		return gains;
	}

	private int computePathDelta(Path path, Graph graph) {
		for (Node node : path.getNodeSet()) {
			graph.removeNode(node.getId());
		}
		return new SFG(graph).computeDelta();
	}

	private Graph makeCopy(Graph graph) {
		Graph newGraph = new AdjacencyListGraph("");

		for (Node node : graph.getNodeSet()) {
			newGraph.addNode(node.getId());
			newGraph.getNode(node.getId()).addAttribute("ui.label", node.getId());
		}

		for (Edge edge : graph.getEdgeSet()) {
			newGraph.addEdge(edge.getId(), edge.getSourceNode().getId(),
					edge.getTargetNode().getId(), true);
			newGraph.getEdge(edge.getId()).addAttribute("weight",
					(Integer) edge.getAttribute("weight"));
		}
		return newGraph;
	}

	private void createPath(LinkedList<Node> nodes, Map<Path, Integer> paths) {
		Path path = new Path();

		for (Node node : nodes) {
			path.getNodePath().add(node);
		}

		while (nodes.size() > 1) {
			Node node1 = nodes.pop();
			Node node2 = nodes.peek();
			for (Edge edge : graph.getEdgeSet()) {
				if (node1 == edge.getSourceNode() && node2 == edge.getTargetNode()) {
					path.getEdgePath().add(edge);
				}
			}
		}

		paths.put(path, computeGain(path));
	}

	private Integer computeGain(Path path) {
		Integer gain = 1;
		for (Edge e : path.getEdgePath()) {
			gain *= e.getAttribute("weight", Integer.class);
		}
		return gain;
	}

}