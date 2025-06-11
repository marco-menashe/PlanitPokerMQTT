package com.example;
import com.example.T4B_Player;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * Integrates a dashboard with the cards, timer, and stories.
 *
 * @author Aidan
 */
public class T4B_DashboardPanel extends JPanel {
    private T4B_SouthPanel southPanel;

    private T4B_ChatPanel chatPanel;

    public T4B_DashboardPanel(T4B_DashboardNanny dashboardNanny) {
        setLayout(new BorderLayout());

        T4B_CardsPanel cardsPanel = new T4B_CardsPanel(dashboardNanny);
        dashboardNanny.setCardsPanel(cardsPanel);

        southPanel = new T4B_SouthPanel();

        add(cardsPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(new T4B_WestPanel(dashboardNanny, this), BorderLayout.EAST);

        dashboardNanny.setDashboardPanel(this);
        String playerName = T4B_Repository.getInstance().getPlayers().get(0).getName();
        chatPanel = new T4B_ChatPanel(
                T4B_Repository.getInstance().getPublisher(),
                playerName
        );
        add(chatPanel, BorderLayout.WEST);

        JButton showPlayersButton = new JButton("Show Players");
        showPlayersButton.addActionListener(e -> {
            List<T4B_Player> players = T4B_Repository.getInstance().getPlayers();
            StringBuilder sb = new StringBuilder("Current Players:\n");
            for (T4B_Player p : players) {
                sb.append(p.getName()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Players", JOptionPane.INFORMATION_MESSAGE);
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(showPlayersButton);

        add(topPanel, BorderLayout.NORTH);

    }

    public void updateResults() {
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

    public T4B_SouthPanel getSouthPanel() {
        return southPanel;
    }
}
