package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import controller.BattleController;
import controller.BattleshipController;
import model.BattleshipModel;
import utils.BattleshipState;
import utils.Utility;

/**
 * <p>Class that represent the board of the Battleship game, and shows all the moves
 * taken both by the {@link model.Player} and the {@link model.Computer}. 
 * A {@link BattlePanel} is a {@link javax.swing.JPanel} that holds two 
 * {@link javax.swing.JButton} grids, one of which is clickable by the user
 * in order to simulate the action of the {@link model.Player}. Both grids
 * are updated at every click, so that the actions taken by {@link model.Computer}
 * can be seen as a color change in the grid cells it chooses to hit.
 * </p>
 * <p>If the {@link model.BattleshipModel} is "timed", this class also initializes 
 * a {@link javax.swing.Timer} held in {@link CountdownPanel} and adds the panel to
 * this view.
 * </p>
 * <p>In this panel, the current instance of {@link model.BattleshipModel} can be 
 * saved in a binary file, if the user somehow wants to suspend the match and continue
 * later on. This is the view that is accessed either after setting all {@link model.Ship}s
 * on the {@link SetShipsPanel} or if an existing valid binary file is found and "Load game"
 * is clicked on {@link StartLoadPanel}.
 * </p>
 * @author 20027017 & 20031485
 *
 */
