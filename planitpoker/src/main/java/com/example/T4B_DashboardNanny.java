package com.example;

import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;

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
    }

    public void setDashboardPanel(T4B_DashboardPanel dashboardPanel) {
        this.dashboardPanel = dashboardPanel;
    }

    public void setCurrentVote(String vote) {
        if (!voteConfirmed) {
            currentVote = vote;
        }
    }

    public void confirmVote() {
        if (currentVote != null) {
            voteConfirmed = true;
            try {
                double val = convertVoteToNumber(currentVote);
                T4B_Repository.getInstance().addVote(val);

                if (publisher != null) {
                    String title = T4B_Repository.getInstance().peekNextStory().getTitle();
                    publisher.publishVote(
                            T4B_Repository.getInstance().getPlayers().get(0).getName(),
                            title,
                            val
                    );
                }

                if (cardsPanel != null) cardsPanel.lockSelection();

                int expectedVotes = T4B_Repository.getInstance().getPlayers().size();
                if (T4B_Repository.getInstance().getCurrentVotes().size() >= expectedVotes) {
                    double avg = T4B_Repository.calculateAverage();
                    String currentTitle = T4B_Repository.getInstance().peekNextStory().getTitle();
                    T4B_Repository.getInstance().completeCurrentStory(currentTitle, (int)Math.round(avg));
                    T4B_Repository.getInstance().clearVotes();
                }

            } catch (NumberFormatException | MqttException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }
    public void startNewVote() {
        voteConfirmed = false;
        currentVote = null;
        if (cardsPanel != null) {
            cardsPanel.resetCards();
        }
    }

    public boolean isVoteConfirmed() {
        return voteConfirmed;
    }

    private double convertVoteToNumber(String vote) throws NumberFormatException {
        return switch (vote) {
            case "½" -> 0.5;
            case "?" -> Double.NaN;
            default -> Double.parseDouble(vote);
        };
    }
}