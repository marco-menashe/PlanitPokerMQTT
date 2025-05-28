package com.example;

import java.util.LinkedList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

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
	private final List<T4B_Player> players;
	// game state
	private T4B_Repository(){
		pcs = new PropertyChangeSupport(this);
	}

	public void addStory(){}

	public void addCurrentRoom(){}

	public void addCurrentMode(){}

	public static T4B_Repository getInstance() {
		if (instance == null){
			instance = new T4B_Repository();
		}
		return instance;
	}


}

