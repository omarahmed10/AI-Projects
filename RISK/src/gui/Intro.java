package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Intro extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame parent;

	public Intro(JFrame parent) {
		setLayout(new BorderLayout());
		this.parent = parent;
		// A "message line" to display results in
		final JLabel msgline = new JLabel(" ");
		Map<String, Integer> agentIDS = new HashMap<>();
		agentIDS.put("Aggresive", 0);
		agentIDS.put("Astar", 1);
		agentIDS.put("Greedy", 2);
		agentIDS.put("Human", 3);
		agentIDS.put("Pacifist", 4);
		agentIDS.put("RTAstar", 5);
		agentIDS.put("Passive", 6);
		String[] argsWithAI = new String[] { "Passive", "Aggresive", "Human", "Pacifist", "Greedy", "Astar",
				"RTAstar" };
		String[] argsWithoutAI = new String[] { "Passive", "Aggresive", "Human", "Pacifist" };
		// Create a panel holding three ItemChooser components
		final Starter c1 = new Starter("Player #1", argsWithAI, null, 0, null);
		final Starter c2 = new Starter("Player #2", argsWithoutAI, null, 0, c1);
		c1.setOtherPlayer(c2);

		// An event listener that displays changes on the message line
		Starter.Listener l = new Starter.Listener() {
			@Override
			public void itemChosen(Starter.Event e) {
				msgline.setText(
						e.getItemChooser().getName() + ": " + e.getSelectedIndex() + ": " + e.getSelectedValue());
			}
		};

		c1.addItemChooserListener(l);
		c2.addItemChooserListener(l);

		// Instead of tracking every change with a ItemChooser.Listener,
		// applications can also just query the current state when
		// they need it. Here's a button that does that.
		JButton report = new JButton("Reset Game");
		report.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Note the use of multi-line italic HTML text
				// with the JOptionPane message dialog box.
				String msg = "<html><i>" + c1.getName() + ": " + c1.getSelectedValue() + "<br>" + c2.getName() + ": "
						+ c2.getSelectedValue() + "</i>";
				long threadId = Thread.currentThread().getId();
				System.out.println("Thread # " + threadId + " is in action listener");
				JOptionPane.showMessageDialog(parent, msg);
				restartGame(agentIDS.get(c1.getSelectedValue()), agentIDS.get(c2.getSelectedValue()));
			}
		});

		add(report, BorderLayout.SOUTH);
		add(c1, BorderLayout.WEST);
		add(c2, BorderLayout.EAST);
	}

	private void restartGame(Object object, Object object2) {
	}
}
