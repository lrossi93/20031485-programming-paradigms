package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import model.BattleshipModel;
import model.Computer;
import model.Player;
import utils.BattleshipState;
import utils.ComputerType;

class BattleshipModelTest {

	@Test
	void testSaveLoad() {
		int gameSize = 10;
		Player p1 = new Player(gameSize);
		Computer p2 = new Computer(gameSize, ComputerType.STUPID);
		p1.randomSetShips();
		p2.randomSetShips();
		
		BattleshipModel bm = new BattleshipModel(p1, p2, gameSize, false, 0);
		assert(bm != null);
		
		assertFalse(bm.isJustSaved());
		
		bm.saveGame();
		
		assertTrue(bm.isJustSaved());
		
		//savedGameExist test
		assert(BattleshipModel.savedGameExists());
		
		//saveGame & loadGame test
		BattleshipModel bm2 = null;
		try {
			bm2 = BattleshipModel.loadGame();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(bm.toString());
		assertEquals(bm, bm2);
		
		//newGame test
		BattleshipModel bm3 = new BattleshipModel();
		bm3.newGame(p1, p2, gameSize, false, 0);
		assertEquals(bm, bm3);
	}
	
	@Test
	void testState() {
		int gameSize = 10;
		BattleshipModel bm = new BattleshipModel(new Player(gameSize), new Computer(gameSize, ComputerType.SMART), gameSize, false, 0);
		
		assertEquals(bm.getState(), BattleshipState.WELCOME);
		
		bm.setState(BattleshipState.BATTLE);
		
		assertEquals(bm.getState(), BattleshipState.BATTLE);
	}
	
	@Test
	void testNewGame() {
		int gameSize = 10;
		int gameSize2 = 20;
		BattleshipModel bm = new BattleshipModel(new Player(gameSize), new Computer(gameSize, ComputerType.SMART), gameSize, false, 0);
		assertEquals(bm.getGameSize(), gameSize);
		assertEquals(bm.getPlayer().getGameSize(), gameSize);
		assertEquals(bm.getComputer().getGameSize(), gameSize);
		assertFalse(bm.isTimed());
		assertEquals(bm.getSecs(), 0);
		
		bm.newGame(new Player(gameSize2), new Computer(gameSize2, ComputerType.STUPID), gameSize2, true, 50);
		assertEquals(bm.getGameSize(), gameSize2);
		assertEquals(bm.getPlayer().getGameSize(), gameSize2);
		assertEquals(bm.getComputer().getGameSize(), gameSize2);
		assertTrue(bm.isTimed());
		assertEquals(bm.getSecs(), 50);
		
	}

}
