package utils;

import java.io.Serializable;

/**
 * Utility Enum containing each possible state for a {@link model.BattleshipModel} object.
 * @author 20027017 & 20031485
 *
 */
public enum BattleshipState implements Serializable{
	WELCOME, 		//start/load screen
	NEWGAME, 		//new game settings screen
	SETNAMES, 		//for future implementations
	SETSHIPS,		//screen for setting ships
	BATTLE;			//displays battle panel
}
