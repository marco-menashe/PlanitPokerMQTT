package com.example;

import java.util.LinkedList;
import java.util.Queue;



/**
 * Controller responsible for managing the stories and their interactions with the user interface.
 *
 * @author Marco Menashe
 */
public class T4B_StoriesNanny {
	
	private final T4B_Main main;
	private final Queue<T4B_Story> newStories = new LinkedList<>();
	private final LinkedList<T4B_Story> prevStories = new LinkedList<>(); 
		
	public T4B_StoriesNanny(T4B_Main main) {
		this.main = main;
	}
	
	public void saveAndAddNew(String text) {
		newStories.add(new T4B_Story(text, 0));
		switchGUI();
		
	}
	
	public void saveAndClose(String text) {
		System.out.println(text);
		T4B_Repository.addStory(text);
		switchGUI();
	}
	
	public void sortPrevStories() {
		prevStories.sort((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()));
	}

	public void importStories() {


	}
	
	public LinkedList<T4B_Story> getPrevStories() {
		prevStories.sort((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()));
		return prevStories;
	}
	
	public Queue<T4B_Story> getNewStories() {		
		return newStories;
	}
	
	public void cancel() {
		System.out.println("canceling...");
	}
	
	private void switchGUI() {
		main.setTitle("dashboard");
		T4B_DashboardNanny dashboardNanny = new T4B_DashboardNanny(main);
		T4B_DashboardPanel dashboardPanel = new T4B_DashboardPanel(dashboardNanny);
		main.setContentPane(dashboardPanel);
		main.setSize(800, 600);
		main.setLocationRelativeTo(null);
		main.revalidate();
		main.repaint();
	}

}

