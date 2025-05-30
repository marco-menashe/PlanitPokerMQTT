package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
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

        // Listeners
        sendButton.addActionListener(this::sendMessage);
        inputField.addActionListener(this::sendMessage);

        // Register listener for new messages
        T4B_Repository.getInstance().addPropertyChangeListener(this);
        refreshChat();
    }

    private void sendMessage(ActionEvent e) {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            try {
                publisher.publishChatMessage(playerName, msg);
                inputField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to send message: " + ex.getMessage());
            }
        }
    }

    private void refreshChat() {
        List<String> messages = T4B_Repository.getInstance().getChatMessages();
        chatArea.setText(String.join("\n", messages));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("newChat".equals(evt.getPropertyName())) {
            refreshChat();
        }
    }
}
