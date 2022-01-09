package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Observable;
import java.util.Scanner;

import utils.BattleshipState;
import utils.ComputerType;
import utils.PlayerState;
import utils.ShipDirection;

/**
 * Model of the Battleship boardgame simulator. It holds a {@link Player}, a {@link Computer}, the size 
 * of the game grid, the timer (if the match is timed) and the file name of the ".dat" file to save/load.
 * 
 * @author 20027017 & 20031485
 *
 */
public class BattleshipModel extends Observable implements Serializable{
	private static final long serialVersionUID = 1L;
	private Player player = null;
	private Computer computer = null;
	private int gameSize;//5, 10, 15 where 5 means 5x5 and so on
	//stato della partita: serve a visualizzare il pannello corretto nella vista
	private BattleshipState state = BattleshipState.WELCOME;
	private PlayerState playerState;//needed by Computer, to check if Player was hit or not
	private boolean timed;//creates timer if true
	private int secs;
	private static final String savedFileName = "battleship_saved_game.dat";
	//every move will set justSaved to false, but the method saveGame will set it to true
	private boolean justSaved;
	
	/**
	 * Default constructor for the class {@link BattleshipModel}. 
	 */
	public BattleshipModel() {
		this.gameSize = 0;
		this.timed = false;
		this.player = null;
		this.computer = null;
		this.player = new Player(gameSize);
		this.computer = new Computer(gameSize, ComputerType.STUPID);
	}
	
	/**
	 * Constructor for the class {@link BattleshipModel}, which initializes a default 
	 * instance of {@link BattleshipModel} from the game size it receives as parameter.
	 * 
	 * @param gameSize The game size of the current Battleship match.
	 */
	public BattleshipModel(int gameSize) {
		this.gameSize = gameSize;
		this.timed = false;
		this.player = new Player(gameSize);
		this.computer = new Computer(gameSize, ComputerType.STUPID);
	}
	
	/**
	 * Constructor for the class {@link BattleshipModel}. It initializes all of its main 
	 * parameters such as {@link Player}, {@link Computer} and the game size, essential for a 
	 * simple Battleship match.
	 * 
	 * @param player A {@link Player} instance.
	 * @param computer A {@link Computer} instance.
	 * @param gameSize The size of the current Battleship match.
	 * @param timed A boolean value, true if the {@link BattleshipModel} has to be timed.
	 * @param secs The number of seconds the timer has to be initialized with.
	 */
	public BattleshipModel(Player player, Computer computer, int gameSize, boolean timed, int secs) {
		this.player = player;
		this.computer = computer;
		this.gameSize = gameSize;
		//this.isDefeated = false;
		this.justSaved = false;
		setTimed(timed, secs);
	}
	
	/**
	 * Returns true if the game has just been saved, false otherwise.
	 * @return true if the game has just been saved, false otherwise.
	 */
	public boolean isJustSaved() {
		return this.justSaved;
	}
	
	/**
	 * Gets the current state of the {@link BattleshipModel}.
	 * @return The current {@link utils.BattleshipState} of the {@link BattleshipModel}.
	 */
	public BattleshipState getState() {
		return this.state;
	}
	
	/**
	 * Sets the current state of the {@link BattleshipModel}.
	 * @param newState The new {@link utils.BattleshipState} of the {@link BattleshipModel}.
	 */
	public void setState(BattleshipState newState) {
		this.state = newState;
		setChanged();
		notifyObservers();
		this.justSaved = false;
	}
	
	/**
	 * Returns the current state of the {@link Player}.
	 * @return The current {@link utils.PlayerState} of the {@link Player}.
	 */
	public PlayerState getPlayerState() {
		return this.playerState;
	}
	
	/**
	 * Sets the current state of the {@link Player}.
	 * @param newPlayerState The new {@link utils.PlayerState} of the {@link Player}.
	 */
	public void setPlayerState(PlayerState newPlayerState) {
		this.playerState = newPlayerState;
	}
	
