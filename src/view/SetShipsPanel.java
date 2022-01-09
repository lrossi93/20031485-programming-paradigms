package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import controller.BattleshipController;
import controller.SetShipsController;
import model.BattleshipModel;
import model.Ship;
import utils.BattleshipState;
import utils.ShipType;
import utils.Utility;

/**
 * <p>Class that represents the phase of setting ships onto the Battleship grid.
 * A {@link SetShipsPanel} is a {@link javax.swing.JPanel} containing two {@link javax.swing.JComboBox}es
 * and some {@link javax.swing.JButton}s, plus another {@link javax.swing.JPanel} containing a 
 * {@link javax.swing.JButton} grid.</p>
 * 
 * <p>When a {@link javax.swing.JButton} from the grid is pressed, the {@link model.Ship} selected
 * in the first {@link javax.swing.JComboBox} is positioned at the coordinates corresponding to the
 * {@link javax.swing.JButton} pressed from the grid. The direction of the {@link model.Ship} is the
 * {@link utils.ShipDirection} selected in the second {@link javax.swing.JComboBox}.</p>
 * 
 * <p>if the user is not happy with how the {@link model.Ship}s are set, he can either press "CLEAR"
 * to remove all positioned {@link model.Ship}s from the grid an manually re-set them or press "RANDOM",
 * so that the {@link model.Ship}s can be positioned randomly all at once.</p>
 * 
 * @author 20027017 & 20031485
 *
 */
