package com.example;

/**
 * Represents a story in the T4B system with a title and score.
 *
 * @author Marco Menashe
 */

public class T4B_Story {
    private String title;
    private int score;

    public T4B_Story(String title, int score) {
        this.title = title;
        this.score = score;
    }

    public String getTitle() { return title; }
    public int getScore() { return score; }
    public void editScore(int score) { this.score = score; }

    public void editTitle(String title) { this.title = title; }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        T4B_Story story = (T4B_Story) obj;
        return title.equals(story.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}