package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import controller.BattleshipController;
import controller.NewGameController;
import model.BattleshipModel;
import utils.BattleshipState;

/**
 * This class extends a {@link javax.swing.JPanel} that is shown
 * according to the {@link model.BattleshipModel}'s state by
 * the {@link BattleshipView}. This graphic element contains various
 * {@link javax.swing.JPanel}s, {@link javax.swing.JButton}s and
 * {@link javax.swing.JRadioButton}s, so that the user can easily
 * choose the settings for its Battleship match, such as grid size, 
 * {@link utils.ComputerType} and difficulty and timer.
 * 
 * @author 20027017 & 20031485
 *
 */
public class NewGamePanel extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;
	
	//attributes
	private static final int WIDTH = 350;
	private static final int HEIGHT = 250;
	private static final String TITLE = "NEW GAME SETTINGS";

	//model
	private BattleshipModel model;
	private BattleshipController controller;
	private NewGameController newGameController;
		
	//JPanels
	private JPanel gameModePanel;
	private JPanel radioButtonSizePanel;
	private JPanel buttonPanel;
	private JPanel confirmBackButtonPanel;
	public JPanel difficultyPanel;
	public JPanel timedPanel;
	private JPanel chooseGameModePanel;
	//private JPanel sizeButtonsPanel;
	
	//JLabels
	private JLabel gameModeLabel;
	private JLabel gridSizeLabel;
	private JLabel difficultyLabel;
	
	//JButtons
	protected JButton confirmButton;
	protected JButton backButton;
	
	//JRadioButtons
	public JRadioButton p1vsp2Button;
	public JRadioButton p1vsCPUButton;
	public JRadioButton easyModeButton;
	public JRadioButton hardModeButton;
	public JRadioButton sizeSButton;
	public JRadioButton sizeMButton;
	public JRadioButton sizeLButton;
	public JRadioButton sizeXLButton;
	public JRadioButton timed1minsButton;
	public JRadioButton timed2minsButton;
	public JRadioButton timed5minsButton;
	
	//JCheckboxes
	public JCheckBox timedCheckBox;
	
	//ButtonGroups
	private ButtonGroup radioButtonModeGroup;
	private ButtonGroup radioButtonSizeGroup;
	private ButtonGroup difficultyButtonGroup;
	private ButtonGroup timedButtonGroup;
	
	private boolean here = false;
	
	/**
	 * Constructor for the class {@link NewGamePanel}. It initializes a
	 * {@link javax.swing.JPanel} and fills it with graphic elements
	 * useful for manually choosing the desired Battleship settings.
	 * 
	 * @param model A {@link model.BattleshipModel} instance.
	 * @param controller A {@link controller.BattleshipController} instance.
	 */
	public NewGamePanel(BattleshipModel model, BattleshipController controller) {
		this.model = model;
		this.controller = controller;
		this.newGameController = this.controller.giveNewGameController(model, this);
		
		this.model.addObserver(this);
		
		//settings
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new BorderLayout());
		
		//all labels
		gameModeLabel = new JLabel("GAME MODE");
		gameModeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		difficultyLabel = new JLabel("DIFFICULTY");
		difficultyLabel.setHorizontalAlignment(JLabel.CENTER);
		
		gridSizeLabel = new JLabel("GRID SIZE");
		gridSizeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//all buttons
		confirmButton = new JButton("CONFIRM");
		confirmButton.addActionListener(newGameController);
		
		backButton = new JButton ("BACK");
		backButton.addActionListener(newGameController);
		
		p1vsp2Button = new JRadioButton("P1vsP2 (unavailable)");
		p1vsp2Button.addActionListener(newGameController);
		
		p1vsCPUButton = new JRadioButton("P1vsCPU");
		p1vsCPUButton.setSelected(true);
		p1vsCPUButton.addActionListener(newGameController);
		
		easyModeButton = new JRadioButton("easy cheesy");
		easyModeButton.setSelected(true);
		
		hardModeButton = new JRadioButton("hard as hell");
		
		sizeMButton = new JRadioButton("10x10");
		sizeMButton.setSelected(true);
		sizeLButton = new JRadioButton("15x15");
		sizeXLButton = new JRadioButton("20x20");
		
		timedCheckBox = new JCheckBox("TIMED");
		timedCheckBox.setSelected(false);
		timedCheckBox.setHorizontalAlignment(JLabel.CENTER);
		
		timed1minsButton = new JRadioButton("1min");
		timed1minsButton.setSelected(true);
		timed2minsButton = new JRadioButton("2mins");
		timed5minsButton = new JRadioButton("5mins");
				
		//all buttonGroups		
		radioButtonModeGroup = new ButtonGroup();
		radioButtonModeGroup.add(p1vsp2Button);
		radioButtonModeGroup.add(p1vsCPUButton);
		
		difficultyButtonGroup = new ButtonGroup();
		difficultyButtonGroup.add(easyModeButton);
		difficultyButtonGroup.add(hardModeButton);
		
		radioButtonSizeGroup = new ButtonGroup();
		radioButtonSizeGroup.add(sizeSButton);
		radioButtonSizeGroup.add(sizeMButton);
		radioButtonSizeGroup.add(sizeLButton);
		radioButtonSizeGroup.add(sizeXLButton);
		
		timedButtonGroup = new ButtonGroup();
		timedButtonGroup.add(timed1minsButton);
		timedButtonGroup.add(timed2minsButton);
		timedButtonGroup.add(timed5minsButton);
		
		//all panels
		difficultyPanel = new JPanel();
		difficultyPanel.setLayout(new BorderLayout());
		
		gameModePanel = new JPanel();
		gameModePanel.setLayout(new BorderLayout());
		
		confirmBackButtonPanel = new JPanel();
		confirmBackButtonPanel.setLayout(new FlowLayout());
		
		radioButtonSizePanel = new JPanel();
		radioButtonSizePanel.setLayout(new BorderLayout());		
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		chooseGameModePanel = new JPanel();
		chooseGameModePanel.setLayout(new BorderLayout());
		
		timedPanel = new JPanel();
		timedPanel.setLayout(new BorderLayout());
	
		gameModePanel.add(gameModeLabel, BorderLayout.NORTH);
		gameModePanel.add(p1vsp2Button, BorderLayout.CENTER);
		gameModePanel.add(p1vsCPUButton, BorderLayout.SOUTH);
		
		difficultyPanel.add(difficultyLabel, BorderLayout.NORTH);
		difficultyPanel.add(easyModeButton, BorderLayout.CENTER);
		difficultyPanel.add(hardModeButton, BorderLayout.SOUTH);
		
		chooseGameModePanel.add(gameModePanel, BorderLayout.WEST);
		chooseGameModePanel.add(difficultyPanel, BorderLayout.EAST);
				
		buttonPanel.add(chooseGameModePanel);
		
		radioButtonSizePanel.add(gridSizeLabel, BorderLayout.NORTH);
		radioButtonSizePanel.add(sizeMButton, BorderLayout.WEST);
		radioButtonSizePanel.add(sizeLButton, BorderLayout.CENTER);
		radioButtonSizePanel.add(sizeXLButton, BorderLayout.EAST);
		
		
		buttonPanel.add(radioButtonSizePanel);
		
		timedPanel.add(timedCheckBox, BorderLayout.NORTH);
		timedPanel.add(timed1minsButton, BorderLayout.WEST);
		timedPanel.add(timed2minsButton, BorderLayout.CENTER);
		timedPanel.add(timed5minsButton, BorderLayout.EAST);
		
		buttonPanel.add(timedPanel);
		
		confirmBackButtonPanel.add(confirmButton);
		confirmBackButtonPanel.add(backButton);

		buttonPanel.add(confirmBackButtonPanel);
		//NewGamePanel
		this.add(buttonPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Gets the title of the {@link NewGamePanel}.
	 * @return A {@link String} containing the title of 
	 * the {@link NewGamePanel}.
	 */
	public static String getTitle() {
		return TITLE;
	}

	@Override
	/**
	 * If the {@link model.BattleshipModel} is in a certain 
	 * {@link utils.BattleshipState}, this panel is made visible.
	 * It is made invisible otherwise.
	 */
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(model.getState() == BattleshipState.NEWGAME) {
			if(!here) {
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
