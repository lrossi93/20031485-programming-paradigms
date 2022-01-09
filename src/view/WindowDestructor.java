package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class that extends a {@link java.awt.event.WindowAdapter} and creates an
 * {@link ExitFrame} when the {@link BattleshipView}'s exit button is pressed.
 * 
 * @author 200217017 & 20031485
 *
 */
public class WindowDestructor extends WindowAdapter{
	
	/**
	 * Creates an {@link ExitFrame} when the 
	 * {@link BattleshipView}'s exit button is pressed.
	 */
	public void windowClosing(WindowEvent e) {
		ExitFrame exitFrame = new ExitFrame();
		exitFrame.setVisible(true);
	}
}
