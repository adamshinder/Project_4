import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;

public class AI {
	char myToken, oppToken;
	long startTime = 0, MAX_RUNNING_TIME = 30000, MAX_DEPTH = 4;

	public AI(char token, char opToken) {
		myToken = token;
		oppToken = opToken;

	}

	// choose a move NOT randomly
	// miniMax! minValue and maxValue
	// heuristic function
	// generate successors - only made for jumps

	// my opponents best move is whats bad for me
	// their best is my worst
	// I want states that look good for me
	// max H is the highest posible heuristic
	// opp Player - Min H - the worst for you
	// min - gimme the worst
	// max - give me the best
	// unti you reach a stop - break;

	public int h(State st) {
		int h = 0;
		HashMap<Point, ArrayList<Point>> jumps = listJumps(st);

		// for each of my tokens
		for (int r = 0; r < st.board.length; r++)
			for (int c = 0; c < st.board.length; c++) {
				if (st.board[r][c] == myToken) {
					// is against a wall
					if (r == st.board.length - 1 || c == st.board.length - 1 || r == 0 || c == 0) {
						h = h - 10;
					}

					Point p = new Point(r, c);
					if (jumps.containsKey(p)) {
						h = h + 100;
					}

					if (isEmpty(st, r - 1, c - 1) == true) {
						h = h + 2;
					}

					if (isEmpty(st, r - 1, c + 1) == true) {
						h = h + 2;
					}
					if (isEmpty(st, r + 1, c + 1) == true) {
						h = h + 2;
					}
					if (isEmpty(st, r - 1, c - 1) == true) {
						h = h + 2;
					}
				}

				else if (st.board[r][c] == oppToken) {
					Point p = new Point(r, c);
					if (jumps.containsKey(p)) /* moves to you and you can jump */
					{
						h = h + 1000;
					}
					if (r == st.board.length - 1 || c == st.board.length - 1 || r == 0 || c == 0) {
						h = h + 10;
					}

					if (isEmpty(st, r - 1, c - 1) == true) {
						h = h - 2;
					}

					if (isEmpty(st, r - 1, c + 1) == true) {
						h = h - 2;
					}
					if (isEmpty(st, r + 1, c + 1) == true) {
						h = h - 2;
					}
					if (isEmpty(st, r - 1, c - 1) == true) {
						h = h - 2;
					}

				}
			}
		return h;
	}

	private State maxValue(State st) {
		if (isTerminal(st)) // "terminal" == game over, timed out, deep enough
			return st;

		// tracking the "best" choice so far
		int v = Integer.MIN_VALUE;
		State bestState = null;
		// compare v to each child's value
		for (State child : generateSuccessors(st)) {
			child.h = h(child);
			v = Math.max(v, minValue(child).h);
			if (bestState == null || child.h == v)
				bestState = child;
		}
		st.h = v;
		return bestState;
	}

	private State minValue(State st) {
		if (isTerminal(st))
			return st;

		int v = Integer.MAX_VALUE;
		State bestState = null;
		for (State child : generateSuccessors(st)) {
			child.h = h(child);
			v = Math.min(v, maxValue(child).h);
			if (bestState == null || child.h == v)
				bestState = child;
		}
		st.h = v;
		return bestState;
	}