	/**
	 * Returns true if it detects a ".dat" file that matches the default filename.
	 * @return true if it detects a ".dat" file that matches the default filename, false otherwise.
	 */
	public static boolean savedGameExists(){
		File savedFile = new File(savedFileName);
		if(savedFile.exists()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Saves the current {@link BattleshipModel} object and all its content into 
	 * a ".dat" binary file.
	 */
	public void saveGame() {
		ObjectOutputStream outputStream = null;
		
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(savedFileName));
			outputStream.writeObject(this);
			outputStream.close();
			
			this.justSaved = true;
			setChanged();
			notifyObservers();
		} 
		catch (IOException e) {
			System.err.println("ERROR@BattleshipModel::saveGame(): Cannot save file!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize a {@link BattleshipModel} with all of its necessary parameters.
	 * 
	 * @param player A {@link Player} instance.
	 * @param computer A {@link Computer} instance.
	 * @param gameSize The size of the current Battleship match.
	 * @param timed A boolean value, true if the match is timed, false otherwise.
	 * @param secs The number of seconds the timer will be initialized with.
	 */
	public void newGame(Player player, Computer computer, int gameSize, boolean timed, int secs) {
		setPlayer(player);
		setComputer(computer);
		setGameSize(gameSize);
		setTimed(timed, secs);
		//game created, so you might want to save it immediately
		this.justSaved = false;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Static method that checks if a ".dat" binary file is present and tries to load its content into
	 * a {@link BattleshipModel} object. On failure, a {@link FileNotFoundException} is thrown.
	 * 
	 * @return The {@link BattleshipModel} object loaded from binary file.
	 * @throws FileNotFoundException
	 */
	public static BattleshipModel loadGame() throws FileNotFoundException {
		ObjectInputStream inputStream = null;
		BattleshipModel loadedGame = null;
		if(savedGameExists()) {
			try {
				inputStream = new ObjectInputStream(new FileInputStream(savedFileName));
				loadedGame = (BattleshipModel)inputStream.readObject();
				inputStream.close();
				System.out.println(savedFileName + " read!");
			} 
			catch (IOException | ClassNotFoundException e) {
				throw new FileNotFoundException("Invalid .dat file!");
			}
		}
		else {
			throw new FileNotFoundException("Saved file not found! Loading failed!");
		}
		return loadedGame;
	}
	
	/**
	 * Gets the {@link Player} of the {@link BattleshipModel} object.
	 * @return A {@link Player} instance.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the {@link Player} of the {@link BattleshipModel} object.
	 * @param player A {@link Player} instance.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Gets the {@link Computer} of the {@link BattleshipModel} object.
	 * @return A {@link Computer} instance.
	 */
	public Computer getComputer() {
		return computer;
	}

	/**
	 * Sets the {@link Computer} of the {@link BattleshipModel} object.
	 * @param computer A {@link Computer} instance.
	 */
	public void setComputer(Computer computer) {
		this.computer = computer;
	}

	/**
	 * Gets the game size of the current Battleship match.
	 * @return An integer representing the game size of the current Battleship match.
	 */
	public int getGameSize() {
		return gameSize;
	}

	/**
	 * Sets the game size of the current Battleship match.
	 * @param gameSize An integer representing the new game size of the current Battleship match.
	 */
	public void setGameSize(int gameSize) {
		this.gameSize = gameSize;
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Sets the timer of a {@link BattleshipModel} object. If timed is false, the secs parameter
	 * is ignored.
	 * 
	 * @param timed A boolean value: true if the game is timed, false otherwise.
	 * @param secs The number of seconds assigned to the timer (if enabled). 
	 */
	public void setTimed(boolean timed, int secs) {
		this.timed = timed;
		this.secs = secs;
	}
	
	/**
	 * Gets the seconds that remain to the countdown.
	 * @return An integer representing the seconds that remain to the countdown.
	 */
	public int getSecs() {
		return this.secs;
	}
	
	/**
	 * Sets the seconds that remain to the countdown.
	 * @param currentTime The seconds that remain to the countdown.
	 */
	public void setSecs(long currentTime) {
		this.secs = (int) currentTime;
	}
	
	/**
	 * Returns true if the match is timed, false otherwise.
	 * @return true if the match is timed, false otherwise.
	 */
	public boolean isTimed() {
		return this.timed;
	}

	/**
	 * Returns a {@link String} with the name of the saved file.
	 * @return A {@link String} with the name of the saved file.
	 */
	public String getSavedFileName() {
		return savedFileName;
	}

	/**
	 * Utility method used to compare two {@link BattleshipModel} objects.
	 * 
	 * @return true if the compared objects are equals, false otherwise.
	 */
	public boolean equals(Object o) {
		if(o instanceof BattleshipModel) {
			if(((BattleshipModel) o).getPlayer().equals(this.getPlayer())
					&& ((BattleshipModel) o).getComputer().equals(this.getComputer())
					&& ((BattleshipModel) o).getGameSize() == (this.getGameSize())
				) return true;
		}
		return false;
	}

	/**
	 * Utility method that prints the characteristics of a {@link BattleshipModel} object.
	 */
	public String toString() {
		return 	"Game Size: "+ getGameSize() +
				"\nPlayer1:\n" + getPlayer().toString() +
				"\nPlayer2:\n" + getComputer().toString() + "\n"
						+ "time left: "+ getSecs()+"\n";
	}
	
	/**
	 * Prototype of {@link BattleshipModel}'s terminal version. It simulates the interaction
	 * between {@link Player} and {@link Computer} until either of them is defeated.
	 */
	public void turns() {
		try (Scanner s = new Scanner(System.in)) {
			while(!player.isDefeated() || !computer.isDefeated()) {
				System.out.print("Insert row: ");
				int row = s.nextInt();
				s.nextLine();
				System.out.print("Insert col: ");
				int col = s.nextInt();
				s.nextLine();
				hitAndGetHit(row, col);
				System.out.println(player.toString());
				System.out.println(computer.toString());
			}
		}
	}
	
	/**
	 * This method simulates a couple of turns ({@link Player}'s and {@link Computer}'s in order)
	 * in which each of the players hits the other once. The parameters are the coordinates of the
	 * cell the {@link Player} wants to hit on the {@link Computer}'s grid.
	 * 
	 * @param row The row coordinate of the cell the {@link Player} wants to hit.
	 * @param col The column coordinate of the cell the {@link Player} wants to hit.
	 */
	public void hitAndGetHit(int row, int col) {
		int[] coordinates = new int[2];
		coordinates = player.hits(row, col);
		//una volta che ho scelto le coordinate da colpire (passate come argomenti)
		//colpisco il computer a quelle coordinate
		computer.isHit(coordinates[0], coordinates[1]);
		
		//tell GUI there has been a move
		justSaved = false;
		
		//computer hits back only if it is not defeated
		if(!computer.isDefeated()) {
			coordinates = computer.computerHits(getPlayerState());
			player.isHit(coordinates[0], coordinates[1]);
			setPlayerState(player.getState());
		}
	}
	
	/**
	 * Prototype of {@link BattleshipModel}'s terminal version. It simulates the {@link Player}'s
	 * positioning of its {@link Ship}s. 
	 */
	public void playerSetsShips() {
		try (Scanner s = new Scanner(System.in)) {
			while(!this.getPlayer().getShipList().isEmpty()) {
				System.out.println("Next ship's dimension: " + player.getShipList().get(0).getLength());
				System.out.print("Insert row: ");
				int row = s.nextInt();
				s.nextLine();
				System.out.print("Insert col: ");
				int col = s.nextInt();
				s.nextLine();
				System.out.print("Insert dir [0 = horizontal, 1 = vertical]: ");
				int dir = s.nextInt();
				s.nextLine();
				ShipDirection direction = null;
				if(dir == 0)
					direction = ShipDirection.HORIZONTAL;
				else
					direction = ShipDirection.VERTICAL;
				player.setShip(0, row, col, direction);
				System.out.println(player.toString());
			}
		}
	}
	
	/**
	 * Place one of the available {@link Player}'s {@link Ship}s on its ships grid.
	 * 
	 * @param shipIndex The index of the {@link Ship} in the {@link Player}'s ships list.
	 * @param row The row coordinate where the {@link Player} wants to place its {@link Ship}.
	 * @param col The column coordinate where the {@link Player} wants to place its {@link Ship}.
	 * @param direction The {@link utils.ShipDirection} of the {@link Ship} being set.
	 */
	public void playerSetsShip(int shipIndex, int row, int col, ShipDirection direction) {
		//places the ship at the shipIndex-th index in shipList
		player.setShip(shipIndex, row, col, direction);
	}
	

	/**
	 * Utility method to speed up the usage of the toString() method.
	 */
	public void print() {
		System.out.println(this.toString());
	}

	/*
	public static void main(String[] args) {
		int gameSize = 15;
		BattleshipModel bm = new BattleshipModel(gameSize);
		bm.newGame(new Player(gameSize), new Computer(gameSize, ComputerType.SMART), gameSize, false, 0);
		System.out.println(bm.getPlayer().toString());
		System.out.println(bm.getComputer().toString());
		bm.getPlayer().randomSetShips();
		bm.getComputer().randomSetShips();
		System.out.println(bm.getPlayer().toString());
		System.out.println(bm.getComputer().toString());
		//bm.turns();
		
		for(int i = 0; i < gameSize; ++i) {
			Random rand = new Random();
			int row = rand.nextInt(gameSize);
			int col = rand.nextInt(gameSize);
			bm.hitAndGetHit(row, col);
		}
		bm.print();
		bm.saveGame();
	}
	*/
}