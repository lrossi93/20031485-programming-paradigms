package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import model.BattleshipModel;
import view.SetShipsPanel;
import utils.BattleshipState;
import utils.ShipDirection;

/**
 * Controller for a {@link SetShipsPanel}. According to the user's settings,
 * it initializes a {@link model.Player}'s ship grid by positioning its available {@link model.Ship}s.
 * 
 * @author 20027017 & 20031485
 *
 */
public class SetShipsController implements ActionListener{

	private BattleshipModel model;
	private SetShipsPanel setShipsPanel;
	
	/**
	 * Constructor for the class {@link SetShipsController} initialized with a
	 * {@link BattleshipModel} instance and a {@link SetShipsPanel}.
	 * 
	 * @param model A {@link BattleshipModel} instance.
	 * @param setShipsPanel A {@link SetShipsPanel} instance.
	 */
	public SetShipsController(BattleshipModel model, SetShipsPanel setShipsPanel) {
		this.model = model;
		this.setShipsPanel = setShipsPanel;
	}
	

	@Override
	/**
	 * When an {@link ActionEvent} is detected, according to the button
	 * pressure which fired it, this method can either set a {@link Ship}, 
	 * set all the available {@link Ship}s randomly, clear all the {@link Ship}s
	 * from the grid or, when all {@link Ship}s are set, enable a "PLAY" button which,
	 * if pressed, triggers a state change in the {@link BattleshipModel}.
	 * 
	 * @param e An {@link ActionEvent} fired when a button on the 
	 * {@link SetShipsPanel} is pressed.
	 */
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		int gameSize = model.getGameSize();
		int row, col;
		String command = source.getText();
		
		if(command.equals("RANDOM")) {
			//tolgo tutte le navi già posizionate
			model.getPlayer().clearShips();
			//riposiziono tutte le navi casualmente
			model.getPlayer().randomSetShips();
		}
		
		if(command.equals("CLEAR")) {
			model.getPlayer().clearShips();
		}
		
		if(command.equals("PLAY")) {
			//posiziona casualmente le navi del computer
			model.getComputer().randomSetShips();
			//passa allo stato BATTLE
			model.setState(BattleshipState.BATTLE);
		}
		
		if(command.equals("BACK")) {
			model.setState(BattleshipState.NEWGAME);
		}
		
		else {
			for(int i = 0; i < gameSize; ++i) {
				for(int j = 0; j < gameSize; ++j) {
					if(source == setShipsPanel.getButtonFromButtonGrid(i, j)) {
						row = i;
						col = j;

						int shipIndex = setShipsPanel.getChooseShip().getSelectedIndex();
						
						ShipDirection direction;
						int directionIndex = setShipsPanel.getChooseDirection().getSelectedIndex();
						
						if(directionIndex == 0)
							direction = ShipDirection.VERTICAL;
						else
							direction = ShipDirection.HORIZONTAL;
						
						model.getPlayer().setShip(shipIndex, row, col, direction);
					}
				}
			}
		}
	}
}