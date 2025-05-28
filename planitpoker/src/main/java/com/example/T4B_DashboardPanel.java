package com.example;

import javax.swing.*;
import java.awt.*;

/**
 * Integrates a dashboard with the cards, timer, and stories.
 *
 * @author javiergs
 */
public class DashboardPanel extends JPanel {
    private SouthPanel southPanel;

    public DashboardPanel(DashboardNanny dashboardNanny) {
        setLayout(new BorderLayout());

        CardsPanel cardsPanel = new CardsPanel(dashboardNanny);
        dashboardNanny.setCardsPanel(cardsPanel);

        southPanel = new SouthPanel();

        add(cardsPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(new WestPanel(dashboardNanny, this), BorderLayout.EAST);
    }

    public void updateResults(){
        southPanel.updateResults();
    }
}

