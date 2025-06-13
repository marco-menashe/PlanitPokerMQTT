package com.example;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Publisher class that sends various updates to the MQTT broker.
 * Handles publishing stories, chat messages, votes, player joins, completed stories, and player lists.
 *
 * @author Marco Menashe
 */

public class T4B_Publisher {
    private final String broker = "tcp://test.mosquitto.org:1883";
    private MqttClient client;

    public T4B_Publisher(String clientId) throws MqttException {
        client = new MqttClient(broker, clientId);
        client.connect();
    }

    public void publishStory(String storyTitle, int score) throws MqttException {
        String payload = storyTitle + "|" + score;
        MqttMessage message = new MqttMessage(payload.getBytes());
        client.publish("planitpoker/stories", message);
    }

    public void publishChatMessage(String username, String messageText) throws MqttException {
        String chatPayload = username + "|" + messageText;
        MqttMessage message = new MqttMessage(chatPayload.getBytes());
        client.publish("planitpoker/chat", message);
    }

    public void publishVote(String username, String storyTitle, double voteValue) throws MqttException {
        String votePayload = username + "|" + storyTitle + "|" + voteValue;
        MqttMessage message = new MqttMessage(votePayload.getBytes());
        client.publish("planitpoker/votes", message);
    }

    public void publishPlayerJoin(String username) throws MqttException {
        MqttMessage message = new MqttMessage(username.getBytes());
        client.publish("planitpoker/join", message);
    }

    public void publishCompletedStory(String storyTitle, int finalScore) throws MqttException {
        String payload = storyTitle + "|" + finalScore;
        MqttMessage message = new MqttMessage(payload.getBytes());
        client.publish("planitpoker/stories", message);
    }

    public void publishPlayerList(List<T4B_Player> players) throws MqttException {
        StringBuilder sb = new StringBuilder();
        for (T4B_Player p : players) {
            sb.append(p.getName()).append(",");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        MqttMessage message = new MqttMessage(sb.toString().getBytes());
        client.publish("planitpoker/players", message);
    }
}