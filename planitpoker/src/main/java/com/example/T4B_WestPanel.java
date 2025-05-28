package com.example;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel that contains the left side of the dashboard.
 * It contains the username, start button, players, timer, invite a teammate and copy URL.
 *
 * @author javiergs
 */
public class WestPanel extends JPanel {
	
    public WestPanel(DashboardNanny dashboardNanny, DashboardPanel dashboardPanel) {
        setBackground(new Color(255, 204, 204));
        setLayout(new GridLayout(8, 1));

        add(new JLabel("Player 1"));
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> dashboardNanny.startNewVote());
        add(startButton);

        add(new JLabel("Players:"));
        add(new JLabel("00:00:00"));
        add(new JLabel("Invite a teammate"));
        add(new JTextField("https://app.planitpoker.com"));
        add(new JButton("Copy URL"));

        JButton confirmButton = new JButton("Confirm Vote");
        confirmButton.addActionListener(e -> dashboardNanny.confirmVote());
        add(confirmButton);

        JButton resultsButton = new JButton("Show Results");
        resultsButton.addActionListener(e -> dashboardPanel.updateResults());
        add(resultsButton);
    }
	
}

