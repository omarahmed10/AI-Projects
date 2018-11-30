package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class UserInterface {
	private char currentNodeID;
	private Integer currentEdgeID;
	private Graph graph;
	private Viewer viewer;
	private ViewPanel panel;
	private SFG sfg;

	public UserInterface() {
		currentNodeID = 'A';
		currentEdgeID = 1;
		System.setProperty("org.graphstream.ui.renderer",
				"org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		graph = new SingleGraph("SFG");
		graph.addAttribute("ui.stylesheet",
				"edge { arrow-shape: arrow; " + "fill-color: black; }");
		graph.addAttribute("ui.stylesheet", "node { text-alignment: under; "
				+ "text-offset: 0px, 4px; text-color: black;}");

		viewer = graph.display();

		panel = viewer.getDefaultView();
		viewer.addView(panel);

		sfg = new SFG(graph);

		addNodeButton();
		addEdgeButton();
		removeNodeButton();
		removeEdgeButton();
		operationsButton();
	}

	private void addNodeButton() {
		JButton addNode = new JButton("AddNode");

		addNode.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {
				String label = JOptionPane.showInputDialog("Enter Node Label",
						currentNodeID);

				String[] colors = { "black", "red", "yellow", "green", "blue", "orange",
						"brown" };

				JComboBox<Object> colorsCombo = new JComboBox<>(colors);

				// Node's color dialog
				int option = JOptionPane.showOptionDialog(null, colorsCombo,
						"Node's Color Choice", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, null);

				// label should not be null && since we will take first char only , it
				// should not be a white space
				if (label != null && label.charAt(0) != ' ') {
					graph.addNode(label);
					graph.getNode(label).addAttribute("ui.label", label);

					if (option != JOptionPane.CLOSED_OPTION) {
						graph.getNode(label).addAttribute("ui.style",
								"fill-color: " + colorsCombo.getSelectedItem() + ";");
					} else {
						graph.getNode(label).addAttribute("ui.style", "fill-color: black;");
					}

					currentNodeID++;
				}
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		panel.add(addNode);
	}

	private void removeNodeButton() {
		JButton removeNode = new JButton("RemoveNode");

		removeNode.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {

				JComboBox<Object> comboBox = new JComboBox<>(
						graph.getNodeSet().toArray());

				// Node dialog
				int option = JOptionPane.showOptionDialog(null, comboBox,
						"Remove node Choice", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, null);

				Node node = (Node) comboBox.getSelectedItem();

				// If any input cancelled , then whole operation also cancelled
				if (option != JOptionPane.CLOSED_OPTION) {
					graph.removeNode(node);
				}
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		panel.add(removeNode);
	}

	private void addEdgeButton() {
		JButton addEdge = new JButton("AddEdge");

		addEdge.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Choose two nodes");

				JComboBox<Object> comboBox = new JComboBox<>(
						graph.getNodeSet().toArray());

				// First node dialog
				int option = JOptionPane.showOptionDialog(null, comboBox,
						"Start node Choice", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, null);

				Node startNode = (Node) comboBox.getSelectedItem();

				// If any input cancelled , then whole operation also cancelled
				if (option != JOptionPane.CLOSED_OPTION) {
					// Second node dialog
					comboBox.setSelectedIndex(comboBox.getSelectedIndex() + 1);

					option = JOptionPane.showOptionDialog(null, comboBox,
							"End node Choice", JOptionPane.DEFAULT_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, null, null);

					Node endNode = (Node) comboBox.getSelectedItem();

					// If any input cancelled , then whole operation also cancelled
					if (option != JOptionPane.CLOSED_OPTION) {
						String gainInput = JOptionPane.showInputDialog("Enter Gain", 1);

						Integer gain;
						try {
							gain = Integer.valueOf(gainInput);

							graph.addEdge(currentEdgeID.toString(), startNode.getId(),
									endNode.getId(), true);

							graph.getEdge(currentEdgeID.toString()).addAttribute("ui.label",
									startNode.toString() + endNode + " = " + gain);

							graph.getEdge(currentEdgeID.toString()).addAttribute("weight",
									gain);

							currentEdgeID++;

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Error in edge");
							e2.printStackTrace();
						}

					} else {
						JOptionPane.showMessageDialog(null, "Error in nodes choosing");
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		panel.add(addEdge);
	}

	private void removeEdgeButton() {
		JButton removeEdge = new JButton("RemoveEdge");

		removeEdge.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {

				JComboBox<Object> comboBox = new JComboBox<>(
						graph.getEdgeSet().toArray());

				// Node dialog
				int option = JOptionPane.showOptionDialog(null, comboBox,
						"Remove Edge Choice", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, null);

				Edge edge = (Edge) comboBox.getSelectedItem();

				// If any input cancelled , then whole operation also cancelled
				if (option != JOptionPane.CLOSED_OPTION) {
					graph.removeEdge(edge);
				}
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		panel.add(removeEdge);
	}

	private void operationsButton() {
		JButton operations = new JButton("Operations");

		operations.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {

				JOptionPane.showMessageDialog(null, "Choose operation");

				String[] operations = new String[] { "Transfer Function",
						"Forward Paths", "Loops", "Untouched loops combinations", "Delta",
						"Forward paths deltas" };

				JComboBox<Object> comboBox = new JComboBox<>(operations);

				// Operations dialog
				int option = JOptionPane.showOptionDialog(null, comboBox,
						"Choose operation", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, null);

				int index = comboBox.getSelectedIndex();

				// If any input cancelled , then whole operation also cancelled
				if (option != JOptionPane.CLOSED_OPTION) {
					JOptionPane.showMessageDialog(null, "Choose two nodes");

					comboBox = new JComboBox<>(graph.getNodeSet().toArray());

					// First node dialog
					option = JOptionPane.showOptionDialog(null, comboBox,
							"Choose start node", JOptionPane.DEFAULT_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, null, null);

					Node startNode = (Node) comboBox.getSelectedItem();

					// If any input cancelled , then whole operation also cancelled
					if (option != JOptionPane.CLOSED_OPTION) {
						// Second node dialog
						comboBox.setSelectedIndex(graph.getNodeCount() - 1);

						option = JOptionPane.showOptionDialog(null, comboBox,
								"End node Choice", JOptionPane.DEFAULT_OPTION,
								JOptionPane.PLAIN_MESSAGE, null, null, null);

						Node endNode = (Node) comboBox.getSelectedItem();

						// If any input cancelled , then whole operation also cancelled
						if (option != JOptionPane.CLOSED_OPTION) {
							String operation;

							if (index == 0) {
								operation = sfg.computeTransferFunction(startNode, endNode)
										.toString();
							} else if (index == 1) {
								operation = sfg.getForwardPaths(startNode, endNode).toString();
							} else if (index == 2) {
								operation = sfg.getLoops().toString();
							} else if (index == 3) {
								operation = sfg.getUntouchedLoopsCombinations().toString();
							} else if (index == 4) {
								operation = sfg.computeDelta().toString();
							} else {
								operation = sfg.getPathsDeltas(startNode, endNode).toString();
							}

							JOptionPane.showMessageDialog(null, operation, operations[index],
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "Error in nodes choosing");
						}
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		panel.add(operations);
	}

	public static void main(String[] args) {
		new UserInterface();
	}

}