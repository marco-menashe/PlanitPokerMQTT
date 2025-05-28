package com.example;

import javax.swing.JFrame;

/**
 * Main class to create a JFrame and display the login panel.
 *
 * @author javiergs
 *
 * /** */
public class T4B_Main extends JFrame {
	
	public T4B_Main() {
		LoginNanny loginNanny = new LoginNanny(this);
		LoginPanel loginPanel = new LoginPanel(loginNanny);
		add(loginPanel);
	}
	
	public static void main(String[] args) {
		T4B_Main main = new T4B_Main();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(400, 400);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
	
}

