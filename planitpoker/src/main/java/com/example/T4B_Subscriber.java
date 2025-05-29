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
        if (topic.equals("planitpoker/stories")) {
            System.out.println("Received story update: " + payload);
            String[] parts = payload.split("\\|");
            if (parts.length == 2) {
                String title = parts[0];
                int score = Integer.parseInt(parts[1]);
                System.out.println("Story title: " + title + ", score: " + score);
                T4B_Repository.getInstance().addStory(title, score);
            }
        } else if (topic.equals("planitpoker/chat")) {
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