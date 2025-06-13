package com.example;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Allows the user to create a new story.
 *
 * @author Marco
 */

public class T4B_StoriesPanel extends JPanel {

	public T4B_StoriesPanel(T4B_StoriesNanny storiesNanny) {
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Create New Story", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		add(titleLabel, BorderLayout.NORTH);

		JTextArea storyTextArea = new JTextArea("Story: Each line contains new story.");
		JScrollPane scrollPane = new JScrollPane(storyTextArea);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridLayout(2, 2));

		JButton importButton = new JButton("Import");
		JButton cancelButton = new JButton("Cancel");

		buttonPanel.add(importButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);

		importButton.addActionListener(e -> storiesNanny.importStories());
		cancelButton.addActionListener(e -> storiesNanny.cancel());
	}
}