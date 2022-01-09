package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import utils.PlayerState;
import utils.ShipDirection;
import utils.ShipLength;
import utils.ShipType;

/**
 * Class representing a player in the Battleship game. 
 * It holds its default name, a matrix for each {@link Ship} the {@link Player} sets
 * and a matrix for each hit the {@link Player} receives. For every incoming hit,
 * the {@link Player} changes its state according to the result of the hit.
 * When a {@link Player} has no {@link Ship}s on his ships grid, it is considered 
 * defeated.
 * 
 * @author 20027017 & 20031485
 *
 */
public class Player extends Observable implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	//griglia che contiene tutte le navi posizionate e non cambia una volta che è stata definita
	//inizializzata a true
	private boolean[][] initialShipsGrid;
	//griglia su cui vado a posizionare le navi --> da questa griglia ottengo initialShipsGrid
	//inizializzata a true
	private boolean[][] shipsGrid;
	//griglia su cui salvo i colpi ricvevuti
	//inizializzata a true
	private boolean[][] hitsGrid;
	private int gameSize;	
	//può essere WATER (mancato), HIT (colpito) o HITANDSUNK (colpito e affondato) - inizia a WATER
	private PlayerState state;
	//mi dice se il tempo della partita è scaduto
	private boolean timedOut;
	//lista delle navi disponibili
	private ArrayList<Ship> shipList;
	//lista delle navi posizionate (che prima erano in shipList)
	private ArrayList<Ship> placedShips;
	//lista delle navi completamente distrutte
	private ArrayList<Ship> deadShips;
	
	/**
	 * Constructor for the class {@link Player}.
	 * 
	 * @param playerName The name of the {@link Player}.
	 */
	public Player(String playerName) {
		this.name = playerName;
	}
	
	/**
	 * Constructor for the class {@link Player}.
	 * 
	 * @param gameSize The size of the game grid.
	 */
	public Player(int gameSize) {
		this.name = "Player";
		this.gameSize = gameSize;
		this.state = PlayerState.WATER;
		this.timedOut = false;
		this.initGrids(gameSize);
		this.initShips(gameSize);
	}
	

	/**
	 * Initializes all the ship lists a {@link Player} holds: a list for its available 
	 * (unplaced) {@link Ship}s, a list for its placed {@link Ship}s and a list
	 * for its sunk {@link Ship}s.
	 * 
	 * @param gameSize The size of the current Battleship match.
	 */
	private void initShips(int gameSize) {
		this.shipList = new ArrayList<Ship>();
		this.placedShips = new ArrayList<Ship>();
		this.deadShips = new ArrayList<Ship>();
		Ship ship = null;
		switch(gameSize) {
			case 5:
				ship = new Ship(ShipType.CARRIER, ShipLength.CARRIERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.BATTLESHIP, ShipLength.BATTLESHIPLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.SUBMARINE, ShipLength.SUBMARINELENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.DESTROYER, ShipLength.DESTROYERLENGTH, gameSize);
				shipList.add(ship);
				break;
				
			case 10:
				ship = new Ship(ShipType.PATROL, ShipLength.PATROLLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.BATTLESHIP, ShipLength.BATTLESHIPLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.DESTROYER, ShipLength.DESTROYERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.CARRIER, ShipLength.CARRIERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.SUBMARINE, ShipLength.SUBMARINELENGTH, gameSize);
				shipList.add(ship);
				break;
				
			case 15:
				ship = new Ship(ShipType.PATROL, ShipLength.PATROLLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.BATTLESHIP, ShipLength.BATTLESHIPLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.DESTROYER, ShipLength.DESTROYERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.DESTROYER, ShipLength.DESTROYERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.CARRIER, ShipLength.CARRIERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.SUBMARINE, ShipLength.SUBMARINELENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.SUBMARINE, ShipLength.SUBMARINELENGTH, gameSize);
				shipList.add(ship);
				break;
				
			case 20:
				ship = new Ship(ShipType.PATROL, ShipLength.PATROLLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.BATTLESHIP, ShipLength.BATTLESHIPLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.DESTROYER, ShipLength.DESTROYERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.CARRIER, ShipLength.CARRIERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.SUBMARINE, ShipLength.SUBMARINELENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.PATROL, ShipLength.PATROLLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.BATTLESHIP, ShipLength.BATTLESHIPLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.DESTROYER, ShipLength.DESTROYERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.CARRIER, ShipLength.CARRIERLENGTH, gameSize);
				shipList.add(ship);
				ship = new Ship(ShipType.SUBMARINE, ShipLength.SUBMARINELENGTH, gameSize);
				shipList.add(ship);
				break;
		}
	}
	
	/**
	 * Initializes a boolean grid with the position of set {@link Ship}s marked as false
	 * and free cells marked as true, copying it from an existing boolean grid.
	 * 
	 * @param grid A boolean grid.
	 */
	public void setInitialShipsGrid(boolean[][] grid) {
		for(int i = 0; i < gameSize; ++i) {
			for(int j = 0; j < gameSize; ++j) {
				initialShipsGrid[i][j] = grid[i][j];
			}
		}
	}
	
	/**
	 * Utility method that prints a representation of the initial position of the
	 * {@link Player}'s {@link Ship}s.
	 */
	public void printInitialShipsGrid() {
		for(int i = 0; i < gameSize; ++i) {
			for(int j = 0; j < gameSize; ++j) {
				if(!initialShipsGrid[i][j])
					System.out.print("[x]");
				else
					System.out.print("[ ]" );
			}
			System.out.println();
		}
	}
	
	/**
	 * Gets the name of the {@link Player}.
	 *
	 * @return A String containing the name of the {@link Player}.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the game size of the Battleship game.
	 * 
	 * @return An integer representing the game size of the current Battleship match.
	 */
	public int getGameSize() {
		return this.gameSize;
	}
	
	/**
	 * Sets the name of the {@link Player}.
	 * 
	 * @param newName String containing the new {@link Player}'s name.
	 */
	public void setName(String newName) {
		this.name = newName;
	}
	
	/**
	 * Gets the matrix of the {@link Player}'s {@link Ship}s set at the beginning
	 * of the game.
	 * 
	 * @return The boolean matrix of the {@link Player}'s set {@link Ship}s.
	 */
	public boolean[][] getInitialShipsGrid(){
		return this.initialShipsGrid;
	}
	
	/**
	 * Gets the matrix of the current {@link Player}'s set {@link Ship}s.
	 * 
	 * @return The boolean matrix of the {@link Player}'s set {@link Ship}s.
	 */
	public boolean[][] getShipsGrid(){
		return this.shipsGrid;
	}
	
	/**
	 * Gets the boolean matrix of the {@link Player}'s received hits.
	 * 
	 * @return The boolean matrix of the {@link Player}'s received hits.
	 */
	public boolean[][] getHitsGrid(){
		return this.hitsGrid;
	}
	
	/**
	 * Gets the list of the available {@link Player}'s {@link Ship}s.
	 * 
	 * @return The list of the available {@link Player}'s {@link Ship}s.
	 */
	public ArrayList<Ship> getShipList(){
		return this.shipList;
	}
	
	/**
	 * Sets a new {@link Player}'s state.
	 * 
	 * @param state The new {@link Player}'s state.
	 */
	public void setState(PlayerState state) {
		this.state = state;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Gets the current {@link Player}'s state.
	 * 
	 * @return The current {@link Player}'s state.
	 */
	public PlayerState getState() {
		return this.state;
	}
	
	/**
	 * Initializes the three boolean grids (initial, ships and hits) to the 
	 * correct size and sets each of their cells to true.
	 * 
	 * @param gridSize The size of the current Battleship match.
	 */
	private void initGrids(int gridSize) {
		initialShipsGrid = new boolean[gridSize][gridSize];
		shipsGrid = new boolean[gridSize][gridSize];
		hitsGrid = new boolean[gridSize][gridSize];
		for(int i=0; i<gridSize; ++i)
			for(int j=0; j<gridSize; ++j) {
				initialShipsGrid[i][j] = true;
				shipsGrid[i][j] = true;
				hitsGrid[i][j] = true;
			}
	}
	
	/**
	 * Method mimicking an incoming hit to the {@link Player}. It sets a cell of the
	 * hits grid to false at the coordinates passed as parameters. It also checks if the 
	 * coordinates match with the piece of a {@link Ship} on the ships grid and decides if 
	 * a {@link Ship} has been hit. It sets the {@link Player}'s state accordingly.
	 * 
	 * @param row The row coordinate being hit
	 * @param col The column coordinate being hit
	 */
	public void isHit(int row, int col) {
		hitsGrid[row][col] = false;//there IS a hit
		shipsGrid[row][col] = true;//there is NOT a ship
		PlayerState newState = PlayerState.WATER;
		for(int i = 0; i < placedShips.size(); ++i) {
			Ship ship = this.placedShips.get(i);
			if(ship.isHit(row, col)) {
				newState = PlayerState.HIT;
				if(ship.isSunk()) {
					deadShips.add(placedShips.get(i));
					placedShips.remove(i);
					newState = PlayerState.HITANDSUNK;
				}
			}
		}
		//includes setChanged and notifyObservers
		setState(newState);
	}
	
	/**
	 * Returns the coordinates the {@link Player} wants to hit.
	 * 
	 * @param row The row coordinate.
	 * @param col the column coordinate.
	 * @return An integer bidimensional array containing the two coordinates.
	 */
	public int[] hits(int row, int col){
		int[] coordinates = new int[2];
		coordinates[0] = row;
		coordinates[1] = col;
		return coordinates;
	}
	

	/**
	 * Sets a single {@link Player}'s {@link Ship} onto the {@link Player}'s game grid.
	 * 
	 * @param shipIndex The index of the {@link Ship}.
	 * @param row The row coordinate the {@link Ship} is going to be set.
	 * @param col The column coordinate the {@link Ship} is going to be set.
	 * @param direction The vertical/horizontal direction of the {@link Ship}.
	 */
	public void setShip(int shipIndex, int row, int col, ShipDirection direction) {
		//se ho posizionato la nave, la rimuovo dalla lista
		if(this.shipList.get(shipIndex).setShip(row, col, direction, this.shipsGrid)) {
			//metto la nave anche sulla griglia iniziale
			this.shipList.get(shipIndex).setShip(row, col, direction, this.initialShipsGrid);
			//tolgo una nave dalla lista delle navi disponibili e la aggiungo alla lista delle navi piazzate
			this.placedShips.add(this.shipList.get(shipIndex));
			this.shipList.remove(shipIndex);
		}
		//update the view
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Randomly sets all {@link Player}'s {@link Ship}s.
	 */
	public void randomSetShips() {
		Random rand = new Random();
		while(!this.shipList.isEmpty()) {
			int row = rand.nextInt(this.gameSize);
			int col = rand.nextInt(this.gameSize);
			int dir = rand.nextInt(2);
			if(dir == 0)
				setShip(0, row, col, ShipDirection.HORIZONTAL);
			else
				setShip(0, row, col, ShipDirection.VERTICAL);
		}
	}
	
	/**
	 * Removes all {@link Player}'s {@link Ship}s from its ships grid and initial ships grid.
	 */
	public void clearShips() {
		while(placedShips.size() != 0) {
			//aggiungo una nave che c'è sulla griglia alla lista delle navi disponibili
			shipList.add(placedShips.get(0));
			//resetto la sua absolutePosition (campo di Ship)
			placedShips.get(0).removeShip();
			//rimuovo la nave dalla lista delle navi posizionate
			placedShips.remove(0);
		}
		//re-imposto tutte le griglie a true
		resetShipsGrid();
		
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Returns the number of the non-completely-destroyed {@link Ship}s of the {@link Player}.
	 * 
	 * @return An integer stating the number of "alive" {@link Ship}s.
	 */
	public int shipsLeft() {
		return placedShips.size();
	}
	
	/**
	 * Sets each cell of the ships grid to true.
	 */
	private void resetShipsGrid() {
		for(int i = 0; i < shipsGrid.length; ++i) {
			for(int j = 0; j < shipsGrid.length; ++j) {
				initialShipsGrid[i][j] = true;
				shipsGrid[i][j] = true;
			}
		}
	}
	
	/**
	 * Set if the {@link Player}'s timer has run out.	
	 * @param timedOut A boolean parameter: false if the player is not defeated, true otherwise.
	 */
	public void setTimedOut(boolean timedOut) {
		this.timedOut= timedOut;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Say if the {@link Player}'s countdown is over or not.
	 * 
	 */
	public boolean isTimedOut() {
		return this.timedOut;
	}
	
	/**
	 * Check if the {@link Player} is defeated, i.e. if all of its {@link Ship}s have been sunk.
	 * @return True if {@link Player} is defeated, false otherwise.
	 */
	public boolean isDefeated() {
		boolean result = true;
		for(int i=0; i<shipsGrid.length; ++i) {
			for(int j=0; j<shipsGrid.length; ++j)
				result = result && shipsGrid[i][j];
		}
		return result;
	}
	
	/**
	 * Utility method for comparing two {@link Player} objects, 
	 * mostly used for testing and debugging purposes.
	 * 
	 * @param o {@link Object} to probe.
	 * @return true if {@link Object} o and the calling {@link Player} are the same 
	 * {@link Object} with the same attributes, false otherwise.
	 */
	public boolean equals(Object o) {
		if(o instanceof Player) {
			if(((Player) o).getName().equals(this.getName())
				&& ((Player) o).getState() == this.getState()
				/*&& ((Player)o).getShipsGrid().equals(this.getShipsGrid())
				&& ((Player)o).getHitsGrid().equals(this.getHitsGrid())*/)
			return true;
		}
		return false;
	}
	
	/**
	 * Utility method for printing a {@link Player} object, mostly used for debugging purposes.
	 */
	public String toString() {
		String toString = "Name: "+this.getName()+"\nShips:\n";
		
		toString += "initialShipsGrid:\n";
		for(int i=0; i < shipsGrid.length; ++i) {
			for(int j=0; j < shipsGrid.length; ++j) {
				if(!initialShipsGrid[i][j])
					toString += "[X]";
				else
					toString += "[ ]";
			}
			toString += "\n";
		}
		
		toString += "currentShipsGrid:\n";
		for(int i=0; i < shipsGrid.length; ++i) {
			for(int j=0; j < shipsGrid.length; ++j) {
				if(!shipsGrid[i][j])
					toString += "[X]";
				else
					toString += "[ ]";
			}
			toString += "\n";
		}
		
		toString += "hitsGrid:\n";
		for(int i=0; i < hitsGrid.length; ++i) {
			for(int j=0; j < hitsGrid.length; ++j) {
				if(!hitsGrid[i][j])
					toString += "[X]";
				else
					toString += "[ ]";
			}
			toString += "\n";
		}
		toString += "Is defeated: "+isDefeated()+"\n";
		
		return toString;
	}
}