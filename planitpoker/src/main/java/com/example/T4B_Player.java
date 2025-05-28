package com.example;



/**
 * Represents a player in the T4B system with a name and player ID.
 * 
 * @author Marco Menashe
 */
public class T4B_Player{

    private String name;
    private String playerID;

    public T4B_Player(String name, String playerID) {
        this.name = name;
        this.playerID = playerID;
        
    }

    public String getName() {
        return name;
    }

    public String getPlayerID() {
        return playerID;
    }




}
