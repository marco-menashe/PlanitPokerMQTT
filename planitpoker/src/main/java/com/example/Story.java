package three;

public class Story {
    private String title;
  
    private int score;

    public Story(String title, int score) {
        this.title = title;
        this.score = score;

    }

    public String getTitle() { return title; }
    public int getScore() { return score; }
    public void editScore(int score) { this.score = score; }

    public void editTitle(String title) { this.title = title; }
    


}
