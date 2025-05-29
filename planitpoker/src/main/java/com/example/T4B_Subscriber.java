package com.example;

import org.eclipse.paho.client.mqttv3.*;

/**
 * Subscriber class that listens for story updates from the MQTT broker.
 * It processes incoming messages and updates the repository accordingly.
 *
 * @author Marco Menashe
 */

public class T4B_Subscriber implements MqttCallback {

    private final String broker = "tcp://test.mosquitto.org:1883";
    private MqttClient client;

    private T4B_Repository repository = T4B_Repository.getInstance();

    public T4B_Subscriber() throws MqttException {
        client = new MqttClient(broker, MqttClient.generateClientId());
        client.setCallback(this);
        client.connect();

        client.subscribe("planitpoker/chat");
        client.subscribe("planitpoker/votes");
        client.subscribe("planitpoker/stories");
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        if (topic.equals("planitpoker/chat")) {
            // Example: payload = "username|Hello world!"
            String[] parts = payload.split("\\|", 2);
            if (parts.length == 2) {
                String username = parts[0];
                String chatText = parts[1];
                System.out.println("Chat from " + username + ": " + chatText);
                T4B_Repository.getInstance().newChatMessage(username, chatText);
            }
        } else if (topic.equals("planitpoker/votes")) {
            // Example: payload = "username|Story Title|5.0"
            String[] parts = payload.split("\\|", 3);
            if (parts.length == 3) {
                String username = parts[0];
                String storyTitle = parts[1];
                double voteValue = Double.parseDouble(parts[2]);
                System.out.println("Vote from " + username + " for story '" + storyTitle + "': " + voteValue);
                T4B_Repository.getInstance().addVote(voteValue);

                int expectedVotes = T4B_Repository.getInstance().getPlayers().size();
                if (T4B_Repository.getInstance().getCurrentVotes().size() >= expectedVotes) {
                    int finalScore = (int) Math.round(T4B_Repository.calculateAverage());
                    T4B_Repository.getInstance().completeCurrentStory(storyTitle, finalScore);
                    T4B_Repository.getInstance().clearVotes();
                }
            }
        }
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // No action needed for subscriber
    }
}
