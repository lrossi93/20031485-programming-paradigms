package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Player;
import utils.PlayerState;
import utils.ShipDirection;

class PlayerTest {

	@Test
	void test() {
		int gameSize = 10;
		Player p = new Player(gameSize);
		Player q = new Player(gameSize);
		
		//assertions after creation
		assert p != null;
		assert q != null;
		assertEquals(p, q);
		assertEquals(p.getName(), "Player");
		assertEquals(p.getState(), PlayerState.WATER);
		assertTrue(p.isDefeated());
		
		//assertions after ship positioning
		p.randomSetShips();
		assertFalse(p.isDefeated());
		assert p.getShipList().size() == 0;
		
		//assertion after ships removal
		p.clearShips();
		assertTrue(p.isDefeated());
		assert p.getShipList().size() != 0;
		
		//assertions after p is hit
		p.setShip(0, 0, 0, ShipDirection.HORIZONTAL);
		p.isHit(0, 0);
		assertEquals(p.getState(), PlayerState.HIT);
		assertNotEquals(p, q);
		assertFalse(p.isDefeated());
		
		//assertions for p's hits
		int[] coords = new int[2];
		coords = p.hits(2, 3);
		assertEquals(coords[0], 2);
		assertEquals(coords[1], 3);
	}
}
