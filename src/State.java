public class State
{
	char[][] board;
	State parent = null;
	int h = 0, depth = 0;
	String previousMove = "";
	char whoseMove = 'b';

	public State()
	{
		board = new char[8][8];
		boolean skip = true;
		for (int r = 0; r < 3; r++)
		{
			for (int c = 0; c < board.length; c++)
			{
				if(!skip) board[r][c] = 'r';
				skip = !skip;
			}
			skip = !skip;
		}
		
		skip = false;
		for (int r = 5; r < 8; r++)
		{
			for (int c = 0; c < board.length; c++)
			{
				if(!skip) board[r][c] = 'b';
				skip = !skip;
			}
			skip = !skip;
		}
	}

	public State(State other)
	{
		board = new char[other.board.length][other.board.length];
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board.length; c++)
				board[r][c] = other.board[r][c];

		parent = other;
		depth = other.depth + 1;
		whoseMove = other.whoseMove;
	}

	public boolean equals(Object other)
	{
		if (other instanceof State)
		{
			for (int r = 0; r < board.length; r++)
				for (int c = 0; c < board.length; c++)
					if (board[r][c] != ((State) other).board[r][c])
						return false;
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		String s = "";
		
		s += "h: "+h+" depth: "+depth+" whoseMove: "+whoseMove+"\n";
		
		return s;
		
	}
}