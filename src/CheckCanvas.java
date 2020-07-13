import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class CheckCanvas extends Canvas
{
	State currentState = null;

	public CheckCanvas(State initialState, Font font)
	{
		setFont(font);
		currentState = initialState;
		setBackground(Color.WHITE);
	}

	public void paint(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());

		boolean isCheck = true;
		int squareSize = getHeight() / 8, squareCounter = 1;
		int gutter = (getWidth() - getHeight())/2;
		
		char[][] board = currentState.board;
		for (int r = 0; r < board.length; r++)
		{
			for (int c = 0; c < board[0].length; c++)
			{
				if (isCheck)
				{
					g.setColor(Color.GREEN);
					g.fillRect(c * squareSize+gutter, r * squareSize, squareSize,
							squareSize);
				}
				

				if (board[r][c] == 'b' || board[r][c] == 'B')
					g.setColor(Color.CYAN);
				else g.setColor(Color.RED);

				if (board[r][c] != 0)
					g.fillOval(c * squareSize+gutter, r * squareSize, squareSize, squareSize);
				
				if(!isCheck)
				{
					g.setColor(Color.DARK_GRAY);
					g.drawString(squareCounter++ + "", c*squareSize+gutter, r*squareSize+20);
				}
				isCheck = !isCheck;
			}
			isCheck = !isCheck;
		}
	}

	public void updateBoard(State st)
	{
		currentState = st;
		repaint();
	}

}