package org.planit;

public class T4B_Card {

    private int value;
    private boolean isRevealed;

    public Card(int value) {
        this.value = value;
        this.isRevealed = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        this.isRevealed = true;
    }

    public void hide() {
        this.isRevealed = false;
    }
}
