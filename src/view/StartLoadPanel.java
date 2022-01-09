package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.BattleshipController;
import controller.StartLoadController;
import model.BattleshipModel;
import utils.BattleshipState;

/**
 * <p>This class extends a {@link javax.swing.JPanel} that is shown
 * according to the {@link model.BattleshipModel}'s state by
 * the {@link BattleshipView}. It contains a {@link javax.swing.JLabel}
 * and two {@link javax.swing.JButton}s, and it is the first panel
 * being shown by the {@link BattleshipView}. When "New game" is pressed,
 * it triggers a {@link utils.BattleshipState} change in the 
 * {@link model.BattleshipModel}, so that the {@link NewGamePanel} is
 * shown. 
 * </p>
 * <p>If a binary saved file exists and has a certain name, the
 * "Load game" {@link javax.swing.JButton} is made visible. If
 * the user presses "Load game", if the file is corrupted or removed,
 * the pressure simply makes the {@link javax.swing.JButton} disappear.
 * It triggers a {@link utils.BattleshipState} change in the 
 * {@link model.BattleshipModel}, so that the {@link BattlePanel} 
 * with the loaded unfinished match is shown.
 * </p>
 * 
 * @author 20027017 & 20031485
 *
 */
public class StartLoadPanel extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;
	
	private static final int WIDTH = 300;
	private static final int HEIGHT = 100;
	private static final String TITLE = "NEW/LOAD GAME";
	private BattleshipModel model;
	private BattleshipController controller;
	private StartLoadController startLoadController;
	private JLabel label;
	private JPanel mainPanel;
	private JPanel buttonPanel;
	private JButton newGameButton;
	public JButton loadGameButton;
	
	private boolean here = false;
	
	/**
	 * Constructor for the class {@link NewGamePanel}. It initializes a
	 * {@link javax.swing.JPanel} and fills it with graphic elements
	 * useful for manually choosing if starting a new game or loading 
	 * an existing unfinished match.
	 * 
	 * @param model
	 * @param controller
	 */
	StartLoadPanel(BattleshipModel model, BattleshipController controller){
		this.model = model;
		this.controller = controller;
		this.startLoadController = this.controller.giveStartLoadController(model, this);
		this.here = true;
		
		//add Observer
		this.model.addObserver(this);

		//settings
		this.setLayout(new BorderLayout());
		this.setSize(WIDTH, HEIGHT);
		
		//mainPanel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		//JLabel
		label = new JLabel("Welcome to UPOBattleship!");
		label.setHorizontalAlignment(JLabel.CENTER);
		
		mainPanel.add(label, BorderLayout.NORTH);
		
		//JPanel for JButtons
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		newGameButton = new JButton("New game");
		newGameButton.addActionListener(startLoadController);
		buttonPanel.add(newGameButton);
		
		loadGameButton = new JButton("Load game");
		loadGameButton.addActionListener(startLoadController);
		buttonPanel.add(loadGameButton);
		
		if(!BattleshipModel.savedGameExists())
			loadGameButton.setVisible(false);
		
		//add panel to frame
		mainPanel.add(buttonPanel, BorderLayout.CENTER);
		add(mainPanel);
		setVisible(true);
	}
		
	/**
	 * Gets the title of the {@link StartLoadPanel}.
	 * @return A {@link String} containing the title of 
	 * the {@link StartLoadPanel}.
	 */
	public String getTitle() {
		return TITLE;
	}

	@Override
	/**
	 * If the {@link model.BattleshipModel} is in a certain 
	 * {@link utils.BattleshipState}, this panel is made visible.
	 * It is made invisible otherwise.
	 */
	public void update(Observable o, Object arg) {
		if(model.getState() == BattleshipState.WELCOME) {
			if(!here) {
				if(BattleshipModel.savedGameExists())
					loadGameButton.setVisible(true);
				this.setVisible(true);
				here = true;
			}
		}
		else {
			this.setVisible(false);
			here = false;
		}
	}
}
