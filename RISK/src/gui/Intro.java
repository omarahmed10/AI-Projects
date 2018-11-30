package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Intro extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame parent;

	public Intro(JFrame parent) {
		
		this.parent = parent;
		// A "message line" to display results in
		final JLabel msgline = new JLabel(" ");
		String[] args = new String[] { "Passive", "Aggresive", "Human", "Pacifist", "Greedy", "Astar", "RTAstar" };
		// Create a panel holding three ItemChooser components
		final Starter c1 = new Starter("Player #1", args, null, 0, null);
		final Starter c2 = new Starter("Player #2", args, null, 0, c1);
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
		JButton report = new JButton("Start Game");
		report.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Note the use of multi-line italic HTML text
				// with the JOptionPane message dialog box.
				String msg = "<html><i>" + c1.getName() + ": " + c1.getSelectedValue() + "<br>" + c2.getName() + ": "
						+ c2.getSelectedValue() + "</i>";
				JOptionPane.showMessageDialog(parent, msg);
				restartGame(c1.getSelectedValue(),c2.getSelectedValue());
			}
		});

		// Add the 3 ItemChooser objects, and the Button to the panel
		add(c1, BorderLayout.WEST);
		add(c2, BorderLayout.EAST);
		add(report,BorderLayout.SOUTH);

//		// Add the panel and the message line to the window
//		Container contentPane = parent.getContentPane();
//		contentPane.add(this, BorderLayout.CENTER);
//		contentPane.add(msgline, BorderLayout.SOUTH);
	}

	private void restartGame(Object object, Object object2) {
		
	}
}
