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
	private Thread playerListBroadcaster;

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
			
			String authToken = T4B_TaigaStoryFetcher.loginAndGetToken(username, password);
			logger.info("Login successful, token length={}", authToken.length());
			T4B_Repository.getInstance().setAuthToken(authToken);
			T4B_Repository.getInstance().addName(username, true);
			// If this is the first player, start broadcasting
			if (T4B_Repository.getInstance().getPlayers().size() == 1) {
				startBroadcastingPlayerList();
			}
			JOptionPane.showMessageDialog(main, "Credentials saved! Now you can proceed.");
			// Proceed to the next screen, e.g., showCreateRoomScreen();
			showCreateRoomScreen();
		} catch (Exception e) {
			logger.error("Login failed for user='{}'", username, e);
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

	private void startBroadcastingPlayerList() {
		if (playerListBroadcaster != null && playerListBroadcaster.isAlive()) return;
		playerListBroadcaster = new Thread(() -> {
			try {
				while (true) {
					T4B_Publisher publisher = T4B_Repository.getInstance().getPublisher();
					if (publisher != null) {
						publisher.publishPlayerList(T4B_Repository.getInstance().getPlayers());
					}
					Thread.sleep(2000); // broadcast every 2 seconds
				}
			} catch (InterruptedException ignored) {}
			catch (Exception e) {
				System.out.println("Player list broadcast error: " + e.getMessage());
			}
		});
		playerListBroadcaster.setDaemon(true);
		playerListBroadcaster.start();
	}
}