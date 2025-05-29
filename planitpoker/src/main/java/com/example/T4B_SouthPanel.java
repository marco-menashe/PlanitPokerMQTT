package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.*;

/**
 * Stories organized in tabs.
 * The first tab contains the active stories, and the second one contains the completed stories.
 *
 * @author javiergs
 */
public class T4B_SouthPanel extends JPanel {

	private JLabel resultLabel;

	public T4B_SouthPanel() {
		setBackground(new Color(161, 190, 239));
		setLayout(new BorderLayout());
		JTabbedPane storyTabs = new JTabbedPane();

		// Add average vote label
		JPanel resultsPanel = new JPanel();
		resultLabel = new JLabel("Average: -");
		resultsPanel.add(resultLabel);
		add(resultsPanel, BorderLayout.NORTH);

		// Get stories from repository instead of nanny
		Queue<T4B_Story> newStories = T4B_Repository.getInstance().getNewStories();
		LinkedList<T4B_Story> prevStories = T4B_Repository.getInstance().getPrevStories();

		// Active stories
		StringBuilder activeStoriesText = new StringBuilder();
		for (T4B_Story story : newStories) {
			activeStoriesText.append(story.getTitle()).append("\n");
		}
		JTextArea activeStories = new JTextArea(activeStoriesText.toString());
		activeStories.setEditable(false);

		// Completed stories
		StringBuilder completedStoriesText = new StringBuilder();
		for (T4B_Story story : prevStories) {
			completedStoriesText.append(String.format("%-50s %10d%n", story.getTitle(), story.getScore()));
		}
		JTextArea completedStories = new JTextArea(completedStoriesText.toString());
		completedStories.setEditable(false);

		// Scroll panes
		JScrollPane activeScrollPane = new JScrollPane(activeStories);
		activeScrollPane.setPreferredSize(new Dimension(400, 150));

		JScrollPane completedScrollPane = new JScrollPane(completedStories);
		completedScrollPane.setPreferredSize(new Dimension(400, 150));

		// Add tabs
		storyTabs.addTab("Active Stories", activeScrollPane);
		storyTabs.addTab("Completed Stories", completedScrollPane);
		storyTabs.addTab("Results", resultsPanel);

		add(storyTabs, BorderLayout.CENTER);
	}

	public void updateResults() {
		double average = T4B_Repository.calculateAverage();
		if (Double.isNaN(average)) {
			resultLabel.setText("Average: ?");
		} else {
			resultLabel.setText(String.format("Average: %.2f", average));
		}
	}
}
