package two;

import one.Blackboard;
import one.Main;
import three.StoriesNanny;
import three.StoriesPanel;

/**
 * Act as a controller for the CreateRoomPanel.
 *
 * @author javiergs
 */
public class CreateRoomNanny {
	
	private Main main;
	
	public CreateRoomNanny(Main main) {
		this.main = main;
	}
	
	public void createRoom(String name, String selectedItem) {
		System.out.println(" Creating room..." + name + ", mode: " + selectedItem);
		Blackboard.addCurrentRoom(name);
		Blackboard.addCurrentMode(selectedItem);
		switchGUI();
	}
	
	private void switchGUI() {
		main.setTitle("Stories");
		StoriesNanny createRoomNanny = new StoriesNanny(main);
		StoriesPanel createRoomPanel = new StoriesPanel(createRoomNanny);
		main.setContentPane(createRoomPanel);
		main.setSize(500, 500);
		main.revalidate();
		main.repaint();
	}
	
}

