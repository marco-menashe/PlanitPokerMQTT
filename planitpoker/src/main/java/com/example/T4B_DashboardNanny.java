package com.example;


import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;

/**
 * Controller responsible for managing the dashboard and its interactions.
 *
 * @author javiergs
 */
public class T4B_DashboardNanny {
    private String currentVote;
    private boolean voteConfirmed = false;
    private T4B_CardsPanel cardsPanel;
    private T4B_Publisher publisher;

    public void setPublisher(T4B_Publisher publisher) {
        this.publisher = publisher;
    }

    public T4B_DashboardNanny(T4B_CardsPanel cardsPanel) {
        this.cardsPanel = cardsPanel;
        this.currentVote = null;
        this.voteConfirmed = false;
    }

    public void setCardsPanel(T4B_CardsPanel cardsPanel){
        this.cardsPanel = cardsPanel;
    }

    public void setCurrentVote(String vote){
        if(!voteConfirmed){
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
                    publisher.publishVote(
                            T4B_Repository.getInstance().getPlayers().get(0).getName(),
                            "CurrentStoryTitle", // eventually pull from current story
                            val
                    );
                }

                if (cardsPanel != null) cardsPanel.lockSelection();

            } catch (NumberFormatException | MqttException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    public void startNewVote(){
        voteConfirmed = false;
        currentVote = null;
        if(cardsPanel != null){
            cardsPanel.resetCards();
        }
    }

    public boolean isVoteConfirmed(){
        return voteConfirmed;
    }

    private double convertVoteToNumber(String vote) throws NumberFormatException{
        return switch (vote){
            case "½" -> 0.5;
            case "?" -> Double.NaN; //not sure what to do here, any number will affect avg score
            default -> Double.parseDouble(vote);
        };
    }
}
