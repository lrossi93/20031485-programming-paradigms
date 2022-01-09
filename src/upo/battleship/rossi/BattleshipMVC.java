package upo.battleship.rossi;

import model.BattleshipModel;
import view.BattleshipView;

/**
 * The main of the Battleship application.
 * 
 * @author 20027017 & 20031485
 *
 */
public class BattleshipMVC {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		BattleshipModel model = new BattleshipModel();
		BattleshipView view = new BattleshipView(model);
	}
}
