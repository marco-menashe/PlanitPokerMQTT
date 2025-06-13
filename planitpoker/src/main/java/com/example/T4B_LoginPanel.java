package com.example;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Create a panel for user login.
 *
 * @author Marco
 */

public class T4B_LoginPanel extends JPanel {

    public T4B_LoginPanel(T4B_LoginNanny joinRoomNanny) {
        JLabel titleLabel = new JLabel("Let's start!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel subtitleLabel = new JLabel("Join the room:");
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField nameField = new JTextField("Enter your name");
        JPasswordField passwordField = new JPasswordField("Enter your password");
        JButton loginButton = new JButton("Login");

        setLayout(new GridLayout(7, 1));
        add(titleLabel);
        add(subtitleLabel);
        add(nameField);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(e -> joinRoomNanny.login(nameField.getText(), new String(passwordField.getPassword())));
    }
}