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
    private final String broker = "tcp://broker.hivemq.com:1883"; // public broker for testing
    private final String topic = "planitpoker/stories";
    private MqttClient client;

    public T4B_Publisher(String clientId) throws MqttException {
        client = new MqttClient(broker, clientId);
        client.connect();
    }

    // Add username to the method signature and payload
    public void publishStory(String username, String storyTitle, int score) throws MqttException {
        String payload = username + "|" + storyTitle + "|" + score;
        MqttMessage message = new MqttMessage(payload.getBytes());
        client.publish(topic, message);
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }
}