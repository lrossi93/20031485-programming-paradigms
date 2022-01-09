package view;

import java.awt.BorderLayout;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import controller.BattleshipController;
import model.BattleshipModel;

/**
 * Class that represents the {@link javax.swing.JFrame} where all panels with 
 * their content are shown. It holds an instance of every panel it uses and
 * both an instance of {@link model.BattleshipModel} and {@link controller.BattleshipController}.
 * Its task is to switch from a panel to the other according to the {@link model.BattleshipModel}
 * state.
 * 
 * @author 20027017 & 20031485
 *
 */
public class BattleshipView extends JFrame implements Observer{
	private static final long serialVersionUID = 1L;
	
	private BattleshipModel model;
	private BattleshipController controller;
	
	private StartLoadPanel startLoadPanel;
	private NewGamePanel newGamePanel;
	private SetShipsPanel setShipsPanel;
	private BattlePanel battlePanel;
	
	/**
	 * Constructor for the class {@link BattleshipView}. It is initialized with a
	 * {@link model.BattleshipModel} instance and initializes its 
	 * {@link controller.BattleshipController} and all panels internally.
	 * @param model A {@link model.BattleshipModel} instance.
	 */
	public BattleshipView(BattleshipModel model) {
		this.model = model;
		this.model.addObserver(this);
		this.controller = new BattleshipController(model);
		
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		addWindowListener(new WindowDestructor());
		setLayout(new BorderLayout());
		
		startLoadPanel = new StartLoadPanel(model, controller);
		add(startLoadPanel);
		startLoadPanel.setVisible(true);
		
		newGamePanel = new NewGamePanel(model, controller);
		add(newGamePanel);
		newGamePanel.setVisible(false);
		
		setShipsPanel = new SetShipsPanel(model, controller);
		add(setShipsPanel);
		setShipsPanel.setVisible(false);
		
		battlePanel = new BattlePanel(model, controller);
		add(battlePanel);
		battlePanel.setVisible(false);
		
		setSize(startLoadPanel.getWidth(), startLoadPanel.getHeight());
		setTitle("WELCOME: " + startLoadPanel.getTitle());
		setVisible(true);
	}

	/**
	 * According to the {@link model.BattleshipModel}'s state, the 
	 * {@link BattleshipView} is updated making the panel corresponding to the 
	 * {@link model.BattleshipModel}'s state visible and making the other panels
	 * invisible.
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		switch(this.model.getState()) {
			case WELCOME:
				setSize(startLoadPanel.getWidth(), startLoadPanel.getHeight());
				break;
			
			case BATTLE:
				setTitle(BattlePanel.getTitle());
				setSize(battlePanel.getWidth(), battlePanel.getHeight());
				break;
		
			case NEWGAME:
				setTitle(NewGamePanel.getTitle());
				setSize(newGamePanel.getWidth(), newGamePanel.getHeight());
				break;
			
			case SETSHIPS:
				setTitle(setShipsPanel.getTitle());	
				setSize(setShipsPanel.getWidth(), setShipsPanel.getHeight());
				break;
				
			default:
				break;
		}
	}
}
