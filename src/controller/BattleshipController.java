package controller;

import model.BattleshipModel;
import view.BattlePanel;
import view.NewGamePanel;
import view.SetShipsPanel;
import view.StartLoadPanel;

/**
 * Controller manager for the MVC paradigm. It requires a {@link BattleshipModel} instance and 
 * its task is to assign new panel controllers to the panels who require them.
 * 
 * @author 20027017 & 20031485
 *
 */
public class BattleshipController{
	
	@SuppressWarnings("unused")
	private BattleshipModel model;

	/**
	 * Constructor for the class {@code BattleshipController}.
	 * @param model A {@code BattleshipModel} object.
	 */
	public BattleshipController(BattleshipModel model) {
		this.model = model;
	}
	
	/**
	 * Returns a new {@link StartLoadController} initialized with a {@link BattleshipModel} instance
	 * and a {@link StartLoadPanel} instance.
	 * @param model A {@link BattleshipModel} instance.
	 * @param panel A {@link StartLoadPanel} instance.
	 * @return A new {@link StartLoadController}.
	 */
	public StartLoadController giveStartLoadController(BattleshipModel model, StartLoadPanel panel) {
		return new StartLoadController(model, panel);
	}
	
	/**
	 * Returns a new {@link NewGameController} initialized with a {@link BattleshipModel} instance
	 * and a {@link NewGamePanel} instance.
	 * @param model A {@link BattleshipModel} instance.
	 * @param panel A {@link NewGamePanel} instance.
	 * @return A new {@link NewGameController}.
	 */
	public NewGameController giveNewGameController(BattleshipModel model, NewGamePanel panel) {
		return new NewGameController(model, panel);
	}
	
	/**
	 * Returns a new {@link SetShipsController} initialized with a {@link BattleshipModel} instance
	 * and a {@link SetShipsPanel} instance.
	 * @param model A {@link BattleshipModel} instance.
	 * @param panel A {@link SetShipsPanel} instance.
	 * @return A new {@link SetShipsController}.
	 */
	public SetShipsController giveSetShipsController(BattleshipModel model, SetShipsPanel panel) {
		return new SetShipsController(model, panel);
	}
	
	/**
	 * Returns a new {@link BattleController} initialized with a {@link BattleshipModel} instance
	 * and a {@link BattlePanel} instance.
	 * @param model A {@link BattleshipModel} instance.
	 * @param panel A {@link BattlePanel} instance.
	 * @return A new {@link BattleController}.
	 */
	public BattleController giveBattleController(BattleshipModel model, BattlePanel panel) {
		return new BattleController(model, panel);
	}
}