	public ArrayList<State> generateSuccessors(State st) {
		ArrayList<State> successors = new ArrayList<>();
		// String[] moves = moveText.split("-");

		State newState = new State(st);
		HashMap<Point, ArrayList<Point>> jumps = listJumps(newState);

		if (jumps.size() > 0) {

			for (Point startingPoint : jumps.keySet()) { // for each starting point in the list of available jumps'
				// starting
				// points
				State s = new State(st);
				ArrayList<Point> destinationjumps = jumps.get(startingPoint);
				for (Point destinationPoint : destinationjumps) {

					s.board[destinationPoint.x][destinationPoint.y] = s.board[startingPoint.x][startingPoint.y];
					s.board[(destinationPoint.x + startingPoint.x) / 2][(destinationPoint.y + startingPoint.y) / 2] = 0;
					s.board[startingPoint.x][startingPoint.y] = 0;
					s.whoseMove = s.whoseMove == 'b' ? 'r' : 'b';

					successors.add(s);
				}
				/*
				 * for (int c = 0; c < st.board.length; c++) { if (jumps.size() > 0) {
				 * 
				 * // for everyKey - find the startingPoints of every potential jumps
				 * 
				 * for(jumps.keySet())//keySet - returns a list of startingPoints { // make a
				 * new state State s = new State(st); // make a state that - swap pieces- empty
				 * with piece // what if it - empty the middle piece // swap start w destination
				 * source.x = destination.x; source.y = destination.y; // empty the jumpedSpot
				 * // point.x = r; // point.y = c; // goal:: to take a Hashmap of jumps and
				 * convert it to a list of successors // states need to be states - not points
				 * // }
				 */
			}
			return successors;
		}
		// for each of my tokens
		for (int r = 0; r < st.board.length; r++)
			for (int c = 0; c < st.board.length; c++) {
				Point source = new Point(r, c);
				Point destination = new Point(r - 1, c + 1);
				if (isLegalMove(newState, jumps, source, destination) == 0) {
					State s = new State(st);
					makeMove(s, jumps, source, destination);
					s.whoseMove = newState.whoseMove == 'b' ? 'r' : 'b';
					successors.add(s);

				}
				Point destination2 = new Point(r + 1, c - 1);
				if (isLegalMove(newState, jumps, source, destination2) == 0) {
					State s = new State(st);
					makeMove(s, jumps, source, destination2);
					s.whoseMove = newState.whoseMove == 'b' ? 'r' : 'b';
					successors.add(s);

				}
				Point destination3 = new Point(r + 1, c + 1);
				if (isLegalMove(newState, jumps, source, destination3) == 0) {
					State s = new State(st);
					makeMove(s, jumps, source, destination3);
					s.whoseMove = newState.whoseMove == 'b' ? 'r' : 'b';
					successors.add(s);

				}
				Point destination4 = new Point(r - 1, c - 1);
				if (isLegalMove(newState, jumps, source, destination4) == 0) {
					State s = new State(st);
					makeMove(s, jumps, source, destination4);
					s.whoseMove = newState.whoseMove == 'b' ? 'r' : 'b';
					successors.add(s);

				}
			}
//problems
		// automatically makes jumps for me
		// the AI doesnt move at times - I can move their pieces
		//

		// destination.x = [c-1]
		// destination.y = [c +1 ]]
		// find the potential moves around the "gap"
		// make new states as the succesors

		// does this move work with isLegalMove
		// take jumps and make succesors out of those jumps -
		// take that info and add it to the successors arraylist

		// if there are no jumps. if jumps.isEmpty - just like N puzzle
		// - move things around to make array

//		System.out.print(successors);
		return successors;
	}
	/*
	 * if (isEmpty(st, r - 1, c - 1)==true || isEmpty(st, r - 1, c + 1)==true
	 * ||isEmpty(st, r + 1, c + 1)==true ||isEmpty(st, r - 1, c - 1)==true)
	 * 
	 * { for (int i = 1; i < moves.length; i++) { Point destination =
	 * translateLocationFromString(moves[i]);
	 * 
	 * System.out.println(jumps); if (isLegalMove(newState, jumps, source,
	 * destination) == 0) { makeMove(newState, jumps, source, destination); source =
	 * destination; } else
	 * 
	 * 
	 * //newState.board[r][c] = token; successors.add(newState);
	 * 
	 * } //need to add the jumping moves - add those succesors to the list } } } }
	 * // State newState = new State(st); // newState.board[r][c] = token; //
	 * successors.add(newState);
	 * 
	 * // else;
	 * 
	 */

	// return successors;

