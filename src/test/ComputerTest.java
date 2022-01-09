package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Computer;
import model.Player;
import utils.ComputerType;
import utils.PlayerState;

class ComputerTest {

	@Test
	void testConstructor() {
		int gameSize = 10;
		Computer c = new Computer(gameSize, ComputerType.STUPID);
		Player p = new Player(gameSize);
		assert c != null;
		assertEquals(c.getDifficulty(), ComputerType.STUPID);
		assertEquals(c.getCoordinatesList().size(), gameSize*gameSize);
		assertTrue(c.getNextHits().isEmpty());
	}
	
	@Test
	void testStupidComputer() {
		int gameSize = 10;
		Computer c = new Computer(gameSize, ComputerType.STUPID);
		Player p = new Player(gameSize);
		assert c != null;
		assertEquals(c.getDifficulty(), ComputerType.STUPID);
		assertEquals(c.getCoordinatesList().size(), gameSize*gameSize);
		assertTrue(c.getNextHits().isEmpty());
		c.randomSetShips();
		p.randomSetShips();
		
		int[] coords = new int[2];

		assertFalse(c.isDefeated());
		for(int i = 0; i<gameSize*gameSize; ++i) {
			coords = c.randomHit();
			p.isHit(coords[0], coords[1]);
		}
		assertTrue(p.isDefeated());
	}
	
	@Test
	void testSmartComputer() {
		int[] coords = new int[2];
		int gameSize = 10;
		Computer d = new Computer(gameSize, ComputerType.SMART);
		Player q = new Player(gameSize);
		q.randomSetShips();
	
		//look for a crossCheck
		while(q.getState() == PlayerState.WATER) {
			coords = d.computerHits(q.getState());
			q.isHit(coords[0], coords[1]);
		}
		d.didComputerHit(q.getState());
		assert d.getLastSuccessfulHit() != null;
		assert d.getLastHit() != null;
		assert d.getLastHit().getRow() == d.getLastSuccessfulHit().getRow();
		assert d.getLastHit().getColumn() == d.getLastSuccessfulHit().getColumn();
		assertTrue(d.getNextHits().size() <= 4);
		d.printNextHits();
		
		//look for a lineCheck
		coords = d.computerHits(q.getState());
		q.isHit(coords[0], coords[1]);
		assert d.getLastSuccessfulHit() != null;
		assert d.getLastHit() != null;
		assert d.getLastHit().getRow() == d.getLastSuccessfulHit().getRow() || d.getLastHit().getColumn() == d.getLastSuccessfulHit().getColumn();
		assertTrue(d.getNextHits().size() <= 2);
		d.printNextHits();
	}
}
