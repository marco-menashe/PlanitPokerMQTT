package com.example;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Shared data structure for the application.
 *
 * @author AdrianSanchez
 * @version 1.0
 */
public class T4B_Repository {


	private static T4B_Repository instance;
	private final PropertyChangeSupport pcs;

	private String roomID;
	private String mode;

	private final List<T4B_Player> players;
	private final List<String> chatMessages;
	private final Queue<T4B_Story> newStories;
	private final LinkedList<T4B_Story> prevStories;

	private final List<Double> currentVotes;

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

	public void addCurrentRoom(String roomID){
		this.roomID = roomID;
		pcs.firePropertyChange("roomID", null, roomID);
	}

	public void addCurrentMode(String mode){
		this.mode = mode;
		pcs.firePropertyChange("mode", null, mode);
	}

	public void addName(String name){
		T4B_Player player = new T4B_Player(name, generatePlayerID(name));
		players.add(player);
		pcs.firePropertyChange("playerAdded", null, player);
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

	public static double calculateAverage(){
		List<Double> votes = getInstance().currentVotes;
		if (votes.isEmpty()) return 0.0;
		double total = 0;
		int count = 0;

		for (double v : votes){
			if(!Double.isNaN(v)){
				total += v;
				count ++;
			}
		}
		return count == 0 ? 0 : total / count;
	}

	public void clearVotes(){
		currentVotes.clear();
		pcs.firePropertyChange("votesCleared", null, null);
	}

	public void addStory(T4B_Story story){
		newStories.add(story);
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
}

