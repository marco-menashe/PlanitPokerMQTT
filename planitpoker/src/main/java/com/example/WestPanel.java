package four;

import javax.swing.*;
import java.awt.*;

/**
 * Panel that contains the left side of the dashboard.
 * It contains the username, start button, players, timer, invite a teammate and copy URL.
 *
 * @author javiergs
 */
public class WestPanel extends JPanel {
	
	public WestPanel() {
		setBackground(new Color(255, 204, 204));
		setLayout(new GridLayout(7,1));
		add(new JLabel("Javier"));
		add(new JButton("Start"));
		add(new JLabel("Players:"));
		add(new JLabel("00:00:00"));
		add(new JLabel("Invite a teammate"));
		add(new JTextField("https://app.planitpoker.com"));
		add(new JButton("Copy URL"));

		// implement the missing functionality
	
	}
	
}

