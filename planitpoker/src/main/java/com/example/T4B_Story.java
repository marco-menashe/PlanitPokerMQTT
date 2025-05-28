package com.example;


/**
 * Represents a story in the T4B system with a title and score.
 * 
 * @author Marco Menashe
 */
public class T4B_Story {
    private String title;
  
    private int score;

    private String username;

    public T4B_Story(String title, int score, String username) {
        this.title = title;
        this.score = score;
        this.username = username;

    }

    public String getTitle() { return title; }
    public int getScore() { return score; }
    public void editScore(int score) { this.score = score; }

    public void editTitle(String title) { this.title = title; }
    


}
