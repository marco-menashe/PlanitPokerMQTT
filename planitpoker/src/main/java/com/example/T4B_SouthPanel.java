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
 *
 * @author adriansanchez
 */
public class T4B_SouthPanel extends JPanel implements PropertyChangeListener {

	private JLabel resultLabel;
	private JLabel currentStoryLabel;
	private JTextArea activeStories;
	private JTextArea completedStories;

	public T4B_SouthPanel() {
		setBackground(new Color(161, 190, 239));
		setLayout(new BorderLayout());

		JTabbedPane storyTabs = new JTabbedPane();

		// --- Current story label ---
		currentStoryLabel = new JLabel("Current Story: None");
		currentStoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
		currentStoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// --- Result label ---
		resultLabel = new JLabel("Average: -");
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel topPanel = new JPanel(new GridLayout(2, 1));
		topPanel.add(currentStoryLabel);
		topPanel.add(resultLabel);
		add(topPanel, BorderLayout.NORTH);

		// --- Active stories ---
		activeStories = new JTextArea();
		activeStories.setEditable(false);
		JScrollPane activeScrollPane = new JScrollPane(activeStories);
		activeScrollPane.setPreferredSize(new Dimension(400, 150));

		// --- Completed stories ---
		completedStories = new JTextArea();
		completedStories.setEditable(false);
		JScrollPane completedScrollPane = new JScrollPane(completedStories);
		completedScrollPane.setPreferredSize(new Dimension(400, 150));

		// --- Tabs ---
		storyTabs.addTab("Active Stories", activeScrollPane);
		storyTabs.addTab("Completed Stories", completedScrollPane);

		add(storyTabs, BorderLayout.CENTER);

		// Register for repository updates
		T4B_Repository.getInstance().addPropertyChangeListener(this);

		// Initial content
		refreshStoryLists();
	}

	// Updates both active and completed story text areas
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

	public void resetAverage() {
		resultLabel.setText("Average: -");
	}

	// This can be called from DashboardNanny to display current story
	public void updateCurrentStory(String title) {
		currentStoryLabel.setText("Current Story: " + (title != null ? title : "None"));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
			case "storyCompleted":
			case "currentStorySet":
				currentStoryLabel.setText("Current Story: " +
						(evt.getNewValue() != null ? ((T4B_Story) evt.getNewValue()).getTitle() : "None"));
				break;
			case "storyAdded":
				refreshStoryLists();
				break;
		}
	}
}
