package com.example;


/**
 * Act as a controller for the CreateRoomPanel.
 *
 * @author javiergs
 */
public class T4B_CreateRoomNanny {
	
	private T4B_Main main;
	
	public T4B_CreateRoomNanny(T4B_Main main) {
		this.main = main;
	}
	
	public void createRoom(String name, String selectedItem) {
		System.out.println(" Creating room..." + name + ", mode: " + selectedItem);
		T4B_Repository.addCurrentRoom(name);
		T4B_Repository.addCurrentMode(selectedItem);
		switchGUI();
	}
	
	private void switchGUI() {
		main.setTitle("Stories");
		T4B_StoriesNanny createRoomNanny = new T4B_StoriesNanny(main);
		T4B_StoriesPanel createRoomPanel = new T4B_StoriesPanel(createRoomNanny);
		main.setContentPane(createRoomPanel);
		main.setSize(500, 500);
		main.revalidate();
		main.repaint();
	}
	
}

