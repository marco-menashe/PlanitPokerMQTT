package com.example;

/**
 * LoginNanny is responsible for handling the login process.
 *
 * @author javiergs
 */
public class T4B_LoginNanny {
	private final T4B_Main main;

	public T4B_LoginNanny(T4B_Main main) {
		this.main = main;
	}

	public void enterRoom(String name) {
		// add player once
		T4B_Repository.getInstance().addName(name);
		showCreateRoomScreen();
	}

	public void login(String name) {
		// login can just call enterRoom
		enterRoom(name);
	}

	private void showCreateRoomScreen() {
		main.setTitle("Room");
		T4B_CreateRoomNanny nanny = new T4B_CreateRoomNanny(main);
		main.setContentPane(new T4B_CreateRoomPanel(nanny));
		main.setSize(500, 500);
		main.revalidate();
		main.repaint();
	}
}
