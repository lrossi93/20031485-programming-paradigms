package view;


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import controller.CountdownController;
import model.BattleshipModel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that represents the timer for a timed Battleship match.
 * If the {@link model.BattleshipModel} is initialized as "timed", 
 * according to the time of initialization it has stored, this class
 * creates a {@link javax.swing.JPanel} with a {@link javax.swing.Timer}
 * initialized with the same count down as the {@link model.BattleshipModel}.
 * 
 * @author 20027017 & 20031485 
 *
 */
public class CountdownPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BattleshipModel model;
	private CountdownController controller;
	
	private Timer timer;
	//tempo totale a disposizione
	private long time;
	//tempo mancante
	private long currentTime;
	private final JLabel timerLabel;
	private static final TitledBorder timerTitle = BorderFactory.createTitledBorder("TIMER");
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("mm : ss");
	
	/**
	 * Constructor for the class {@link CountdownPanel}. It takes the number of seconds and
	 * a {@link model.BattleshipModel} instance and initializes a {@link javax.swing.Timer}.
	 * @param secs The seconds of count down.
	 * @param model A {@link model.BattleshipModel} instance.
	 */
	public CountdownPanel(long secs, BattleshipModel model){
	    //devo trasformare il tempo in millisecondi
		time = secs *1000;
	    currentTime = time;
	    this.model = model;
	    timerLabel = new JLabel(dateFormat.format(new Date(time)),JLabel.CENTER);
	    timerLabel.setFont(new Font("Monospace", Font.PLAIN, 30));
	   	timerLabel.setBorder(timerTitle);
	    controller = new CountdownController(model, this);
	    setTimer(controller);  
	    add(timerLabel);
	}
	
	/**
	 * Returns the stored {@link javax.swing.Timer}.
	 * @return The stored {@link javax.swing.Timer}
	 */
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Sets the stored {@link javax.swing.Timer}.
	 * @param timer A {@link javax.swing.Timer} instance.
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * Returns the time of initialization in milliseconds.
	 * @return The time of initialization in milliseconds.
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the time of initialization.
	 * @param time The time of initialization in milliseconds.
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Returns the time of initialization minus the time that has passed
	 * the start of the count down, in milliseconds.
	 * 
	 * @return The time of initialization minus the time that has passed
	 * the start of the count down, in milliseconds.
	 */
	public long getCurrentTime() {
		return currentTime;
	}

	/**
	 * Sets the current time (initialization time minus time passed from the
	 * start of the count down).
	 * @param currentTime The new current time.
	 */
	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
		this.model.setSecs(currentTime);
	}

	/**
	 * Sets an {@link java.awt.event.ActionListener} to listen to the
	 * actions fired by the stored timer.
	 * @param actionListener An {@link java.awt.event.ActionListener} instance.
	 */
	public void setTimer(ActionListener actionListener) {
		this.timer = new Timer(1000, actionListener);
	}
	
	/**
	 * Starts the stored timer.
	 */
	public void timerStart() {
		this.timer.start();
	}
	
	/**
	 * Stops the stored timer.
	 */
	public void timerStop() {
		this.timer.stop();
	}
	
	/**
	 * Checks if the available time has run out.
	 * @return A boolean: true if the time is up, false otherwise.
	 */
	public boolean isTimeOver() {
		return (currentTime == 0);
	}
	
	/**
	 * Returns a {@link javax.swing.JLabel} containing the time left
	 * to the count down.
	 * @return A {@link javax.swing.JLabel} containing the time left
	 * to the count down.
	 */
	public JLabel getTimerLabel() {
		return this.timerLabel;
	}
	
	/**
	 * Returns the {@link java.text.SimpleDateFormat} used for the 
	 * timer's {@link javax.swing.JLabel} format.
	 * @return The {@link java.text.SimpleDateFormat} used for the 
	 * timer's {@link javax.swing.JLabel} format.
	 */
	public SimpleDateFormat getDateFormat() {
		return this.dateFormat;
	}
}
