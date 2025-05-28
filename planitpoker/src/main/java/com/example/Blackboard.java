package com.example;

import java.util.LinkedList;

/**
 * Shared data structure for the application.
 *
 * @author javiergs
 */
public class Blackboard {
	
	private static LinkedList<String> names = new LinkedList<>();
	private static LinkedList<String> stories = new LinkedList<>();
	private static String currentRoom;
	private static String mode;
	
	public static void addName(String name) {
		names.add(name);
	}
	
	public static void addStory(String story) {
		stories.add(story);
	}
	
	public static void addCurrentRoom(String name) {
		currentRoom = name;
	}
	
	public static void addCurrentMode(String selectedItem) {
		mode = selectedItem;
	}

}

