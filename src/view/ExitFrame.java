package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Utility {@link javax.swing.JFrame} created whenever the user clicks on
 * the {@link BattleshipView}'s exit button. This class consists in a 
 * {@link javax.swing.JFrame} with two {@link javax.swing.JButton}s and a 
 * {@link javax.swing.JLabel} and basically asks the user if he/she wants to
 * exit the game. If "YES" is pressed, this class triggers the program termination. 
 * If "NO" is pressed, the focus returns to the main {@link javax.swing.JFrame}, 
 * i.e. {@link BattleshipView}.
 * 
 * @author 20027017 & 20031485
 *
 */
public class ExitFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private static final int width = 200;
	private static final int height = 100;
	
	/**
	 * Constructor for the {@link ExitFrame} object. It creates a 
	 * {@link javax.swing.JFrame} and populates it with two {@link javax.swing.JButton}s
	 * and a {@link javax.swing.JLabel}.
	 */
	public ExitFrame() {
		setSize(width, height);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		JLabel label = new JLabel("Are you sure?");
		label.setHorizontalAlignment(JLabel.CENTER);
		add(label, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		JButton yesButton = new JButton("YES");
		yesButton.addActionListener(this);
		buttonPanel.add(yesButton);
		
		JButton noButton = new JButton("NO");
		noButton.addActionListener(this);
		buttonPanel.add(noButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	/**
	 * Detects an {@link java.awt.event.ActionEvent} fired by the pressure
	 * of {@link ExitFrame}'s buttons and, if "YES" is pressed it terminates
	 * the application; if "NO" is pressed, the focus on the 
	 * {@link BattleshipView} is reestablished and {@link ExitFrame} is closed. 
	 */
	public void actionPerformed(ActionEvent e) {
		//catches the command
		String command = e.getActionCommand();
		//interprets the command
		if(command.equals("YES")) {
			//terminates program
			System.exit(0);
		}
		else if(command.equals("NO")) {
			//closes just the current exit window
			dispose();
		}
		else {
			//this will never be displayed
			System.out.println("ERROR CLOSING WINDOW");
		}
	}
}
