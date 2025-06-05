package com.example;

import javax.swing.JOptionPane;


/**
 * LoginNanny is responsible for handling the login process.
 *
 * @author Marco
 */
public class T4B_LoginNanny {
	private final T4B_Main main;

	public T4B_LoginNanny(T4B_Main main) {
		this.main = main;
	}

	// public void enterRoom(String name) {
	// 	T4B_Repository.getInstance().addName(name, false);
	// 	showCreateRoomScreen();
	// }

	public void login(String username, String password) {
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			JOptionPane.showMessageDialog(main, "Username and password are required.");
			return;
		}
		try {
			// Store credentials using TaigaStoryFetcher
			// T4B_TaigaStoryFetcher.addUsernameAndPassword(username, password);
			String authToken = T4B_TaigaStoryFetcher.loginAndGetToken(username, password);
			T4B_Repository.getInstance().setAuthToken(authToken);
			JOptionPane.showMessageDialog(main, "Credentials saved! Now you can proceed.");
			// Proceed to the next screen, e.g., showCreateRoomScreen();
			showCreateRoomScreen();
		} catch (Exception e) {
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