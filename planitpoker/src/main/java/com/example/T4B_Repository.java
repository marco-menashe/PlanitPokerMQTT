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
public class T4B_Repository extends PropertyChangeSupport{
	private static T4B_Repository instance;
	private String authToken;
	private String projectSlug;
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
	private final Map<String, Double> currentVotesByPlayer = new HashMap<>();

	private T4B_Repository(){
		super(T4B_Repository.class);
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
		firePropertyChange("roomID", null, roomID);
	}

	public void addCurrentMode(String mode){
		this.mode = mode;
		firePropertyChange("mode", null, mode);
	}

	public void addName(String name, boolean shouldBroadcast) {
		for (T4B_Player player : players) {
			if (player.getName().equalsIgnoreCase(name)) {
				return;
			}
		}
		T4B_Player player = new T4B_Player(name, generatePlayerID(name));
		players.add(player);
		firePropertyChange("playerAdded", null, player);

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
		currentVotesByPlayer.clear();
		storyVoters.clear();
		firePropertyChange("votesCleared", null, null);
	}
	public Map<String, Double> getCurrentVotesByPlayer() {
		return new HashMap<>(currentVotesByPlayer);
	}

	public void addStory(T4B_Story story) {
		for (T4B_Story s : newStories) {
			if (s.getTitle().equals(story.getTitle())) return; // already active
		}
		for (T4B_Story s : prevStories) {
			if (s.getTitle().equals(story.getTitle())) return; // already completed
		}
		newStories.add(story);
		if (currentStory == null) {
			currentStory = story;
			firePropertyChange("currentStorySet", null, currentStory);
		}
		firePropertyChange("storyAdded", null, story);
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
		firePropertyChange("newChat", null, fullMessage);
	}


	public List<String> getChatMessages() {
		return chatMessages;
	}

	public void completeCurrentStory(String title, int finalScore) {
		T4B_Story matched = null;

		for (T4B_Story story : newStories) {
			if (story.getTitle().equals(title)) {
				matched = story;
				break;
			}
		}

		if (matched != null) {
			newStories.removeIf(s -> s.getTitle().equals(title));

			matched.editScore(finalScore);
			matched.setVotesByPlayer(getCurrentVotesByPlayer());
			prevStories.add(matched);

			firePropertyChange("storyCompleted", null, matched);

			currentStory = newStories.peek();
			firePropertyChange("currentStorySet", null, currentStory);
		}
	}

	public List<Double> getCurrentVotes() {
		return currentVotes;
	}
	public void addVote(String username, double vote) {
		String title = currentStory != null ? currentStory.getTitle() : "unknown";
		storyVoters.putIfAbsent(title, new HashSet<>());
		Set<String> votersForThisStory = storyVoters.get(title);

		if (votersForThisStory.contains(username)) return;

		votersForThisStory.add(username);
		currentVotes.add(vote);
		currentVotesByPlayer.put(username, vote);
		firePropertyChange("voteAdded", null, vote);
	}

	public T4B_Story getCurrentStory() {
		return currentStory;
	}

	public void setCurrentStory(T4B_Story story) {
		this.currentStory = story;
	}

	public void clearPlayers() {
		players.clear();
		firePropertyChange("playersCleared", null, null);
	}
}