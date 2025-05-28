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
public class Repository {


	private static Repository instance;

	private final PropertyChangeSupport pcs;

	private String roomID;
	private final List<Player> players;
	// game state
	private Repository(){
		this.pcs = new PropertyChangeSupport()
	}

	public static com.example.Repository getInstance() {
		if (instance == null){
			instance = new Repository();
		}
		return instance;
	}


}