	// when should we terminate Minimax
	public boolean isTerminal(State st) {
		return (System.currentTimeMillis() - startTime > MAX_RUNNING_TIME || 
				st.depth >= MAX_DEPTH || isGameOver(st)); // OR
		// time,
		// etc.
	}

	// Method for the AI choosing a move.
	// Initially a random agent.
	public State chooseMove(State st) {
		MAX_DEPTH = MAX_DEPTH + 2;
		startTime = System.currentTimeMillis();
		State bestMove = maxValue(st);
		System.out.println(bestMove);
		// newState.whoseMove = newState.whoseMove == 'b' ? 'r' : 'b'; // switch to the
		// next player

		return bestMove;
	}

	// Takes human String input in the form "10-19-26", validates, then executes the
	// move.
	public State processMove(State st, String moveText, JTextField textField) {
		String[] moves = moveText.split("-");
		Point source = translateLocationFromString(moves[0]);
		State newState = new State(st);
		HashMap<Point, ArrayList<Point>> jumps = listJumps(newState);

		for (int i = 1; i < moves.length; i++) {
			Point destination = translateLocationFromString(moves[i]);

			// System.out.println(jumps);
			int test = isLegalMove(newState, jumps, source, destination);
			if (test == 0) {
				makeMove(newState, jumps, source, destination);
			} else {
				textField.setText("Move rejected for reason " + test + "!");
				textField.setSelectionStart(0);
				textField.setSelectionEnd(textField.getText().length());
				return st;
			}
			source = destination;
		}
		newState.whoseMove = newState.whoseMove == 'b' ? 'r' : 'b';

		return newState;
	}

	// Complete the move on specified State, update the jumps map.
	private void makeMove(State newState, HashMap<Point, ArrayList<Point>> jumps, Point source, Point destination) {
		newState.board[destination.x][destination.y] = newState.board[source.x][source.y];
		newState.board[source.x][source.y] = 0;

		if (jumps.get(source) != null && jumps.get(source).contains(destination)) {
			newState.board[(source.x + destination.x) / 2][(source.y + destination.y) / 2] = 0;
			jumps = listJumps(newState);
		}
	}

	// Checks to see if a specific move is not legal, returns 0 if it is legal,
	// reason number otherwise.
	public int isLegalMove(State st, HashMap<Point, ArrayList<Point>> jumps, Point source, Point destination) {
		// same place?
		if (source.equals(destination))
			return 1;

		// not your token?
		if (isEnemy(st, source.x, source.y) || isEmpty(st, source.x, source.y))
			return 2;

		// wrong direction?
		if (st.board[source.x][source.y] == 'b') {
			if (destination.x > source.x)
				return 3;
		}

		if (st.board[source.x][source.y] == 'r') {
			if (destination.x < source.x)
				return 4;
		}

		// destination isn't empty?

		if (!isEmpty(st, destination.x, destination.y))
			return 5;

		// enforce jump priority
		if (jumps == null)
			jumps = listJumps(st);
		if (jumps.size() > 0 && // any jumps?
				(!jumps.containsKey(source) // is the source in the collection of jumps?
						|| !jumps.get(source).contains(destination))) // is the destination found under the key?
			return 6;

		// make sure difference is 1, if there aren't any jumps
		if (jumps.size() == 0 && (Math.abs(source.x - destination.x) > 1 || Math.abs(source.y - destination.y) > 1))
			return 7;

		return 0;
	}

	// Checks whether the specified location on the board is both in bounds and
	// empty.
	public boolean isEmpty(State st, int r, int c) {
		return r >= 0 && r < st.board.length && c >= 0 && c < st.board.length && st.board[r][c] == 0;
	}

	// Checks to see if the specified location is an opponent's piece, based on
	// whose turn it is.
	public boolean isEnemy(State st, int dr, int dc) {
		if (!(dr >= 0 && dr < st.board.length && dc >= 0 && dc < st.board.length))
			return false;
		if (st.whoseMove == 'b')
			return st.board[dr][dc] == 'r' || st.board[dr][dc] == 'R';
		else
			return st.board[dr][dc] == 'b' || st.board[dr][dc] == 'B';
	}

