package com.example;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * Panel that displays the cards used for estimating.
 *
 * @author
 */
public class T4B_CardsPanel extends JPanel {
    private static final String[] CARD_VALUES = {
            "0", "½", "1", "2", "3", "5", "8", "20", "40", "10", "0", "?"
    };

    private final T4B_DashboardNanny dashboardNanny;
    private JButton selectedCard;
    private static final Color DEFAULT_COLOR = new Color(172, 248, 199);
    private static final Color HOVER_COLOR = new Color(200, 200, 200);
    private static final Color SELECTED_COLOR = Color.YELLOW;
    private static final Color CONFIRMED_COLOR = Color.GREEN;

    public T4B_CardsPanel(T4B_DashboardNanny dashboardNanny) {
        this.dashboardNanny = dashboardNanny;
        setLayout(new GridLayout(4, 3, 10, 10));

        for (String value : CARD_VALUES) {
            JButton card = new JButton(value);
            styleCard(card);
            add(card);

            card.addActionListener(e -> handleCardSelection(card));
            addHoverEffect(card);
        }
    }

    private void styleCard(JButton card) {
        card.setBackground(DEFAULT_COLOR);
        card.setFont(new Font("SansSerif", Font.BOLD, 20));
        card.setFocusPainted(false);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void addHoverEffect(JButton card){
        card.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                if(!dashboardNanny.isVoteConfirmed() && card != selectedCard){
                    card.setBackground(HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e){
                if(!dashboardNanny.isVoteConfirmed() && card != selectedCard){
                    card.setBackground(DEFAULT_COLOR);
                }
            }
        });
    }

    private void handleCardSelection(JButton card){
        if(dashboardNanny.isVoteConfirmed()){
            return;
        }

        if(selectedCard != null){
            selectedCard.setBackground(DEFAULT_COLOR);
        }

        selectedCard = card;
        card.setBackground(SELECTED_COLOR);
        dashboardNanny.setCurrentVote(card.getText());
    }

    public void lockSelection(){
        if(selectedCard != null){
            selectedCard.setBackground(CONFIRMED_COLOR);
        }
        for(Component comp : getComponents()){
            if(comp instanceof JButton){
                comp.setEnabled(false);
            }
        }
    }

    public void resetCards(){
        selectedCard = null;
        for(Component comp : getComponents()){
            if(comp instanceof JButton){
                JButton btn = (JButton) comp;
                btn.setEnabled(true);
                btn.setBackground(DEFAULT_COLOR);
            }
        }
    }
}