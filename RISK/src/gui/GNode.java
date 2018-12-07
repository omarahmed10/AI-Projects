package gui;
import org.graphstream.graph.*;

import map.Territory;

public class GNode {
	protected Node graphNode;
	protected Territory territory;
	
	public GNode(Node graphNode,Territory territory) {
		this.graphNode = graphNode;
		this.territory = territory;
	}
}
