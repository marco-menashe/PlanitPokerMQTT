package com.example;

import javax.swing.JFrame;

/**
 * Main class to create a JFrame and display the login panel.
 *
 * @author AdrcianSanchez
 *
 */

public class T4B_Main extends JFrame {

	public T4B_Main() {
		setTitle("Welcome");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);

		T4B_LoginNanny loginNanny = new T4B_LoginNanny(this);
		setContentPane(new T4B_LoginPanel(loginNanny));
	}

	public static void main(String[] args) {
		new T4B_Main().setVisible(true);
	}
}