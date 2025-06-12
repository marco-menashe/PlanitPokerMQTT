package com.example;

import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoginNanny is responsible for handling the login process.
 *
 * @author Marco
 */
public class T4B_LoginNanny {
	private final T4B_Main main;
	private static final Logger logger = LoggerFactory.getLogger(T4B_LoginNanny.class);

	public T4B_LoginNanny(T4B_Main main) {
		this.main = main;
	}


	public void login(String username, String password) {
		logger.info("Login attempt for user='{}'", username);
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			logger.warn("Empty username or password provided");
			JOptionPane.showMessageDialog(main, "Username and password are required.");
			return;
		}
		try {
			// Store credentials using TaigaStoryFetcher
			// T4B_TaigaStoryFetcher.addUsernameAndPassword(username, password);
			String authToken = T4B_TaigaStoryFetcher.loginAndGetToken(username, password);
			logger.info("Login SUCCESS for user='{}'; tokenLength={}", username, authToken.length());
			T4B_Repository.getInstance().setAuthToken(authToken);
			T4B_Repository.getInstance().addName(username, true);
			JOptionPane.showMessageDialog(main, "Credentials saved! Now you can proceed.");
			// Proceed to the next screen, e.g., showCreateRoomScreen();
			showCreateRoomScreen();
		} catch (Exception e) {
			logger.error("Login FAILED for user='{}': {}", username, e.getMessage(), e);
			JOptionPane.showMessageDialog(main, "Failed to save credentials: " + e.getMessage());
		}
	}

	private void showCreateRoomScreen() {
		main.setTitle("Room");
		T4B_CreateRoomNanny createRoomNanny = new T4B_CreateRoomNanny(main);
		main.setContentPane(new T4B_CreateRoomPanel(createRoomNanny));
		main.setSize(500, 500);
		main.revalidate();
		main.repaint();
	}
}