package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Act as a controller for the CreateRoomPanel.
 *
 * @author Marco
 *
 */
public class T4B_CreateRoomNanny {

	private T4B_Main main;
	private static final Logger logger = LoggerFactory.getLogger(T4B_CreateRoomNanny.class);

	public T4B_CreateRoomNanny(T4B_Main main) {
		this.main = main;
	}

	public void createRoom(String name, String selectedItem) {
		//System.out.println("Creating room..." + name + ", mode: " + selectedItem);
		logger.info("createRoom invoked: name='{}', mode='{}'", name, selectedItem);
		// T4B_Repository.getInstance().setProjectSlug(slug);
		try {
			T4B_Repository.getInstance().addCurrentRoom(name);
			T4B_Repository.getInstance().addCurrentMode(selectedItem);
			switchGUI();
			logger.info("createRoom SUCCESS: room='{}'", name);
		} catch (Exception e) {
			logger.error("createRoom FAILED for room='{}': {}", name, e.getMessage(), e);
		}
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