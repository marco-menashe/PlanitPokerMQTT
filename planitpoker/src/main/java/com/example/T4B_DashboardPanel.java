package com.example;
import com.example.T4B_Player;

import java.awt.*;
import java.util.List;
import java.util.Map;
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
        JButton showCompletedButton = new JButton("Show Completed Stories");
        showCompletedButton.addActionListener(e -> {
            List<T4B_Story> completed = T4B_Repository.getInstance().getPrevStories();
            if (completed.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No completed stories yet.");
                return;
            }

            StringBuilder sb = new StringBuilder("Completed Stories:\n\n");
            for (T4B_Story story : completed) {
                sb.append("• ").append(story.getTitle())
                        .append(" — Average: ").append(story.getScore())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString(), "Results", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton viewPlayerVotesButton = new JButton("View Player Votes");
        viewPlayerVotesButton.addActionListener(e -> {
            List<T4B_Player> players = T4B_Repository.getInstance().getPlayers();

            if (players.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No players available.");
                return;
            }

            // Dropdown list of players
            String[] playerNames = players.stream()
                    .map(T4B_Player::getName)
                    .toArray(String[]::new);

            String selectedPlayer = (String) JOptionPane.showInputDialog(
                    null,
                    "Select a player to view their votes:",
                    "Player Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    playerNames,
                    playerNames[0]);

            if (selectedPlayer != null) {
                showPlayerVoteHistory(selectedPlayer);
            }
        });

        topPanel.add(viewPlayerVotesButton);

        topPanel.add(showCompletedButton);


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
    private void showPlayerVoteHistory(String playerName) {
        List<T4B_Story> completedStories = T4B_Repository.getInstance().getPrevStories();
        StringBuilder sb = new StringBuilder(playerName + "'s Votes:\n\n");

        boolean hasVotes = false;

        for (T4B_Story story : completedStories) {
            Map<String, Double> votes = story.getVotesByPlayer();
            if (votes.containsKey(playerName)) {
                sb.append("• ").append(story.getTitle())
                        .append(" → ").append(votes.get(playerName))
                        .append("\n");
                hasVotes = true;
            }
        }

        if (!hasVotes) {
            sb.append("No votes recorded for this player.");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), playerName + "'s Vote History", JOptionPane.INFORMATION_MESSAGE);
    }

}
