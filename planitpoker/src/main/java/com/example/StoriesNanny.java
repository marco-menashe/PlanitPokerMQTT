package com.example;

import java.util.LinkedList;
import java.util.Queue;



/**
 * Controller responsible for managing the stories and their interactions with the user interface.
 *
 * @author javiergs
 */
public class StoriesNanny {
	
	private final Main main;
	private final Queue<Story> newStories = new LinkedList<>(); // Moved to class level
	private final LinkedList<Story> prevStories = new LinkedList<>(); // Reintroduced prevStories
		
	public StoriesNanny(Main main) {
		this.main = main;
	}
	
	public void saveAndAddNew(String text) {
		newStories.add(new Story(text, 0));
		switchGUI();
		
	}
	
	public void saveAndClose(String text) {
		System.out.println(text);
		Blackboard.addStory(text);
		switchGUI();
	}
	
	public void sortPrevStories() {
		prevStories.sort((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()));
	}

	public void importStories() {
		prevStories.add(new Story("Story 1", 5));
		prevStories.add(new Story("Story 2", 8));
		prevStories.add(new Story("Story 3", 2));
		prevStories.add(new Story("Story 4", 1));
		
		
		
		
		newStories.add(new Story("Upcoming story", 0)); 
		newStories.add(new Story("Upcoming story 2", 0)); 
	}
	
	public LinkedList<Story> getPrevStories() {
		prevStories.sort((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()));
		return prevStories;
	}
	
	public Queue<Story> getNewStories() {		
		return newStories;
	}
	
	public void cancel() {
		System.out.println("canceling...");
	}
	
	private void switchGUI() {
		main.setTitle("dashboard");
		DashboardNanny dashboardNanny = new DashboardNanny(main);
		DashboardPanel dashboardPanel = new DashboardPanel(dashboardNanny);
		main.setContentPane(dashboardPanel);
		main.setSize(800, 600);
		main.setLocationRelativeTo(null);
		main.revalidate();
		main.repaint();
	}

}

