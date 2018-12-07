package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class RiskGame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JLabel announcement;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RiskGame window = new RiskGame();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws InterruptedException
	 */
	public RiskGame() throws InterruptedException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws InterruptedException
	 */
	private void initialize() {
		setTitle("Risk");
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Can not set look and feel");
		}
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(new Intro(this), BorderLayout.EAST);

		announcement = new JLabel("");
		announcement.setHorizontalAlignment(SwingConstants.CENTER);
		announcement.setFont(announcement.getFont().deriveFont(30.0f));
		add(announcement, BorderLayout.PAGE_START);

		setSize(700, 700);
		setVisible(true);

		makeAnnouncment("Choose Agents");
	}
	
	public static void makeAnnouncment(String text) {
		announcement.setText(text);
		System.out.println(text);
	}
}
