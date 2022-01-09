package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileNotFoundException;

import javax.swing.JButton;

import model.BattleshipModel;
import utils.BattleshipState;
import view.NewGamePanel;
import view.StartLoadPanel;

/**
 * Controller for a {@link StartLoadPanel}. According to the pressed button, 
 * it can either trigger a file loading or a new game setting.
 * @author 20027017 & 20031485
 *
 */
public class StartLoadController implements ActionListener{
	
	private BattleshipModel model;
	@SuppressWarnings("unused")
	private StartLoadPanel startLoadPanel;
	
	/**
	 * Constructor for the class {@link StartLoadController} initialized with a
	 * {@link BattleshipModel} instance and a {@link StartLoadPanel}.
	 * 
	 * @param model A {@link BattleshipModel} instance.
	 * @param startLoadPanel A {@link StartLoadPanel} instance.
	 */
	public StartLoadController(BattleshipModel model, StartLoadPanel startLoadPanel) {
		this.model = model;
		this.startLoadPanel = startLoadPanel;
	}
	
	@Override
	/**
	 * When an {@link ActionEvent} is detected, according to the button
	 * pressure which fired it, this method can either initialize a {@link BattleshipModel}
	 * object from an existing ".dat" file through its static {@code loadGame()} method or
	 * start a new game from scratch. In both cases, the {@link ActionEvent} detection
	 * triggers a state change in the {@link BattleshipModel}'s state, activating the 
	 * {@code setVisible()} methods of the previous and next panels in the panel 
	 * sequence.
	 * 
	 * @param e An {@link ActionEvent} fired when a button on the 
	 * {@link NewGamePanel} is pressed. 
	 */
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		String command = source.getText();
		switch(command) {
			case "New game":
				System.out.println("new game");
				//il modello viene messo in stato NEWGAME e il NewGamePanel si rende visibile
				model.setState(BattleshipState.NEWGAME);
				break;
				
			case "Load game":
				try {
					BattleshipModel m = BattleshipModel.loadGame();
					model.newGame(m.getPlayer(), m.getComputer(), m.getGameSize(), m.isTimed(), m.getSecs());
					model.setState(BattleshipState.BATTLE);
				} 
				catch (FileNotFoundException e1) {
					startLoadPanel.loadGameButton.setVisible(false);
				}
				break;
				
			default:
				System.err.println("ERROR@StartLoadController::actionPerformed()");
		}
	}
}
