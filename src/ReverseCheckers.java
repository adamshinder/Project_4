import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ReverseCheckers extends JFrame implements ActionListener
{
	JLabel infoLabel;
	JTextField inputField;
	CheckCanvas canvas;
	AI ai;
	State currentState;
	Font mainFont = new Font("Arial", Font.PLAIN, 24);
	char aiToken = 'r', playerToken = 'b';

	public ReverseCheckers()
	{
		super("Reverse Checkers");
		ai = new AI(aiToken, playerToken);
		currentState = new State();
		canvas = new CheckCanvas(currentState, mainFont);
		JFrame window = new JFrame("Tic Tac Toe");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int dim = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()
				* 0.8);
		window.setSize(dim, dim);

		infoLabel = new JLabel("Game Start!  Black's Move!",
				SwingConstants.CENTER);
		infoLabel.setFont(mainFont);
		inputField = new JTextField(20);
		inputField.setFont(mainFont);
		inputField.setText("Enter input here.  Ex: 10-14");
		inputField.setSelectionStart(0);
		inputField.setSelectionEnd(inputField.getText().length());
		inputField.setToolTipText("Enter input here.  Ex: 10-14");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(infoLabel, BorderLayout.NORTH);
		mainPanel.add(inputField, BorderLayout.SOUTH);
		mainPanel.add(canvas, BorderLayout.CENTER);
		inputField.addActionListener(this);

		window.setBackground(Color.WHITE);
		window.add(mainPanel);
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		if (currentState.whoseMove == aiToken) doAIMove();
	}

	public static void main(String[] args)
	{ 
		new ReverseCheckers();
	}

	public void doAIMove()
	{
		currentState = ai.chooseMove(currentState);
		canvas.updateBoard(currentState);
	}

	@Override public void actionPerformed(ActionEvent arg0)
	{
		String input = inputField.getText();
		inputField.setText("");

		State newState = ai.processMove(currentState, input, inputField);
		if (newState.equals(currentState)) infoLabel.setText("Invalid move!");
		else
		{
			currentState = newState;
			if (ai.isGameOver(currentState))
			{
				infoLabel.setText("Game over!");
				inputField.setEnabled(false);
			}
			Thread t = new Thread() { public void run(){ canvas.updateBoard(currentState); }};
			t.start();
			doAIMove();
		}

	}

}