	// Checks to see if another square is an enemy to the one at [r][c]
	public boolean isEnemy(State st, int r, int c, int dr, int dc) {
		if (!(dr >= 0 && dr < st.board.length && dc >= 0 && dc < st.board.length))
			return false;
		if (st.board[r][c] == 'b')
			return st.board[dr][dc] == 'r' || st.board[dr][dc] == 'R';
		else
			return st.board[dr][dc] == 'b' || st.board[dr][dc] == 'B';
	}

	// Produces a Map of all the legal jumps.
	public HashMap<Point, ArrayList<Point>> listJumps(State st) {
		HashMap<Point, ArrayList<Point>> jumps = new HashMap<>();
		for (int r = 0; r < st.board.length; r++) {
			for (int c = 0; c < st.board.length; c++) {
				if ((st.whoseMove == 'b' && (st.board[r][c] == 'b' || st.board[r][c] == 'B'))
						|| (st.whoseMove == 'r' && st.board[r][c] == 'R')) {
					// look ahead one row and one column to either side
					if (isEnemy(st, r - 1, c - 1) && isEmpty(st, r - 2, c - 2)) {
						Point p = new Point(r, c);
						jumps.put(p, new ArrayList<Point>());
						jumps.get(p).add(new Point(r - 2, c - 2));
					}
					if (isEnemy(st, r - 1, c + 1) && isEmpty(st, r - 2, c + 2)) {
						Point p = new Point(r, c);
						jumps.put(p, new ArrayList<Point>());
						jumps.get(p).add(new Point(r - 2, c + 2));
					}
				}
				if ((st.whoseMove == 'r' && (st.board[r][c] == 'r' || st.board[r][c] == 'R'))
						|| (st.whoseMove == 'b' && st.board[r][c] == 'B')) {
					if (isEnemy(st, r + 1, c - 1) && isEmpty(st, r + 2, c - 2)) {
						Point p = new Point(r, c);
						jumps.put(p, new ArrayList<Point>());
						jumps.get(p).add(new Point(r + 2, c - 2));
					}
					if (isEnemy(st, r + 1, c + 1) && isEmpty(st, r + 2, c + 2)) {
						Point p = new Point(r, c);
						jumps.put(p, new ArrayList<Point>());
						jumps.get(p).add(new Point(r + 2, c + 2));
					}
				}
			}
		}
		return jumps;
	}

	// Given a Point, convert it back to Checkers coordinates.
	public int translateLocationFromPoint(Point p) {
		int c = p.x % 2 == 0 ? p.y : p.y + 1;

		return (p.x * 4) + (c - (c / 2));
	}

	// Given a String, convert it to a Point, where Point.x == row, Point.y ==
	// column.
	public Point translateLocationFromString(String squareID) {
		return translateLocationFromInt(Integer.parseInt(squareID));
	}

	// Given an integer, convert it to a Point, where Point.x == row, Point.y ==
	// column.
	public Point translateLocationFromInt(int id) {
		int row = (id - 1) / 4;

		int col = 0;
		if (row % 2 == 0) {
			int cid = id % 8;
			col = cid + (cid - 1);
		} else {
			int cid = (id - 1) % 4;
			col = cid * 2;
		}

		return new Point(row, col);
	}

	// Game is over if there are no pieces left for one side.
	public boolean isGameOver(State st) {
		int rs = 0, bs = 0;
		for (int r = 0; r < st.board.length; r++)
			for (int c = 0; c < st.board.length; c++) {
				if (st.board[r][c] == 'b' || st.board[r][c] == 'B')
					bs++;
				if (st.board[r][c] == 'r' || st.board[r][c] == 'R')
					rs++;
			}

		return rs == 0 || bs == 0;
	}
}