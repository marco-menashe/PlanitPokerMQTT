package com.example;


/**
 * Controller responsible for managing the dashboard and its interactions.
 *
 * @author javiergs
 */
public class DashboardNanny {
    private String currentVote;
    private boolean voteConfirmed = false;
    private CardsPanel cardsPanel;

    public DashboardNanny(){
    }

    public void setCardsPanel(CardsPanel cardsPanel){
        this.cardsPanel = cardsPanel;
    }

    public void setCurrentVote(String vote){
        if(!voteConfirmed){
            currentVote = vote;
        }
    }

    public void confirmVote(){
        if(currentVote != null){
            voteConfirmed = true;
            try {
                double numericVote = convertVoteToNumber(currentVote);
                Blackboard.addVote(numericVote);
                if(cardsPanel != null){
                    cardsPanel.lockSelection();
                }
            } catch(NumberFormatException e){
                System.out.println("Vote was not a number -> skipped");
            }
        }
    }

    public void startNewVote(){
        voteConfirmed = false;
        currentVote = null;
        if(cardsPanel != null){
            cardsPanel.resetCards();
        }
    }

    public boolean isVoteConfirmed(){
        return voteConfirmed;
    }

    private double convertVoteToNumber(String vote) throws NumberFormatException{
        return switch (vote){
            case "½" -> 0.5;
            case "?" -> Double.NaN; //not sure what to do here, any number will affect avg score
            default -> Double.parseDouble(vote);
        };
    }
}
