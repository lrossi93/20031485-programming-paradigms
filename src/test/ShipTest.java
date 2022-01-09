package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Ship;
import utils.ShipDirection;
import utils.ShipLength;
import utils.ShipType;

class ShipTest {

	@Test
	void test() {
		int gameSize = 10;
		boolean[][] shipsGrid = new boolean[gameSize][gameSize];
		for(int i = 0; i<gameSize; ++i)
			for(int j = 0; j < gameSize; ++j)
				shipsGrid[i][j] = true;
		Ship s = new Ship(ShipType.CARRIER, ShipLength.CARRIERLENGTH, gameSize);
		
		//assertions after creation
		assert s != null;
		assert s.getLength() == ShipLength.CARRIERLENGTH;
		assert s.getLength() == 5;
		
		//assertions after positioning
		assertTrue(s.enoughSpace(0, 0, ShipDirection.HORIZONTAL, shipsGrid));
		assertTrue(s.setShip(0, 0, ShipDirection.HORIZONTAL, shipsGrid));
		assertFalse(s.isSunk());
		
		//assertions after hit
		assertTrue(s.isHit(0, 0));
		assertFalse(s.isHit(1, 0));
		assertFalse(s.isSunk());
		
		//assertions after removal
		s.removeShip();
		assertTrue(s.isSunk());
	}

}
