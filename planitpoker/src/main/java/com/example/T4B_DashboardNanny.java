package com.example;

import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;
import java.util.*;

/**
 * Controller responsible for managing the dashboard and its interactions.
 */
public class T4B_DashboardNanny {
    private String currentVote;
    private boolean voteConfirmed = false;
    private T4B_CardsPanel cardsPanel;
    private T4B_Publisher publisher;

    private String currentStoryTitle;

    private T4B_DashboardPanel dashboardPanel;

    public T4B_DashboardNanny(T4B_CardsPanel cardsPanel) {
        this.cardsPanel = cardsPanel;
        this.currentVote = null;
        this.voteConfirmed = false;
    }

    public void setPublisher(T4B_Publisher publisher) {
        this.publisher = publisher;
    }

    public void setCardsPanel(T4B_CardsPanel cardsPanel) {
        this.cardsPanel = cardsPanel;
    }

    public void setCurrentStoryTitle(String title) {
        this.currentStoryTitle = title;

        // Notify SouthPanel to update the current story label
        if (dashboardPanel != null && dashboardPanel.getSouthPanel() != null) {
            dashboardPanel.getSouthPanel().updateCurrentStory(title);
        }
    }

    public void setDashboardPanel(T4B_DashboardPanel dashboardPanel) {
        this.dashboardPanel = dashboardPanel;
    }

    public void setCurrentVote(String vote) {
        if (!voteConfirmed) this.currentVote = vote;
    }

    public void confirmVote() {
        if (voteConfirmed) return;
        if (currentVote == null) {
            JOptionPane.showMessageDialog(null, "Please select a card first.");
            return;
        }
        if (currentStoryTitle == null) {
            JOptionPane.showMessageDialog(null, "No active story to vote on.");
            return;
        }

        try {
            double val = convertVoteToNumber(currentVote);

            // Only publish, don't call completeCurrentStory here
            if (publisher != null) {
                publisher.publishVote(
                        T4B_Repository.getInstance().getPlayers().get(0).getName(),
                        currentStoryTitle, val
                );
            }

            voteConfirmed = true;
            if (cardsPanel != null) cardsPanel.lockSelection();

        } catch (NumberFormatException | MqttException e) {
            JOptionPane.showMessageDialog(null, "Error recording vote: " + e.getMessage());
        }
    }


    public void showResults() {
        List<Double> votes = T4B_Repository.getInstance().getCurrentVotes();
        System.out.println("DEBUG: showResults sees votes = " + votes.size());
        if (votes.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No votes cast this round.");
            return;
        }

        double avg = T4B_Repository.calculateAverage();
        dashboardPanel.updateResults();
        JOptionPane.showMessageDialog(null, "Average vote: " + avg);
        // No longer finalizing the story here – that’s handled by subscriber
    }

    public void startNewVote() {
        T4B_Repository.getInstance().clearVotes();
        voteConfirmed = false;
        currentVote = null;

        if (cardsPanel != null) cardsPanel.resetCards();
        if (dashboardPanel != null)
            dashboardPanel.getSouthPanel().resetAverage();

        T4B_Story next = T4B_Repository.getInstance().peekNextStory();
        currentStoryTitle = (next != null ? next.getTitle() : null);

        // ⬇ Notify SouthPanel of the new current story
        if (dashboardPanel != null && dashboardPanel.getSouthPanel() != null) {
            dashboardPanel.getSouthPanel().updateCurrentStory(currentStoryTitle);
        }
    }


    public boolean isVoteConfirmed() {
        return voteConfirmed;
    }

    private double convertVoteToNumber(String vote) throws NumberFormatException {
        return switch (vote) {
            case "½" -> 0.5;
            case "?" -> Double.NaN;
            default -> {
                try {
                    yield Double.parseDouble(vote);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid vote input: " + vote);
                    throw e;
                }
            }
        };
    }
}