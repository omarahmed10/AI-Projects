package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.graphstream.graph.Graph;

public class Toolbar extends JToolBar {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Choose which Shape to be drawn.
	 */
	private JButton shapeButton;
	/**
	 * erase part of the Shape and replace it with the background color.
	 */
	private JButton moveButton;
	/**
	 * draw a free form line
	 */
	private JButton penButton;
	/**
	 *
	 */
	private JButton undoButton;
	/**
	 *
	 */
	private JButton redoButton;
	private JButton circleButton, ellipseButton, lineButton,
			rectangleButton, squareButton, triangleButton,
			fillButton, selectButton, deleteButton;

	private boolean editState;

	public Toolbar(Graph graph) {
		penButton = createButtons("/images/pen.png", true,
				"pen");
		penButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graph.getNode("1").changeAttribute("ui.label", "youu");
			}
		});
		rectangleButton = createButtons(
				"/images/rectangle.png", true, "rectangle");
		squareButton = createButtons("/images/square.png",
				true, "square");
		ellipseButton = createButtons("/images/ellipse.png",
				true, "ellipse");
		circleButton = createButtons("/images/circle.png",
				true, "circle");
		lineButton = createButtons("/images/line.png", true,
				"line");
		triangleButton = createButtons(
				"/images/triangle.png", true, "triangle");
		undoButton = createButtons("/images/undo.png", true,
				"undo");
		redoButton = createButtons("/images/redo.png", true,
				"redo");
		fillButton = createButtons("/images/fill.png", true,
				"fill");
		selectButton = createButtons("/images/select.png",
				true, "resize");
		moveButton = createButtons("/images/move.png", true,
				"move");
		deleteButton = createButtons("/images/del.png",
				true, "delete");
		
		setBackground(Color.BLACK);
		setOrientation(SwingConstants.VERTICAL);
	}

	private JButton createButtons(String imagePath,
			boolean state, String text) {
		JButton b = new JButton();
		b.setIcon(createIcon(imagePath));
		b.setVisible(state);
		b.setToolTipText(text);
		add(b);
		add(Box.createRigidArea(new Dimension(0, 6)));
		return b;
	}

	private ImageIcon createIcon(String Path) {
		URL url = getClass().getResource(Path);
		if (url == null) {
			throw new RuntimeException();
		}
		ImageIcon icon = new ImageIcon(url);

		return icon;
	}

}
