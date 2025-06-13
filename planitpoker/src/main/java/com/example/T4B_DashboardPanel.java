package com.example;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;


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

        JButton showAllVotesChartButton = new JButton("All Players' Votes");
        showAllVotesChartButton.addActionListener(e -> showAllVotesChart());
        topPanel.add(showAllVotesChartButton);

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
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (T4B_Story story : completedStories) {
            Map<String, Double> votes = story.getVotesByPlayer();
            if (votes.containsKey(playerName)) {
                double vote = votes.get(playerName);
                sb.append("• ").append(story.getTitle())
                        .append(" → ").append(vote)
                        .append("\n");
                dataset.addValue(vote, "Vote", story.getTitle());
                hasVotes = true;
            }
        }
        if (!hasVotes) {
            JOptionPane.showMessageDialog(null, "No votes recorded for this player.");
            return;
        }

        JOptionPane.showMessageDialog(null, sb.toString(), playerName + "'s Vote History", JOptionPane.INFORMATION_MESSAGE);

        JFreeChart barChart = ChartFactory.createBarChart(
                playerName + "'s Votes",
                "Story",
                "Vote Value",
                dataset
        );

        var renderer = (org.jfree.chart.renderer.category.BarRenderer)
                barChart.getCategoryPlot().getRenderer();
        renderer.setSeriesPaint(0, new Color(30, 144, 255)); // DodgerBlue
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        barChart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        barChart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 5)
        );

        barChart.setBackgroundPaint(Color.WHITE);
        barChart.getPlot().setBackgroundPaint(Color.BLACK);
        barChart.getPlot().setOutlineVisible(false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(1000, 600)); // Expand space for long labels

        JFrame frame = new JFrame(playerName + "'s Vote Chart");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
    private void showAllVotesChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<T4B_Story> stories = T4B_Repository.getInstance().getPrevStories();
        List<T4B_Player> players = T4B_Repository.getInstance().getPlayers();

        for (T4B_Story story : stories) {
            String storyTitle = story.getTitle();
            Map<String, Double> votes = story.getVotesByPlayer();

            for (T4B_Player player : players) {
                String playerName = player.getName();
                if (votes.containsKey(playerName)) {
                    dataset.addValue(votes.get(playerName), playerName, storyTitle);
                }
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "All Players' Votes by Story",
                "Story",
                "Vote Value",
                dataset
        );

        barChart.setBackgroundPaint(Color.WHITE);
        barChart.getPlot().setBackgroundPaint(Color.BLACK);
        barChart.getPlot().setOutlineVisible(false);

        org.jfree.chart.renderer.category.BarRenderer renderer =
                (org.jfree.chart.renderer.category.BarRenderer) barChart.getCategoryPlot().getRenderer();
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);

        barChart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 5)
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(1100, 600));

        JFrame frame = new JFrame("All Player Votes");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