public class SetShipsPanel extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;

	private BattleshipModel model;
	private BattleshipController controller;
	private SetShipsController setShipsController;
	private static final String TITLE = "SET YOUR SHIPS!";
	private static String[] direction = { "VERTICAL", "HORIZONTAL" };
	
	private int WIDTH = 700;
	private int HEIGHT = 700;
	
	private JPanel dropDownAndButtonsPanel; //panel hosting buttons and dropdown menus
	private JPanel shipsPanel; //panel on which the buttonGrid will be shown
	private JButton[][] buttonGrid; //clickable grid of buttons for setting ships
	
	private static final TitledBorder chooseShipTitle = BorderFactory.createTitledBorder("Ship (length)");
	private JComboBox<String> chooseShip;

	private static final TitledBorder chooseDirectionTitle = BorderFactory.createTitledBorder("Direction");
	private JComboBox<String> chooseDirection;
	
	private JButton play;
	private JButton clearShips;
	private JButton randomSetAll;
	private JButton back;
	
	
	private boolean here = false;
	

	/**
	 * Constructor for the Class {@link SetShipsPanel}. It creates a JPanel to
	 * be shown in the main game frame.
	 * @param model A {@link model.BattleshipModel} object.
	 */
	public SetShipsPanel(BattleshipModel model, BattleshipController controller) {
		this.model = model;
		this.controller = controller;
		this.setShipsController = this.controller.giveSetShipsController(model, this);
		this.setLayout(new FlowLayout());
		this.setSize(WIDTH, HEIGHT);
		this.model.addObserver(this);
	}
	
	/**
	 * Whenever the {@link SetShipsPanel} is accessed, all of its components are set.
	 * They consist in two {@link javax.swing.JComboBox}es
	 * and some {@link javax.swing.JButton}s, plus another {@link javax.swing.JPanel} containing a 
	 * {@link javax.swing.JButton} grid.
	 */
	public void setAllComponents() {
		if(model.getGameSize() == 10)
			setSize(WIDTH, HEIGHT);
		if(model.getGameSize() == 15)
			setSize(WIDTH, HEIGHT+50);
		if(model.getGameSize() == 20)
			setSize(WIDTH, HEIGHT+100);
		
		//observe player
		this.model.getPlayer().addObserver(this);
		setDropDownAndButtonsPanel();
		setButtonGrid();
	}
	
	/**
	 * Updates all the components of a {@link SetShipsPanel}.
	 */
	public void updateAllComponents() {
		updateDropDownAndButtonsPanel();
		updateButtonGrid();
	}
	/**
	 * Creates and adds a panel with two {@link javax.swing.JComboBox}es
	 * and some {@link javax.swing.JButton}s.
	 */
	public void setDropDownAndButtonsPanel() {
		//if a previous version of dropDown...Panel exists, remove it
		if(dropDownAndButtonsPanel != null)
			this.remove(dropDownAndButtonsPanel);
		
		dropDownAndButtonsPanel = new JPanel();
		dropDownAndButtonsPanel.setLayout(new FlowLayout());
		
		//fill the availableShips String array for the comboBox
		String[] availableShips = getPlayersShipsStrings(model);
		chooseShip = new JComboBox<String>(availableShips);
		chooseShip.setSelectedIndex(0);
		chooseShip.setBorder(chooseShipTitle);
		dropDownAndButtonsPanel.add(chooseShip);
		
		//comboBox for directions
		chooseDirection = new JComboBox<String>(direction);
		chooseDirection.setSelectedIndex(0);
		chooseDirection.setBorder(chooseDirectionTitle);
		dropDownAndButtonsPanel.add(chooseDirection);
		
		//button that sets all ships randomly
		randomSetAll = new JButton("RANDOM");
		randomSetAll.setEnabled(true);
		randomSetAll.addActionListener(setShipsController);
		dropDownAndButtonsPanel.add(randomSetAll);
		
		//button that deletes all ships from the grid
		clearShips = new JButton("CLEAR");
		clearShips.setEnabled(true);
		clearShips.addActionListener(setShipsController);
		dropDownAndButtonsPanel.add(clearShips);
		
		//enabled only if all ships have been set
		play = new JButton("PLAY");
		
		if(model.getPlayer().getShipList().size() == 0)
			play.setEnabled(true);
		else
			play.setEnabled(false);
		play.addActionListener(setShipsController);
		dropDownAndButtonsPanel.add(play);
		
		//button that deletes all ships from the grid
		back = new JButton("BACK");
		back.setEnabled(true);
		back.addActionListener(setShipsController);
		dropDownAndButtonsPanel.add(back);
		
		dropDownAndButtonsPanel.setVisible(true);
		add(dropDownAndButtonsPanel);
	}
	
	/**
	 * Updates the content of the {@link javax.swing.JPanel} containing the
	 * {@link javax.swing.JComboBox}es. The content of the {@link javax.swing.JComboBox}es
	 * is updated too, and the "PLAY" {@link javax.swing.JButton} is enabled only if the
	 * user has set all the available {@link model.Ship}s.
	 */
	public void updateDropDownAndButtonsPanel() {
		//ships comboBox
		String[] availableShips = getPlayersShipsStrings(model);
		if(chooseShip != null)
		chooseShip.removeAllItems();
		
		for(String s : availableShips) {
			if(chooseShip != null)
			chooseShip.addItem(s);
		}
		if(chooseShip != null) {
			chooseShip.setSelectedIndex(0);
			chooseShip.setBorder(chooseShipTitle);
		}
		//play button
		if(play != null) {
			if(model.getPlayer().getShipList().size() == 0)
				play.setEnabled(true);
			else
				play.setEnabled(false);
		}
	}
	
	/**
	 * Creates and adds a {@link javax.swing.JButton} grid as big as the game size to the main panel.
	 */
	public void setButtonGrid(){
		int gameSize = model.getGameSize();
		int dim = 25;
		
		//Player's shipsGrid
		boolean[][] shipsGrid = new boolean[gameSize][gameSize];
		shipsGrid = model.getPlayer().getShipsGrid();
		
		//if a previous version of shipsPanel exists, remove it
		if(shipsPanel != null)
			remove(shipsPanel);
		
		shipsPanel = new JPanel();
		shipsPanel.setLayout(new GridLayout(gameSize + 1, gameSize + 1));
		
		//the buttonGrid must be one cell smaller than shipsPanel
		//i.e. as big as Player's shipsGrid
		buttonGrid = new JButton[gameSize][gameSize];
		
		for(int i = 0; i < gameSize + 1; ++i) {
			for(int j = 0; j < gameSize + 1; ++j) {
				
				if(i == 0 && j == 0) {
					JTextField t = new JTextField();
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					shipsPanel.add(t);
				}
				
				else if(i == 0 && j != 0) {
					JTextField t = new JTextField(Utility.COLUMNS[j]);
					t.setHorizontalAlignment(JTextField.CENTER);
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					shipsPanel.add(t);
				}
				
				else if(i != 0 && j == 0) {
					JTextField t = new JTextField(Utility.ROWS[i]);
					t.setHorizontalAlignment(JTextField.CENTER);
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					shipsPanel.add(t);
				}
				
				else {
					//if there is no ship underneath
					if(shipsGrid[i-1][j-1]) {
						buttonGrid[i-1][j-1] = new JButton();
						buttonGrid[i-1][j-1].setPreferredSize(new Dimension(dim, dim));
						buttonGrid[i-1][j-1].setBackground(Color.WHITE);
						buttonGrid[i-1][j-1].addActionListener(setShipsController);
						shipsPanel.add(buttonGrid[i-1][j-1]);
					}
					//if there is a ship underneath
					else {
						buttonGrid[i-1][j-1] = new JButton();
						buttonGrid[i-1][j-1].setPreferredSize(new Dimension(dim, dim));
						buttonGrid[i-1][j-1].setBackground(Color.BLACK);
						buttonGrid[i-1][j-1].setOpaque(true);
						buttonGrid[i-1][j-1].setEnabled(false);
						shipsPanel.add(buttonGrid[i-1][j-1]);
					}
				}
			}
		}
		shipsPanel.setVisible(true);
		add(shipsPanel);
	}
	
	/**
	 * Whenever a {@link javax.swing.JButton} from the button grid is pressed, that
	 * grid is updated. If a {@link model.Ship} was successfully set, the cells
	 * where it lies are disabled and black-colored. On positioning errors, the clicked
	 * {@link javax.swing.JButton} remains enabled and white.
	 */
	public void updateButtonGrid() {
		int gameSize = model.getGameSize();
		
		//Player's shipsGrid
		boolean[][] shipsGrid = new boolean[gameSize][gameSize];
		shipsGrid = model.getPlayer().getShipsGrid();
	
		for(int i = 0; i < gameSize; ++i) {
			for(int j = 0; j < gameSize; ++j) {
				if(buttonGrid != null) {
					//if there is a ship underneath
					if(!shipsGrid[i][j]) {
						buttonGrid[i][j].setBackground(Color.BLACK);
						buttonGrid[i][j].setOpaque(true);
						buttonGrid[i][j].setEnabled(false);
					}
					else {
						buttonGrid[i][j].setBackground(Color.WHITE);
						buttonGrid[i][j].setOpaque(false);
						buttonGrid[i][j].setEnabled(true);
						if(model.getPlayer().getShipList().size() == 0)
							buttonGrid[i][j].setEnabled(false);
					}
				}
			}
		}
	}
	
	/**
	 * Returns the {@link javax.swing.JButton} grid.
	 * @return the {@link javax.swing.JButton} grid.
	 */
	public JButton[][] getButtonGrid() {
		return buttonGrid;
	}
	
	/**
	 * Returns a {@link javax.swing.JButton} from the grid, according to its coordinates.
	 * @param i The row coordinate of the {@link javax.swing.JButton} in the grid.
	 * @param j The column coordinate of the {@link javax.swing.JButton} in the grid.
	 * @return A {@link javax.swing.JButton} from the grid.
	 */
	public JButton getButtonFromButtonGrid(int i, int j) {
		if(buttonGrid != null)
			return buttonGrid[i][j];
		else 
			return null;
	}
	
	/**
	 * Removes all the graphic content from the {@link SetShipsPanel}.
	 */
	public void removeAllComponents() {
		dropDownAndButtonsPanel = null; //panel hosting buttons and dropdown menus
		shipsPanel = null; //panel on which the buttonGrid will be shown
		buttonGrid = null; //clickable grid of buttons for setting ships
		chooseShip = null;
		chooseDirection = null;
		play = null;
		clearShips = null;
		randomSetAll = null;
		back = null;
	}
	
	/**
	 * Returns the {@link String} array representation of the list of 
	 * {@link model.Player}'s {@link model.Ship}s.
	 * 
	 * @param model An instance of a {@link model.BattleshipModel} object.
	 * @return A {@link String} array.
	 */
	public String[] getPlayersShipsStrings(BattleshipModel model) {
		int playerShipsNo = model.getPlayer().getShipList().size();
		String[] stringArray = new String[playerShipsNo];
		
		//if there are no more ships:
		if(playerShipsNo == 0) {
			stringArray = new String[1];
			stringArray[0] = "NO SHIPS LEFT!";
		}
		
		//if there is at least one ship:
		int i = 0;
		while(i < playerShipsNo) {
			Ship currentShip = model.getPlayer().getShipList().get(i);
			ShipType shipType = currentShip.getShipType();
			switch(shipType) {
				case PATROL:
					stringArray[i] = "PATROL (" + currentShip.getLength() + ")";
					break;
					
				case SUBMARINE:
					stringArray[i] = "SUBMARINE (" + currentShip.getLength() + ")";
					break;
				
				case DESTROYER:
					stringArray[i] = "DESTROYER (" + currentShip.getLength() + ")";
					break;
				
				case BATTLESHIP:
					stringArray[i] = "BATTLESHIP (" + currentShip.getLength() + ")";
					break;
				
				case CARRIER:
					stringArray[i] = "CARRIER  (" + currentShip.getLength() + ")";
					break;
				
				default:
					System.err.println("ERROR@SetShipsPanel::getPlayersShipsStrings");
					break;
			}
			++i;
		}
		return stringArray;
	}

	/**
	 * Gets the title of the current {@link SetShipsPanel}.
	 * @return A {@link String} containing the title of the current {@link SetShipsPanel}.
	 */
	public String getTitle() {
		return TITLE;
	}
	
	/**
	 * Gets the width of the current {@link SetShipsPanel}.
	 * @return An integer containing the width of the current {@link SetShipsPanel}.
	 */
	public int getWidth() {
		return WIDTH;
	}
	
	/**
	 * Gets the height of the current {@link SetShipsPanel}.
	 * @return An integer containing the height of the current {@link SetShipsPanel}.
	 */
	public int getHeight() {
		return HEIGHT;
	}
	
	/**
	 * Gets the {@link javax.swing.JComboBox} for the {@link model.Ship} choice.
	 * @return The {@link javax.swing.JComboBox} for the {@link model.Ship} choice.
	 */
	public JComboBox<String> getChooseShip() {
		return chooseShip;
	}
	
	/**
	 * Gets the {@link javax.swing.JComboBox} for the {@link utils.ShipDirection} choice.
	 * @return The {@link javax.swing.JComboBox} for the {@link utils.ShipDirection} choice.
	 */
	public JComboBox<String> getChooseDirection() {
		return chooseDirection;
	}
	
	@Override
	/**
	 * On access or whenever a {@link model.Ship} is successfully set, this method updates
	 * the whole {@link SetShipsPanel} according to the {@link model.Player}'s available
	 * {@link model.Ship}s and its grids.
	 */
	public void update(Observable o, Object arg) {
		if(model.getState() == BattleshipState.SETSHIPS) {
			if(!here) {
				//set the two panels with their content
				setAllComponents();
				this.setVisible(true);
				here = true;
				updateAllComponents();
			}
			else
				updateAllComponents();
		}
		else {
			this.setVisible(false);
			this.removeAll();
			removeAllComponents();
			here = false;
		}
	}
}
