package com.example;

import java.awt.*;

import javax.swing.JPanel;

/**
 * Integrates a dashboard with the cards, timer, and stories.
 *
 * @author javiergs
 */
public class T4B_DashboardPanel extends JPanel {
    private T4B_SouthPanel southPanel;

    public T4B_DashboardPanel(T4B_DashboardNanny dashboardNanny) {
        setLayout(new BorderLayout());

        T4B_CardsPanel cardsPanel = new T4B_CardsPanel(dashboardNanny);
        dashboardNanny.setCardsPanel(cardsPanel);

        southPanel = new T4B_SouthPanel();

        add(cardsPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(new T4B_WestPanel(dashboardNanny, this), BorderLayout.EAST);
    }

    public void updateResults(){
        southPanel.updateResults();
    }

    public T4B_CardsPanel getCardsPanel() {
        for (Component comp : getComponents()) {
            if (comp instanceof T4B_CardsPanel) {
                return (T4B_CardsPanel) comp;
            }
        }
        return null;
    }

}

