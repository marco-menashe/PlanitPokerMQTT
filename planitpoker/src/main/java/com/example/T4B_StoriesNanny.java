package com.example;

import java.util.Queue;

import javax.swing.JOptionPane;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller responsible for managing the stories and their interactions with the user interface.
 * It interacts with the shared repository for persistence and updates.
 *
 * @author Marco
 */
public class T4B_StoriesNanny {

	private final T4B_Main main;
	private static final Logger logger = LoggerFactory.getLogger(T4B_StoriesNanny.class);

	public T4B_StoriesNanny(T4B_Main main) {
		this.main = main;
	}

public void importStories() {
    String authToken = T4B_Repository.getInstance().getAuthToken();
    if (authToken == null || authToken.isEmpty()) {
        JOptionPane.showMessageDialog(main, "You must log in to Taiga first.");
        return;
    }


    String projectSlug = JOptionPane.showInputDialog(main, "Enter your Taiga project slug:");
    if (projectSlug == null || projectSlug.isEmpty()) {
        JOptionPane.showMessageDialog(main, "Project slug is required.");
        return;
    }
	logger.info("Importing stories for project slug '{}'", projectSlug);


	try {
        int projectId = T4B_TaigaStoryFetcher.getProjectId(authToken, projectSlug);
        JSONArray stories = T4B_TaigaStoryFetcher.fetchUserStories(authToken, projectId);
		logger.info("Fetched {} stories from Taiga", stories.length());

   
        Queue<T4B_Story> newStories = T4B_Repository.getInstance().getNewStories();
        newStories.clear();


        for (int i = 0; i < stories.length(); i++) {
            JSONObject story = stories.getJSONObject(i);
            String title = story.optString("subject", "(no title)");
            int score = story.has("total_points") && !story.isNull("total_points")
                    ? (int) story.getDouble("total_points")
                    : 0;
            T4B_Repository.getInstance().addStory(new T4B_Story(title, score));
        }

        JOptionPane.showMessageDialog(main, "Imported " + stories.length() + " stories from Taiga.");
        switchGUI();
    } catch (Exception e) {
		logger.error("Failed to import stories", e);
        JOptionPane.showMessageDialog(main, "Failed to import stories from Taiga: " + e.getMessage());
    }
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

		T4B_Publisher publisher = T4B_Repository.getInstance().getPublisher();
		if (publisher == null) {
			try {
				publisher = new T4B_Publisher(MqttClient.generateClientId());
				T4B_Repository.getInstance().setPublisher(publisher);
			} catch (MqttException e) {
				System.out.println("Failed to initialize publisher: " + e.getMessage());
				return;
			}
		}

		try {
			publisher.publishStory(text, 0);
		} catch (MqttException e) {
			System.out.println("Failed to publish story: " + e.getMessage());
		}

		switchGUI();
	}


	// public void importStories() {
	// 	switchGUI();
	// }

	// public LinkedList<T4B_Story> getPrevStories() {
	// 	return T4B_Repository.getInstance().getPrevStories();
	// }

	// public Queue<T4B_Story> getNewStories() {
	// 	return T4B_Repository.getInstance().getNewStories();
	// }



	public void cancel() {
		System.out.println("canceling...");
	}

	private void switchGUI() {
		main.setTitle("Dashboard");

		T4B_Publisher publisher;
		try {
			publisher = new T4B_Publisher(MqttClient.generateClientId());
		} catch (MqttException e) {
			JOptionPane.showMessageDialog(main, "Publisher failed: " + e.getMessage());
			return;
		}
		T4B_Repository.getInstance().setPublisher(publisher);

		try {
			new T4B_Subscriber();
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

		T4B_Story currentStory = T4B_Repository.getInstance().getCurrentStory();
		if (currentStory != null) {
			dashboardNanny.setCurrentStoryTitle(currentStory.getTitle());
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