public class BattlePanel extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;

	private BattleshipModel model;
	private BattleshipController controller;
	private BattleController battleController;
	
	private static final String TITLE = "BATTLE!";
	private static final int WIDTH = 1000;
	private static final int HEIGHT = 650;
	
	private final int smallDim = 30;
	private final int mediumDim = 25;
	private final int largeDim = 20;
	
	private JPanel bothGridsPanel;
	
	private static final TitledBorder shipsGridTitle = BorderFactory.createTitledBorder("YOUR SHIPS");
	private JPanel leftPanel;
	private JPanel shipsGridPanel;
	private JButton[][] shipsButtonGrid;
	
	private static final TitledBorder hitsGridTitle = BorderFactory.createTitledBorder("ENEMY'S SHIPS");
	private JPanel rightPanel;
	private JPanel hitsGridPanel;
	private JButton[][] hitsButtonGrid;
	
	private JPanel allButtonsPanel;
	
	private JButton saveButton;
	
	private CountdownPanel timerPanel;
	
	private boolean here = false;
	
	
	private static final String PLAYER_STATUS = "PLAYER STATUS: ";
	private static final String COMPUTER_STATUS = "COMPUTER STATUS: ";
	private static final String YOU_WIN = "You win!";
	private static final String YOU_LOSE = "You lose!";
	
	private JLabel playerStatusLabel = new JLabel(PLAYER_STATUS);
	private JLabel computerStatusLabel = new JLabel(COMPUTER_STATUS);
	private JLabel youWinLabel = new JLabel(YOU_WIN);
	private JLabel youLoseLabel = new JLabel(YOU_LOSE);
	
	/**
	 * Constructor for the class {@link BattlePanel}. It links the {@link BattleshipModel}
	 * and the {@link BattleshipController} to private fields so that it can access their methods.
	 * Then it initializes its dimensions and its layout and, finally, links itself to the model
	 * as {@link java.util.Observer} to listen to changes in the {@link BattleshipModel}.
	 * 
	 * @param model A {@link BattleshipModel} instance.
	 * @param controller A {@link BattleshipController} Instance.
	 */
	public BattlePanel(BattleshipModel model, BattleshipController controller) {
		this.model = model;
		this.controller = controller;
		setSize(WIDTH, HEIGHT);
		setLayout(new BorderLayout());
		this.model.addObserver(this);
		this.battleController = this.controller.giveBattleController(model, this);
	}
	
	/**
	 * Returns a {@link String} containing the title of the panel.
	 * @return A {@link String} containing the title of the panel.
	 */
	public static String getTitle() {
		return TITLE;
	}
	
	/**
	 * Utility method used to fill the {@link BattlePanel} with sub-panels and
	 * {@link javax.swing.JButton} grids. Launched when the panel is accessed.
	 */
	public void setAllComponents() {
		System.out.println("setAllComponents");
			
		//link observables
		this.model.getPlayer().addObserver(this);
		this.model.getComputer().addObserver(this);
		
		//if the game is timed
		if(model.isTimed()) {
			setTimer(model.getSecs());	
			//setTimer(5);
		}
		
		//create panel with grids
		bothGridsPanel = new JPanel();
		bothGridsPanel.setLayout(new FlowLayout());
		
		//fill panel with grids
		setShipsGrid();
		setHitsGrid();
		
		add(bothGridsPanel, BorderLayout.CENTER);
		
		//create panel with buttons
		setButtonsPanel();
	}
	
	/**
	 * Sets a {@link javax.swing.Timer} held in {@link CountdownPanel} with 
	 * an amount of seconds.
	 * 
	 * @param secs The amount of seconds.
	 */
	public void setTimer(long secs) {
		timerPanel = new CountdownPanel(secs, model);
		add(timerPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Updates all the components set when {@link BattlePanel} is first accessed.
	 * Launched whenever a button is clicked or an action is taken.
	 */
	public void updateAllComponents() {
		updateShipsGrid();
		
		updateHitsGrid();
		
		if(!model.isJustSaved())
			saveButton.setEnabled(false);
		else
			saveButton.setEnabled(true);
	}

	/**
	 * <p>
	 * Sets all the components to create the ships grid, where the user can see its ships.
	 * It creates a {@link javax.swing.JPanel} with GridLayout and fills its cells with 
	 * {@link javax.swing.JButton}s. All those {@link javax.swing.JButton}s are disabled
	 * and if the cell matches a {@link model.Ship}, it is colored in black; if it does not, 
	 * it is colored in white. 
	 * </p>
	 * <p>
	 * If the user is restoring a saved match, cells can also be blue
	 * if the {@link model.Computer} has hit that cell but there was no {@link model.Ship}
	 * underneath, or red if the corresponding {@link model.Computer} hit was successful.
	 * </p>
	 */
	public void setShipsGrid() {
		int gameSize = model.getGameSize();
		int dim = 0;
		if(gameSize == 10)
			dim = smallDim;
		if(gameSize == 15)
			dim = mediumDim;
		if(gameSize == 20)
			dim = largeDim;

		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		
		//Player's shipsGrid
		boolean[][] shipsGrid = new boolean[gameSize][gameSize];
		shipsGrid = model.getPlayer().getShipsGrid();
		
		boolean[][] hitsGrid = new boolean[gameSize][gameSize];
		hitsGrid = model.getPlayer().getHitsGrid();
		
		shipsGridPanel = new JPanel();
		shipsGridPanel.setLayout(new GridLayout(gameSize + 1, gameSize + 1));
		
		//the buttonGrid must be one cell smaller than shipsPanel
		//i.e. as big as Player's shipsGrid
		shipsButtonGrid = new JButton[gameSize][gameSize];
		
		for(int i = 0; i < gameSize + 1; ++i) {
			for(int j = 0; j < gameSize + 1; ++j) {
				
				if(i == 0 && j == 0) {
					JTextField t = new JTextField();
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					shipsGridPanel.add(t);
				}
				
				else if(i == 0 && j != 0) {
					JTextField t = new JTextField(Utility.COLUMNS[j]);
					t.setHorizontalAlignment(JTextField.CENTER);
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					shipsGridPanel.add(t);
				}
				
				else if(i != 0 && j == 0) {
					JTextField t = new JTextField(Utility.ROWS[i]);
					t.setHorizontalAlignment(JTextField.CENTER);
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					shipsGridPanel.add(t);
				}
				
				else {
					shipsButtonGrid[i-1][j-1] = new JButton();
					shipsButtonGrid[i-1][j-1].setPreferredSize(new Dimension(dim, dim));
					
					//if there are no ships and no hits (true/true)
					if(shipsGrid[i-1][j-1] && hitsGrid[i-1][j-1]) {
						shipsButtonGrid[i-1][j-1].setBackground(Color.WHITE);
						shipsButtonGrid[i-1][j-1].setEnabled(false);
						shipsButtonGrid[i-1][j-1].addActionListener(battleController);
					}
					//if there are ships but no hits (false/true)
					else if(!shipsGrid[i-1][j-1] && hitsGrid[i-1][j-1]){
						shipsButtonGrid[i-1][j-1].setBackground(Color.BLACK);
						shipsButtonGrid[i-1][j-1].setOpaque(true);
						shipsButtonGrid[i-1][j-1].setEnabled(false);
					}
					//if there are no ships but hits (true/false)
					else if(shipsGrid[i-1][j-1] && !hitsGrid[i-1][j-1]) {
						shipsButtonGrid[i-1][j-1].setBackground(Color.BLUE);
						shipsButtonGrid[i-1][j-1].setOpaque(true);
						shipsButtonGrid[i-1][j-1].setEnabled(false);
					}
					//if there are both ships and hits (false/false)
					else if(!shipsGrid[i-1][j-1] && !hitsGrid[i-1][j-1]) {
						shipsButtonGrid[i-1][j-1].setBackground(Color.RED);
						shipsButtonGrid[i-1][j-1].setOpaque(true);
						shipsButtonGrid[i-1][j-1].setEnabled(false);
					}
					shipsGridPanel.add(shipsButtonGrid[i-1][j-1]);
				}
			}
		}
		
		shipsGridPanel.setBorder(shipsGridTitle);
		shipsGridPanel.setVisible(true);
		leftPanel.add(shipsGridPanel, BorderLayout.NORTH);
		leftPanel.add(playerStatusLabel, BorderLayout.CENTER);
		bothGridsPanel.add(leftPanel);
	}
	
	/**
	 * Whenever the {@link model.Computer} hits the {@link model.Player},
	 * the {@link javax.swing.JButton} corresponding to the chosen coordinates
	 * becomes either red if those coordinates match the position of a {@link model.Player}'s
	 * {@link model.Ship}, or blue otherwise. An unchosen cell remains white.
	 */
	public void updateShipsGrid() {
		int gameSize = model.getGameSize();
		
		boolean[][] shipsGrid = new boolean[gameSize][gameSize];
		shipsGrid = model.getPlayer().getInitialShipsGrid();
		
		boolean[][] hitsGrid = new boolean[gameSize][gameSize];
		hitsGrid = model.getPlayer().getHitsGrid();
		
		for(int i = 0; i < gameSize; ++i) {
			for(int j = 0; j < gameSize; ++j) {
				//if there are no ships and no hits (true/true)
				if(shipsGrid[i][j] && hitsGrid[i][j]) {
					
				}
				
				//if there are ships but no hits (false/true)
				else if(!shipsGrid[i][j] && hitsGrid[i][j]){
					
				}
				
				//if there are no ships but hits (true/false)
				else if(shipsGrid[i][j] && !hitsGrid[i][j]) {
					shipsButtonGrid[i][j].setBackground(Color.BLUE);
					shipsButtonGrid[i][j].setOpaque(true);
				}
				
				//if there are both ships and hits (false/false)
				else if(!shipsGrid[i][j] && !hitsGrid[i][j]) {
					shipsButtonGrid[i][j].setBackground(Color.RED);
					shipsButtonGrid[i][j].setOpaque(true);
				}
			}
		}
		
		switch(model.getPlayer().getState()) {
			case HIT:
				playerStatusLabel.setText(PLAYER_STATUS + "It hurts, doesn't it?");
				break;
			
			case HITANDSUNK:
				if(model.getPlayer().isDefeated())
					playerStatusLabel.setText(PLAYER_STATUS + "Ha-ha! You're a looser!");
				else
					playerStatusLabel.setText(PLAYER_STATUS + "Ha-ha! I sunk your ship!");
				break;
				
			case WATER:
				playerStatusLabel.setText(PLAYER_STATUS + "I will hit you next time!");
				break;
		}
	}
	
	/**
	 * <p>
	 * Sets all the components to create the hits grid, where the user can see its fired hits.
	 * It creates a {@link javax.swing.JPanel} with GridLayout and fills its cells with 
	 * {@link javax.swing.JButton}s. Its {@link javax.swing.JButton}s are disabled whenever they
	 * have already been clicked, and they are enabled and white-colored if they still are not. When the user
	 * clicks on a {@link javax.swing.JButton} from this grid, if the {@link model.Computer}
	 * had set a {@link model.Ship} in that position, on update the cell turns red, blue if
	 * the user missed. 
	 * </p>
	 * <p>
	 * If the user is restoring a saved match, cells can also be blue
	 * if the {@link model.Player} had hit that cell but there was no {@link model.Ship}
	 * underneath, or red if the corresponding {@link model.Player} hit was successful.
	 * </p>
	 */
	public void setHitsGrid() {
		int gameSize = model.getGameSize();
		int dim = 0;
		if(gameSize == 10)
			dim = smallDim;
		if(gameSize == 15)
			dim = mediumDim;
		if(gameSize == 20)
			dim = largeDim;
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		//Player's shipsGrid
		boolean[][] shipsGrid = new boolean[gameSize][gameSize];
		shipsGrid = model.getComputer().getShipsGrid();
		
		boolean[][] hitsGrid = new boolean[gameSize][gameSize];
		hitsGrid = model.getComputer().getHitsGrid();
		
		hitsGridPanel = new JPanel();
		hitsGridPanel.setLayout(new GridLayout(gameSize + 1, gameSize + 1));
		
		//the buttonGrid must be one cell smaller than shipsPanel
		//i.e. as big as Player's shipsGrid
		hitsButtonGrid = new JButton[gameSize][gameSize];
		
		for(int i = 0; i < gameSize + 1; ++i) {
			for(int j = 0; j < gameSize + 1; ++j) {
				
				if(i == 0 && j == 0) {
					JTextField t = new JTextField();
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					hitsGridPanel.add(t);
				}
				
				else if(i == 0 && j != 0) {
					JTextField t = new JTextField(Utility.COLUMNS[j]);
					t.setHorizontalAlignment(JTextField.CENTER);
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					hitsGridPanel.add(t);
				}
				
				else if(i != 0 && j == 0) {
					JTextField t = new JTextField(Utility.ROWS[i]);
					t.setHorizontalAlignment(JTextField.CENTER);
					t.setPreferredSize(new Dimension(dim, dim));
					t.setEditable(false);
					hitsGridPanel.add(t);
				}
				
				else {
					hitsButtonGrid[i-1][j-1] = new JButton();
					hitsButtonGrid[i-1][j-1].setPreferredSize(new Dimension(dim, dim));
					
					//if there are no ships and no hits (true/true)
					if(shipsGrid[i-1][j-1] && hitsGrid[i-1][j-1]) {
						hitsButtonGrid[i-1][j-1].setBackground(Color.WHITE);
						hitsButtonGrid[i-1][j-1].addActionListener(battleController);
					}
					//if there are ships but no hits (false/true)
					else if(!shipsGrid[i-1][j-1] && hitsGrid[i-1][j-1]){
						hitsButtonGrid[i-1][j-1].setBackground(Color.WHITE);
						hitsButtonGrid[i-1][j-1].addActionListener(battleController);
					}
					//if there are no ships but hits (true/false)
					else if(shipsGrid[i-1][j-1] && !hitsGrid[i-1][j-1]) {
						hitsButtonGrid[i-1][j-1].setBackground(Color.BLUE);
						hitsButtonGrid[i-1][j-1].setOpaque(true);
						hitsButtonGrid[i-1][j-1].setEnabled(false);
					}
					//if there are both ships and hits (false/false)
					else if(!shipsGrid[i-1][j-1] && !hitsGrid[i-1][j-1]) {
						hitsButtonGrid[i-1][j-1].setBackground(Color.RED);
						hitsButtonGrid[i-1][j-1].setOpaque(true);
						hitsButtonGrid[i-1][j-1].setEnabled(false);
					}
					hitsGridPanel.add(hitsButtonGrid[i-1][j-1]);
				}
			}
		}
		hitsGridPanel.setBorder(hitsGridTitle);
		hitsGridPanel.setVisible(true);
		rightPanel.add(hitsGridPanel, BorderLayout.NORTH);
		rightPanel.add(computerStatusLabel, BorderLayout.CENTER);
		bothGridsPanel.add(rightPanel);
	}
	
	/**
	 * Whenever an enabled {@link javax.swing.JButton} from the ships grid is pressed,
	 * the grid is updated. That button turns disabled and changes its color:
	 * it becomes red if the button's coordinates match those of a {@link model.Computer}'s
	 * {@link model.Ship}, blue otherwise.
	 */
	public void updateHitsGrid() {
		int gameSize = model.getGameSize();
		
		boolean[][] shipsGrid = new boolean[gameSize][gameSize];
		shipsGrid = model.getComputer().getInitialShipsGrid();
		
		boolean[][] hitsGrid = new boolean[gameSize][gameSize];
		hitsGrid = model.getComputer().getHitsGrid();
		
		for(int i = 0; i < gameSize; ++i) {
			for(int j = 0; j < gameSize; ++j) {				
				//if there are no ships and no hits (true/true)
				if(shipsGrid[i][j] && hitsGrid[i][j]) {
					
				}
				
				//if there are ships but no hits (false/true)
				else if(!shipsGrid[i][j] && hitsGrid[i][j]){
					
				}
				
				//if there are no ships but hits (true/false)
				else if(shipsGrid[i][j] && !hitsGrid[i][j]) {
					hitsButtonGrid[i][j].setBackground(Color.BLUE);
					hitsButtonGrid[i][j].setEnabled(false);
				}
				
				//if there are both ships and hits (false/false)
				else if(!shipsGrid[i][j] && !hitsGrid[i][j]) {
					hitsButtonGrid[i][j].setBackground(Color.RED);
					hitsButtonGrid[i][j].setEnabled(false);
				}
				hitsButtonGrid[i][j].setOpaque(true);
				
				if(model.getPlayer().isDefeated() || model.getComputer().isDefeated() || model.getPlayer().isTimedOut()) {
					hitsButtonGrid[i][j].setEnabled(false);
				}
			}
		}
		
		switch(model.getComputer().getState()) {
			case HIT:
				computerStatusLabel.setText(COMPUTER_STATUS + "Ouch, you hit me!");
				break;
			
			case HITANDSUNK:
				if(model.getComputer().isDefeated())
					computerStatusLabel.setText(COMPUTER_STATUS + "Oh no! My fleet!");
				else
					computerStatusLabel.setText(COMPUTER_STATUS + "Damn! You sunk my ship!");
				break;
				
			case WATER:
				computerStatusLabel.setText(COMPUTER_STATUS + "Ha-ha! You missed!");
				break;
		}
		
		
	}
	
	/**
	 * <p>
	 * Creates and fills a {@link javax.swing.JPanel} with a 
	 * {@link javax.swing.JButton} and two {@link javax.swing.JLabel}s for win/loss conditions.
	 * When the {@link javax.swing.JButton} is pressed, the current instance of 
	 * {@link model.BattleshipModel} is saved on a binary file.
	 * </p>
	 * <p>
	 * The two {@link javax.swing.JLabel}s are initially invisible, but on win/loss
	 * condition the correct {@link javax.swing.JLabel} is shown.
	 * </p>
	 */
	public void setButtonsPanel() {
		allButtonsPanel = new JPanel();
		allButtonsPanel.setLayout(new FlowLayout());
		
		saveButton = new JButton("SAVE GAME");
		saveButton.addActionListener(battleController);

		if(model.isJustSaved()) {
			saveButton.setEnabled(false);
		}
		else {
			saveButton.setEnabled(true);
		}
		
		allButtonsPanel.add(saveButton);

		youWinLabel.setHorizontalAlignment(JLabel.CENTER);
		youWinLabel.setFont(new Font("Monospace", Font.PLAIN, 50));
		allButtonsPanel.add(youWinLabel);
		youWinLabel.setVisible(false);
		
		
		youLoseLabel.setHorizontalAlignment(JLabel.CENTER);
		youLoseLabel.setFont(new Font("Monospace", Font.PLAIN, 50));
		allButtonsPanel.add(youLoseLabel);
		youLoseLabel.setVisible(false);
			
		add(allButtonsPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Returns a {@link javax.swing.JButton} from the hits grid.
	 * @param i The row coordinate of the {@link javax.swing.JButton}'s position.
	 * @param j The column coordinate of the {@link javax.swing.JButton}'s position.
	 * @return A {@link javax.swing.JButton} instance.
	 */
	public JButton getButtonFromHitsButtonGrid(int i, int j) {
		return hitsButtonGrid[i][j];
	}
	
	/**
	 * Gets the remaining seconds to the end of the countdown.
	 * @return A long representing the remaining seconds to the end of the countdown.
	 */
	public long getSecsLeft() {
		return timerPanel.getCurrentTime();
	}

	@Override
	/**
	 * Called either on first access or whenever a {@link javax.swing.JButton} is pressed on this panel. 
	 * It updates all of its components according to the data the {@link model.BattleshipModel} holds.
	 */
	public void update(Observable o, Object arg) {
		if(model.getState() == BattleshipState.BATTLE) {
			if(!here) {
				setAllComponents();
				this.setVisible(true);
				if(model.isTimed())
					timerPanel.timerStart();
				here = true;
				updateShipsGrid();
				updateHitsGrid();
			}
			//real update
			else
				updateAllComponents();
			
			//update saveButton
			if(model.isJustSaved()) {
				saveButton.setEnabled(false);
			}
			else {
				saveButton.setEnabled(true);
			}
			
			//check for win/lose conditions
			if(model.getPlayer().isDefeated() || model.getComputer().isDefeated() || model.getPlayer().isTimedOut()) {

				saveButton.setVisible(false);
				
				//if computer wins
				if(model.getPlayer().isDefeated()) {
					youLoseLabel.setVisible(true);
					if(timerPanel != null)
						timerPanel.timerStop();
				}
				//if time runs out
				if(model.getPlayer().isTimedOut()) {
					if(timerPanel != null)
						timerPanel.timerStop();
					youLoseLabel.setVisible(true);
				}
				//if player wins
				if(model.getComputer().isDefeated()) {
					if(timerPanel != null)
						timerPanel.timerStop();
					youWinLabel.setVisible(true);
				}
			}
		}
		else {
			this.setVisible(false);
			here = false;
		}
		
		
	}
}
