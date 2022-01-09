package controller;
import view.CountdownPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import model.BattleshipModel;

/**
 * Controller for a {@link CountdownPanel}. It decrements the timer's value
 * every second or displays a red background it the time has run out.
 * 
 * @author 20027017 & 20031485
 *
 */
public class CountdownController implements ActionListener{
	private BattleshipModel model;
	private CountdownPanel panel;
	
	/**
	 * Constructor for the class {@link CountdownPanel}, initialized with a 
	 * {@link BattleshipModel} instance and a {@link CountdownPanel}.
	 * @param panel A {@link BattleshipModel} instance.
	 * @param model A {@link CountdownPanel} instance. 
	 */
	public CountdownController(BattleshipModel model, CountdownPanel panel) {
		this.model = model;
		this.panel = panel;
	}
	
	@Override
    /**
     * When an {@link ActionEvent} is fired by the {@link CountdownPanel}'s timer,
     * the time left is checked and if it has run out, the {@link CountdownPanel}'s
     * background becomes red and its timer stops. The time left is decreased otherwise.
     * @param e An {@link ActionEvent} fired by the {@link CountdownPanel}'s timer.
     */
	public void actionPerformed(ActionEvent e){
		//if the countdown is not over
		if(!panel.isTimeOver()) {
			//decrease the current time
			panel.setCurrentTime(panel.getCurrentTime() - 1000);
			//display the new current time on the timerLabel
			panel.getTimerLabel().setText(panel.getDateFormat().format(new Date(panel.getCurrentTime())));
		}
		else {
			panel.setBackground(Color.RED);
			model.getPlayer().setTimedOut(true);
		}
	}
}
