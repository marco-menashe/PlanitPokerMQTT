package com.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Publisher class that sends story updates to the MQTT broker.
 * It allows publishing stories with their scores to a specific topic.
 *
 * @author Marco Menashe
 */

public class T4B_Publisher {
    private final String broker = "tcp://test.mosquitto.org:1883"; // public broker for testing
    // private final String topic = "planitpoker/stories";
    private MqttClient client;

    public T4B_Publisher(String clientId) throws MqttException {
        client = new MqttClient(broker, clientId);
        client.connect();
    }

    // Add username to the method signature and payload
    public void publishStory(String storyTitle, int score) throws MqttException {
        String payload = storyTitle + "|" + score;
        MqttMessage message = new MqttMessage(payload.getBytes());
        client.publish("planitpoker/stories", message);
    }

    // Publish a chat message with username
    public void publishChatMessage(String username, String messageText) throws MqttException {
        String chatPayload = username + "|" + messageText;
        MqttMessage message = new MqttMessage(chatPayload.getBytes());
        client.publish("planitpoker/chat", message);
    }

    // Publish a vote for a story
    public void publishVote(String username, String storyTitle, double voteValue) throws MqttException {
        String votePayload = username + "|" + storyTitle + "|" + voteValue;
        MqttMessage message = new MqttMessage(votePayload.getBytes());
        client.publish("planitpoker/votes", message);
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }
    public void publishPlayerJoin(String username) throws MqttException {
        MqttMessage message = new MqttMessage(username.getBytes());
        client.publish("planitpoker/join", message);
    }

}