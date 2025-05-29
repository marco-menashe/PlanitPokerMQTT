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
		T4B_LoginNanny loginNanny = new T4B_LoginNanny(this);
		T4B_LoginPanel loginPanel = new T4B_LoginPanel(loginNanny);
		add(loginPanel);

		// 🔧 Add listener for debug/testing
		T4B_Repository.getInstance().addPropertyChangeListener(evt -> {
			System.out.println("REPO UPDATE: " + evt.getPropertyName() + " -> " + evt.getNewValue());
		});
	}

	public static void main(String[] args) {
		T4B_Main main = new T4B_Main();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setSize(400, 400);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
}

