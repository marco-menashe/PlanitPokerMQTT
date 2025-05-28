package com.example;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Stories organized in tabs.
 * The first tab contains the active stories, and the second one contains the completed stories.
 *
 * @author javiergs
 */
public class T4B_SouthPanel extends JPanel {
	
	public SouthPanel() {
		setBackground(new Color(161, 190, 239));
		setLayout(new BorderLayout());
		JTabbedPane storyTabs = new JTabbedPane();
		
		JPanel resultsPanel = new JPanel();
        	resultLabel = new JLabel("Average: -");
       	 	resultsPanel.add(resultLabel);
	
		//Create StoriesNanny instance to access stories
		StoriesNanny storiesNanny = new StoriesNanny(null); // Pass appropriate Main instance
		storiesNanny.importStories(); // Import stories to populate the lists
		Queue<Story> newStories = storiesNanny.getNewStories(); // Access newStories
	
		LinkedList<Story> prevStories = storiesNanny.getPrevStories();

		
		//Populate active stories with newStories
		StringBuilder activeStoriesText = new StringBuilder();
		for (Story story : newStories) {
			activeStoriesText.append(story.getTitle()).append("\n");
		}
		JTextArea activeStories = new JTextArea(activeStoriesText.toString());
		
		 //Populate completed stories with prevStories
		StringBuilder completedStoriesText = new StringBuilder();
		for (Story story : prevStories) {
			completedStoriesText.append(String.format("%-50s %10s%n", story.getTitle(), story.getScore())).append("\n");
		}
		JTextArea completedStories = new JTextArea(completedStoriesText.toString());
		
		//Adjust table height
		JScrollPane activeScrollPane = new JScrollPane(activeStories);
		activeScrollPane.setPreferredSize(new Dimension(400, 150)); //Adjust height
		
		JScrollPane completedScrollPane = new JScrollPane(completedStories);
		completedScrollPane.setPreferredSize(new Dimension(400, 150)); //Adjust height
		
		storyTabs.addTab("Active Stories", activeScrollPane);
		storyTabs.addTab("Completed Stories", completedScrollPane);
		
		add(storyTabs, BorderLayout.CENTER);
	}
	
    public void updateResults(){
        double average = Blackboard.calculateAverage();
        resultLabel.setText(String.format("Average: %.2f", average));
    }

}


