package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;

import model.BattleshipModel;
import model.Computer;
import model.Player;
import utils.BattleshipState;
import utils.ComputerType;
import view.NewGamePanel;

/**
 * Controller for a {@link NewGamePanel}. According to the user's settings,
 * it initializes a {@link BattleshipModel} object through its {@code newGame()} method.
 * 
 * @author 20027017 & 20031485
 *
 */
public class NewGameController implements ActionListener{
	//attributes
	private BattleshipModel model;
	private NewGamePanel newGamePanel;

	/**
	 * Constructor for the class {@link NewGameController} initialized with a
	 * {@link BattleshipModel} instance and a {@link NewGamePanel}.
	 * 
	 * @param model A {@link BattleshipModel} instance.
	 * @param newGamePanel A {@link NewGamePanel} instance.
	 */
	public NewGameController(BattleshipModel model, NewGamePanel newGamePanel){
		this.model = model;
		this.newGamePanel = newGamePanel;
	}
	

	@Override
	/**
	 * When an {@link ActionEvent} is detected, according to the button
	 * pressure which fired it, this method can initialize a {@link BattleshipModel}
	 * object through its {@code newGame()} method. If other buttons were pressed, it 
	 * may change the {@link BattleshipModel}'s state, triggering the {@code setVisible()}
	 * methods of the previous and next panels in the panel sequence.
	 * 
	 * @param e An {@link ActionEvent} fired when a button on the 
	 * {@link NewGamePanel} is pressed. 
	 */
	public void actionPerformed(ActionEvent e) {
		//abstractButton to get both JButtons and JRadioButtons
		AbstractButton source = (AbstractButton)e.getSource();
		String command = source.getText();
		switch(command) {
			case "CONFIRM":
				if(newGamePanel.p1vsp2Button.isSelected()) {
					//feature unavailable
					model.setState(BattleshipState.NEWGAME);
				}
				else {
					int gameSize = 10;
					int secs = 600;
					ComputerType computerType = ComputerType.STUPID;
					boolean timed = false;
					if(newGamePanel.p1vsCPUButton.isSelected()) {
						
						if(newGamePanel.easyModeButton.isSelected())
							computerType = ComputerType.STUPID;
						
						if(newGamePanel.hardModeButton.isSelected())
							computerType = ComputerType.SMART;
						
						if(newGamePanel.sizeMButton.isSelected())
							gameSize = 10;
						
						if(newGamePanel.sizeLButton.isSelected())
							gameSize = 15;
						
						if(newGamePanel.sizeXLButton.isSelected())
							gameSize = 20;
						
						if(newGamePanel.timedCheckBox.isSelected())
							timed = true;
						
						if(newGamePanel.timed1minsButton.isSelected())
							secs = 1*60;
						
						if(newGamePanel.timed2minsButton.isSelected())
							secs = 2*60;
						
						if(newGamePanel.timed5minsButton.isSelected())
							secs = 5*60;
						
						//una volta che ho impostato i parametri, chiamo newGame con tutti i parametri impostati
						model.newGame(new Player(gameSize), new Computer(gameSize, computerType), gameSize, timed, secs);

					}
					model.setState(BattleshipState.SETSHIPS);
				}
				break;
				
			case "BACK":
				System.out.println("back to start/load");
				model.setState(BattleshipState.WELCOME);
				break;
				
			case "P1vsP2 (unavailable)":
				newGamePanel.difficultyPanel.setVisible(false);;
				break;
				
			case "P1vsCPU":
				newGamePanel.difficultyPanel.setVisible(true);
				break;
				
			case "TIMED":
				if(newGamePanel.timedCheckBox.isSelected())
					newGamePanel.timedPanel.setVisible(true);
				else
					newGamePanel.timedPanel.setVisible(false);
				break;
				
			default:
				System.out.println("error in newGameController");
				//TODO throw exception?
				break;
		}
	}
}
