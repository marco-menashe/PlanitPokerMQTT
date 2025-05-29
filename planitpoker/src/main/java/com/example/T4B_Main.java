package com.example;

import javax.swing.JFrame;

/**
 * Main class to create a JFrame and display the login panel.
 *
 * @author AdrianSanchez
 *
 * /** */
public class T4B_Main extends JFrame {

	public T4B_Main() {
		setTitle("Welcome");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);

		T4B_LoginNanny loginNanny = new T4B_LoginNanny(this);
		setContentPane(new T4B_LoginPanel(loginNanny));


		T4B_DashboardNanny dashboardNanny = new T4B_DashboardNanny(null);
		T4B_CardsPanel cardsPanel = new T4B_CardsPanel(dashboardNanny);
		dashboardNanny.setCardsPanel(cardsPanel);

		T4B_DashboardPanel dashboardPanel = new T4B_DashboardPanel(dashboardNanny);
		add(dashboardPanel);

		T4B_Repository.getInstance().addPropertyChangeListener(evt -> {
			System.out.println("REPO UPDATE: " + evt.getPropertyName() + " -> " + evt.getNewValue());
		});
	}

	public static void main(String[] args) {
//		T4B_Main main = new T4B_Main();
//		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		main.setSize(400, 400);
//		main.setLocationRelativeTo(null);
//		main.setVisible(true);
		new T4B_Main().setVisible(true);
	}
}

