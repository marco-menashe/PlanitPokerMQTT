package com.example;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subscriber class that listens for story updates from the MQTT broker.
 * It processes incoming messages and updates the repository accordingly.
 *
 * @author Marco Menashe
 */

public class T4B_Subscriber implements MqttCallback {

    private final String broker = "tcp://test.mosquitto.org:1883";
    private MqttClient client;
    private static final Logger logger = LoggerFactory.getLogger(T4B_Subscriber.class);
    private T4B_Repository repository = T4B_Repository.getInstance();


    public T4B_Subscriber() throws MqttException {
        client = new MqttClient(broker, MqttClient.generateClientId());
        client.setCallback(this);
        client.connect();

        client.subscribe("planitpoker/chat");
        client.subscribe("planitpoker/votes");
        client.subscribe("planitpoker/stories");
        client.subscribe("planitpoker/join");

    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
        logger.warn("MQTT connection lost", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.debug("MQTT msg on '{}': {}", topic, payload);
        if (topic.equals("planitpoker/chat")) {
            String[] parts = payload.split("\\|", 2);
            if (parts.length == 2) {
                String username = parts[0];
                String chatText = parts[1];
                System.out.println("Chat from " + username + ": " + chatText);
                repository.newChatMessage(username, chatText);
            }

        } else if (topic.equals("planitpoker/votes")) {
            String[] parts = payload.split("\\|", 3);
            if (parts.length == 3) {
                String username = parts[0];
                String storyTitle = parts[1];
                double voteValue = Double.parseDouble(parts[2]);
                System.out.println("Vote from " + username + " for story '" + storyTitle + "': " + voteValue);
                repository.addVote(username, voteValue);
            }
        } else if (topic.equals("planitpoker/join")) {
            String username = new String(message.getPayload());
            System.out.println("Player joined: " + username);
            T4B_Repository.getInstance().addName(username, false);
        } else if (topic.equals("planitpoker/stories")) {
            String[] parts = payload.split("\\|", 2);
            if (parts.length == 2) {
                String title = parts[0];
                int score = Integer.parseInt(parts[1]);
                T4B_Story story = new T4B_Story(title, score);
                repository.addStory(story);
                repository.setCurrentStory(story);
            }
        }
        else if (topic.equals("planitpoker/players")) {
            String[] names = payload.split(",");
            T4B_Repository repo = T4B_Repository.getInstance();
            repo.clearPlayers(); 
            for (String name : names) {
                if (!name.isEmpty()) {
                    repo.addName(name, false);
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