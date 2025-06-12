package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code T4B_ChatPanel} class represents a chat interface.

 * <p>This class listens for new chat messages via a property change listener on the shared {@link T4B_Repository}.
 * Messages are published through a {@link T4B_Publisher} instance.
 *
 * @author Adrian Sanchez
 */
public class T4B_ChatPanel extends JPanel implements PropertyChangeListener {

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private final T4B_Publisher publisher;
    private final String playerName;
    private static final Logger logger = LoggerFactory.getLogger(T4B_ChatPanel.class);

    public T4B_ChatPanel(T4B_Publisher publisher, String playerName) {
        this.publisher = publisher;
        this.playerName = playerName;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 300)); // Resize as needed

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(this::sendMessage);
        inputField.addActionListener(this::sendMessage);

        T4B_Repository.getInstance().addPropertyChangeListener(this);
        refreshChat();
    }

    private void sendMessage(ActionEvent e) {
        String msg = inputField.getText().trim();
        if (msg.isEmpty()) {
            logger.debug("sendMessage: empty message ignored");
            return;
        }
        logger.info("Sending chat message from '{}' → {}", playerName, msg);
        try {
            //logger.info("Publishing chat from {}: {}", playerName, msg);
            publisher.publishChatMessage(playerName, msg);
            inputField.setText("");
            logger.debug("sendMessage: publish succeeded");
        } catch (Exception ex) {
            logger.error("sendMessage: publish FAILED for '{}': {}", playerName, ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Failed to send message: " + ex.getMessage());
        }
    }

    private void refreshChat() {
        List<String> messages = T4B_Repository.getInstance().getChatMessages();
        logger.debug("Refreshing chat UI with {} messages", messages.size());
        chatArea.setText(String.join("\n", messages));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("newChat".equals(evt.getPropertyName())) {
            refreshChat();
        }
    }
}
