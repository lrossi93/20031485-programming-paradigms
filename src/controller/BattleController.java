package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;

import model.BattleshipModel;
import view.BattlePanel;

/**
 * Controller for a {@link BattlePanel} object. It listens for actions
 * fired by a {@code BattlePanel} and either saves the game or tries and
 * hit a {@link model.Computer}'s ship, according to the pressed button.
 * 
 * @author 20027017 & 20031485
 *
 */
public class BattleController implements ActionListener{

	private BattleshipModel model;
	private BattlePanel battlePanel;
	
	/**
	 * Constructor for the class {@link BattleController}.
	 * @param model A {@link BattleshipModel} object.
	 * @param battlePanel A {@link BattlePanel}.
	 */
	public BattleController(BattleshipModel model, BattlePanel battlePanel) {
		this.model = model;
		this.battlePanel = battlePanel;
	}
	
	/**
	 * Performs an action according to the pressed button.
	 * @param e An {@link ActionEvent} fired by a pressed key on the {@link BattlePanel}.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		int gameSize = model.getGameSize();
		int row, col;
		String command = source.getText();
		
		//if user clicked "SAVE GAME"
		if(command.equals("SAVE GAME")) {
			if(model.isTimed())
				//se il modello è a tempo, ottengo il tempo mancante al termine della partita
				//dal battlePanel (che contiene CountdownPanel). CountdownPanel lavora
				//con i millisecondi, quindi devo trasformare millisecondi-->secondi facendo /1000
				//imposto quindi il tempo mancante al termine nel modello
				model.setSecs(battlePanel.getSecsLeft()/1000);
			model.saveGame();
			BattleshipModel bm = null;
			
			try {
				bm = BattleshipModel.loadGame();
				bm.print();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
		}
		//if user clicked a grid button
		else {
			for(int i = 0; i < gameSize; ++i) {
				for(int j = 0; j < gameSize; ++j) {
					if(source == battlePanel.getButtonFromHitsButtonGrid(i, j)) {
						row = i;
						col = j;
						model.hitAndGetHit(row, col);
					}
				}
			}
		}
	}
}
