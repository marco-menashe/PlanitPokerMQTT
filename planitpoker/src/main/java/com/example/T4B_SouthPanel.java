package com.example;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.*;

/**
 * Stories organized in tabs.
 * The first tab contains the active stories, and the second one contains the completed stories.
 */
public class T4B_SouthPanel extends JPanel implements PropertyChangeListener {

	private JLabel resultLabel;
	private JTextArea activeStories;
	private JTextArea completedStories;

	public T4B_SouthPanel() {
		setBackground(new Color(161, 190, 239));
		setLayout(new BorderLayout());
		JTabbedPane storyTabs = new JTabbedPane();

		// Add average vote label
		JPanel resultsPanel = new JPanel();
		resultLabel = new JLabel("Average: -");
		resultsPanel.add(resultLabel);
		add(resultsPanel, BorderLayout.NORTH);

		// Active stories
		activeStories = new JTextArea();
		activeStories.setEditable(false);
		JScrollPane activeScrollPane = new JScrollPane(activeStories);
		activeScrollPane.setPreferredSize(new Dimension(400, 150));

		// Completed stories
		completedStories = new JTextArea();
		completedStories.setEditable(false);
		JScrollPane completedScrollPane = new JScrollPane(completedStories);
		completedScrollPane.setPreferredSize(new Dimension(400, 150));

		// Add tabs
		storyTabs.addTab("Active Stories", activeScrollPane);
		storyTabs.addTab("Completed Stories", completedScrollPane);
		storyTabs.addTab("Results", resultsPanel);

		add(storyTabs, BorderLayout.CENTER);

		// Listen for changes in the repository
		T4B_Repository.getInstance().addPropertyChangeListener(this);

		// Initial content
		refreshStoryLists();
	}


	public void refreshStoryLists() {
		StringBuilder activeText = new StringBuilder();
		Queue<T4B_Story> newStories = T4B_Repository.getInstance().getNewStories();
		for (T4B_Story story : newStories) {
			activeText.append(story.getTitle()).append("\n");
		}
		activeStories.setText(activeText.toString());

		StringBuilder completedText = new StringBuilder();
		LinkedList<T4B_Story> prevStories = T4B_Repository.getInstance().getPrevStories();
		for (T4B_Story story : prevStories) {
			completedText.append(String.format("%-50s %10d%n", story.getTitle(), story.getScore()));
		}
		completedStories.setText(completedText.toString());
	}


	public void updateResults() {
		double average = T4B_Repository.calculateAverage();
		if (Double.isNaN(average)) {
			resultLabel.setText("Average: ?");
		} else {
			resultLabel.setText(String.format("Average: %.2f", average));
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case "voteAdded":
				updateResults();
				break;
			case "storyCompleted":
			case "storyAdded":
				refreshStoryLists();
				break;
		}
	}
}