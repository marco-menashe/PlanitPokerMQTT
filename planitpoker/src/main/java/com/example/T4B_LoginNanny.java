package com.example;



/**
 * LoginNanny is responsible for handling the login process.
 *
 * @author javiergs
 */
public class T4B_LoginNanny {
	
	private T4B_Main main;
	
	public T4B_LoginNanny(T4B_Main main) {
		this.main = main;
	}
	
	public void enterRoom(String name) {
		System.out.println(name + " Entering a room...");
		login (name);
		switchGUI();
	}
	
	public void login(String name) {
		System.out.println(name + " Logging in...");
		T4B_Repository.getInstance().addName(name);
		switchGUI();
	}

	private void switchGUI() {
		main.setTitle("Room");
		T4B_CreateRoomNanny createRoomNanny = new T4B_CreateRoomNanny(main);
		T4B_CreateRoomPanel createRoomPanel = new T4B_CreateRoomPanel(createRoomNanny);
		main.setContentPane(createRoomPanel);
		main.setSize(500, 500);
		main.revalidate();
		main.repaint();
	}
	
}
