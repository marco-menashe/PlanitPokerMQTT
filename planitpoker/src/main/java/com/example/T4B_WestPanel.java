package com.example;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Panel that contains the right side of the dashboard.
 * Includes start, timer, invite, and voting buttons.
 *
 * @author adriansanchez
 */

public class T4B_WestPanel extends JPanel {
    private JLabel timerLabel;
    private Timer votingTimer;
    private long startTime;

    public T4B_WestPanel(T4B_DashboardNanny dashboardNanny, T4B_DashboardPanel dashboardPanel) {
        setBackground(new Color(255, 204, 204));
        setLayout(new GridLayout(8, 1));

        String username = T4B_Repository.getInstance().getPlayers().get(0).getName();
        JLabel nameLabel = new JLabel("Logged in as: " + username);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameLabel);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            dashboardNanny.startNewVote();
            startTimer();
        });
        add(startButton);

        timerLabel = new JLabel("Time: 0s");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel);

        JButton confirmButton = new JButton("Confirm Vote");
        confirmButton.addActionListener(e -> dashboardNanny.confirmVote());
        add(confirmButton);

        JButton resultsButton = new JButton("Show Results");
        resultsButton.addActionListener(e -> {
            dashboardNanny.showResults();
            stopTimer();
        });
        add(resultsButton);
    }

    private void startTimer() {
        stopTimer();
        startTime = System.currentTimeMillis();
        votingTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long elapsedSec = (System.currentTimeMillis() - startTime) / 1000;
                timerLabel.setText("Time: " + elapsedSec + "s");
            }
        });
        votingTimer.start();
    }

    private void stopTimer() {
        if (votingTimer != null) {
            votingTimer.stop();
        }
    }
}
