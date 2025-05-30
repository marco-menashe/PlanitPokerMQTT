package com.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Controller responsible for managing the stories and their interactions with the user interface.
 * It interacts with the shared repository for persistence and updates.
 *
 * @author Marco
 */
public class T4B_StoriesNanny {

	private final T4B_Main main;

	public T4B_StoriesNanny(T4B_Main main) {
		this.main = main;
	}

	public void saveAndAddNew(String text) {
		T4B_Repository.getInstance().addStory(new T4B_Story(text, 0));  // Save locally

		T4B_Publisher publisher = T4B_Repository.getInstance().getPublisher();
		if (publisher == null) {
			try {
				publisher = new T4B_Publisher(MqttClient.generateClientId());
				T4B_Repository.getInstance().setPublisher(publisher);
			} catch (Exception e) {
				System.out.println("Failed to initialize publisher: " + e.getMessage());
				return;
			}
		}

		try {
			publisher.publishStory(text, 0);
		} catch (Exception e) {
			System.out.println("Failed to publish story: " + e.getMessage());
		}
	}



	public void saveAndClose(String text) {
		T4B_Story story = new T4B_Story(text, 0);
		T4B_Repository.getInstance().addStory(story);
		try {
			T4B_Repository.getInstance().getPublisher().publishStory(text, 0);
		} catch (MqttException e) {
			System.out.println("Failed to publish story: " + e.getMessage());
		}
		switchGUI();
	}


	public void importStories() {
		switchGUI();
	}

	public LinkedList<T4B_Story> getPrevStories() {
		return T4B_Repository.getInstance().getPrevStories();
	}

	public Queue<T4B_Story> getNewStories() {
		return T4B_Repository.getInstance().getNewStories();
	}

	public void cancel() {
		System.out.println("canceling...");
	}

	private void switchGUI() {
		main.setTitle("Dashboard");

		// MQTT setup
		T4B_Publisher publisher;
		try {
			publisher = new T4B_Publisher(MqttClient.generateClientId());
		} catch (MqttException e) {
			JOptionPane.showMessageDialog(main, "Publisher failed: " + e.getMessage());
			return;
		}

		T4B_Repository.getInstance().setPublisher(publisher);

		try {
			new T4B_Subscriber(); // Automatically registers with Repository
		} catch (MqttException e) {
			JOptionPane.showMessageDialog(main, "Subscriber failed: " + e.getMessage());
			return;
		}

		String username = T4B_Repository.getInstance().getPlayers().isEmpty()
				? "Player" + System.currentTimeMillis()
				: T4B_Repository.getInstance().getPlayers().get(0).getName();
		try {
			publisher.publishPlayerJoin(username);
		} catch (MqttException e) {
			System.out.println("Could not broadcast join: " + e.getMessage());
		}

		T4B_DashboardNanny dashboardNanny = new T4B_DashboardNanny(null);
		dashboardNanny.setPublisher(publisher);

		// Set current story title
		T4B_Story nextStory = T4B_Repository.getInstance().peekNextStory();
		if (nextStory != null) {
			dashboardNanny.setCurrentStoryTitle(nextStory.getTitle());
		}

		T4B_DashboardPanel dashboardPanel = new T4B_DashboardPanel(dashboardNanny);
		dashboardNanny.setCardsPanel(dashboardPanel.getCardsPanel());
		dashboardNanny.setDashboardPanel(dashboardPanel);

		main.setContentPane(dashboardPanel);
		main.setSize(800, 600);
		main.setLocationRelativeTo(null);
		main.revalidate();
		main.repaint();
	}
}