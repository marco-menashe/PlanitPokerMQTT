package com.example;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Queue;
import javax.swing.*;

/**
 * Stories organized in tabs.
 * Now only includes active stories.
 *
 * @author adriansanchez
 */
public class T4B_SouthPanel extends JPanel implements PropertyChangeListener {

	private JLabel resultLabel;
	private JLabel currentStoryLabel;
	private JTextArea activeStories;

	public T4B_SouthPanel() {
		setBackground(new Color(161, 190, 239));
		setLayout(new BorderLayout());

		JTabbedPane storyTabs = new JTabbedPane();

		currentStoryLabel = new JLabel("Current Story: None");
		currentStoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
		currentStoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

		resultLabel = new JLabel("Average: -");
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel topPanel = new JPanel(new GridLayout(2, 1));
		topPanel.add(currentStoryLabel);
		topPanel.add(resultLabel);
		add(topPanel, BorderLayout.NORTH);

		activeStories = new JTextArea();
		activeStories.setEditable(false);
		JScrollPane activeScrollPane = new JScrollPane(activeStories);
		activeScrollPane.setPreferredSize(new Dimension(400, 150));

		storyTabs.addTab("Active Stories", activeScrollPane);

		add(storyTabs, BorderLayout.CENTER);
		T4B_Repository.getInstance().addPropertyChangeListener(this);
		refreshStoryLists();
	}

	public void refreshStoryLists() {
		StringBuilder activeText = new StringBuilder();
		Queue<T4B_Story> newStories = T4B_Repository.getInstance().getNewStories();
		for (T4B_Story story : newStories) {
			activeText.append(story.getTitle()).append("\n");
		}
		activeStories.setText(activeText.toString());
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
