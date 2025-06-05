package com.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


/**
 * Singleton class that serves as the central data repository .
 * managing players, storing and broadcasting chat messages,
 * handling votes and calculating averages, and tracking story progress across sessions.
 *
 * @author Adrian Sanchez
 * @version 1.0
 */
public class T4B_Repository {


	private static T4B_Repository instance;
	private final PropertyChangeSupport pcs;


	private String authToken;




	private String roomID;
	private String mode;
	private T4B_Publisher publisher;

	private final List<T4B_Player> players;
	private final List<String> chatMessages;
	private final Queue<T4B_Story> newStories;
	private final LinkedList<T4B_Story> prevStories;

	private final List<Double> currentVotes;
	private final Map<String, Set<String>> storyVoters = new HashMap<>();
	private T4B_Story currentStory;


	private T4B_Repository(){
		this.pcs = new PropertyChangeSupport(this);
		this.players = new ArrayList<>();
		this.chatMessages = new LinkedList<>();
		this.newStories = new LinkedList<>();
		this.prevStories = new LinkedList<>();
		this.currentVotes = new ArrayList<>();
	}

	public static synchronized T4B_Repository getInstance(){
		if (instance == null){
			instance = new T4B_Repository();
		}
		return instance;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}


	public String getAuthToken() {
		return authToken;
	}
	
	
	
	public T4B_Story peekNextStory() {
		return newStories.peek();
	}

	public void addCurrentRoom(String roomID){
		this.roomID = roomID;
		pcs.firePropertyChange("roomID", null, roomID);
	}

	public void addCurrentMode(String mode){
		this.mode = mode;
		pcs.firePropertyChange("mode", null, mode);
	}

	public void addName(String name, boolean shouldBroadcast) {
		for (T4B_Player player : players) {
			if (player.getName().equalsIgnoreCase(name)) {
				return;
			}
		}
		T4B_Player player = new T4B_Player(name, generatePlayerID(name));
		players.add(player);
		pcs.firePropertyChange("playerAdded", null, player);

		if (shouldBroadcast && publisher != null) {
			try {
				publisher.publishPlayerJoin(player.getName());
			} catch (Exception e) {
				System.out.println("Failed to broadcast join: " + e.getMessage());
			}
		}
	}

	public void setPublisher(T4B_Publisher publisher) {
		this.publisher = publisher;
	}

	public T4B_Publisher getPublisher() {
		return publisher;
	}


	private String generatePlayerID(String name){
		return name + "_" + System.currentTimeMillis();
	}

	public List<T4B_Player> getPlayers(){
		return players;
	}

	public void addVote(double vote){
		currentVotes.add(vote);
		pcs.firePropertyChange("voteAdded", null, vote);
	}


	public static double calculateAverage() {
		List<Double> votes = getInstance().getCurrentVotes();
		if (votes.isEmpty()) return Double.NaN;
		double sum = 0;
		int count = 0;
		for (Double v : votes) {
			if (v != null && !Double.isNaN(v)) {
				sum += v;
				count++;
			}
		}
		return count == 0 ? Double.NaN : sum / count;
	}
	public void clearVotes(){
		currentVotes.clear();
		storyVoters.clear();
		pcs.firePropertyChange("votesCleared", null, null);
	}



	public void addStory(T4B_Story story) {
		newStories.add(story);
		if (currentStory == null) {
			currentStory = story;
			pcs.firePropertyChange("currentStorySet", null, currentStory);
		}
		pcs.firePropertyChange("storyAdded", null, story);
	}

	public void addStory(String title, int score){
		T4B_Story story = new T4B_Story(title, score);
		prevStories.add(story);
		pcs.firePropertyChange("storyCompleted", null, story);
	}

	public Queue<T4B_Story> getNewStories() {
		return newStories;
	}

	public LinkedList<T4B_Story> getPrevStories() {
		return prevStories;
	}

	public void newChatMessage(String username, String message){
		String fullMessage = username + ": " + message;
		chatMessages.add(fullMessage);
		pcs.firePropertyChange("newChat", null, fullMessage);
	}


	public List<String> getChatMessages() {
		return chatMessages;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener){
		pcs.addPropertyChangeListener(listener);
	}

	public void fireCustomChange(String property, Object oldVal, Object newVal){
		pcs.firePropertyChange(property, oldVal, newVal);
	}
	public void completeCurrentStory(String title, int finalScore) {
		T4B_Story completedStory = null;

		for (T4B_Story story : newStories) {
			if (story.getTitle().equals(title)) {
				completedStory = story;
				break;
			}
		}

		if (completedStory != null) {
			newStories.remove(completedStory);
			completedStory.editScore(finalScore);
			prevStories.add(completedStory);
			pcs.firePropertyChange("storyCompleted", null, completedStory);

			currentStory = newStories.peek();
			pcs.firePropertyChange("currentStorySet", null, currentStory);
		}
	}

	public List<Double> getCurrentVotes() {
		return currentVotes;
	}
	public void addVote(String username, double vote) {
		String title = currentStory != null ? currentStory.getTitle() : "unknown";

		storyVoters.putIfAbsent(title, new HashSet<>());
		Set<String> votersForThisStory = storyVoters.get(title);

		if (votersForThisStory.contains(username)) return; // prevent double vote

		votersForThisStory.add(username);
		currentVotes.add(vote);
		pcs.firePropertyChange("voteAdded", null, vote);
	}

	public T4B_Story getCurrentStory() {
		return currentStory;
	}

	public void setCurrentStory(T4B_Story story) {
		this.currentStory = story;
	}

}