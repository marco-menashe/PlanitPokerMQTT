package com.example;

import org.eclipse.paho.client.mqttv3.*;

/**
 * Subscriber class that listens for story updates from the MQTT broker.
 * It processes incoming messages and updates the repository accordingly.
 *
 * @author Marco Menashe
 */

public class T4B_Subscriber implements MqttCallback {

    private final String broker = "tcp://broker.hivemq.com:1883";
    private final String topic = "planitpoker/stories";
    private MqttClient client;

    private T4B_Repository repository = T4B_Repository.getInstance();

    public T4B_Subscriber() throws MqttException {
        client = new MqttClient(broker, MqttClient.generateClientId());
        client.setCallback(this);
        client.connect();
        client.subscribe(topic);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        System.out.println("Received story update: " + payload);

        // Example: payload = "Story Title|5"
        String[] parts = payload.split("\\|");
        if (parts.length == 2) {
            String title = parts[0];
            int score = Integer.parseInt(parts[1]);
        
            System.out.println("Story title: " + title + ", score: " + score);
            T4B_Repository.saveStory(title, score);
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