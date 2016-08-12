package code.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import code.model.tile.ITile;
import code.model.tile.PlaceHolderTile;
import code.model.tile.RealTile;

/* 
 * I made changes in the lab 8 solution being provided
 * to students that should be incorporated into the
 * lab 9 solution code. 
 */
public class KeyBricksModel {
	
	public static final int COLS = 5;
	public static final int ROWS = COLS;
	
	public String _save;
	
	private Observer _observer;	// who to notify when the model changes
	
	private ArrayList<ArrayList<ITile>> _board; // a representation of the board (non-graphical)
	
	private ArrayList<Character> _letters;
	
	public KeyBricksModel() {
		_observer = null;
		initializeLetters();
		initializeBoard();
	}
	
	public void initializeBoard() {
		_board = new ArrayList<ArrayList<ITile>>();
		for (int c=0; c<COLS; c++) {
			ArrayList<ITile> col = new ArrayList<ITile>();
			_board.add(col);
			for (int r=0; r<ROWS; r++) {
				RealTile t = new RealTile(ColorUtility.getRandomColor());
				col.add(t);
			}
		}
		ensureEnoughTilesWithCharacters();
	}

	public void initializeLetters() {
		_letters = new ArrayList<Character>();
		for (char c = 'a'; c <= 'z'; c = (char) (c + 1)) {
			_letters.add(c);
		}
	}
	
	public void setObserver(Observer obs) {
		_observer = obs;
	}
	
	public void gameStateChanged() {
		if (_observer != null) {
			_observer.update();
		}
	}

	public ITile get(int col, int row) {
		if (col < 0 || col >= COLS) { return null; }
		if (row < 0 || row >= ROWS) { return null; }
		return _board.get(col).get(row);
	}

	public void remove(char ch) {
		for (int c=0; c<COLS; c++) {
			for (int r=0; r<ROWS; r++) {
				ITile t = get(c,r);
				if (t.isRealTile()) {
					if (ch == t.getCharacter()) {
						removeAdjacentSameColorTiles(t);
						ensureEnoughTilesWithCharacters();
						gameStateChanged();
						return;
					}
				}
			}
		}
	}
	
	public void removeAdjacentSameColorTiles(ITile tile) {
		ArrayList<ITile> toBeChecked = new ArrayList<ITile>();
		toBeChecked.add(tile);
		HashSet<ITile> toBeRemoved = new HashSet<ITile>();
		// STAGE 1: find tiles that need to be removed
		while (! toBeChecked.isEmpty()) {
			ITile t = toBeChecked.remove(0);
			toBeRemoved.add(t);
			Point p = locationOfTile(t);
			int c = p.x;
			int r = p.y;
			checkTile(c, r-1, toBeChecked, toBeRemoved, t);
			checkTile(c, r+1, toBeChecked, toBeRemoved, t);
			checkTile(c-1, r, toBeChecked, toBeRemoved, t);
			checkTile(c+1, r, toBeChecked, toBeRemoved, t);
		}
		// STAGE 2: remove them
		for (ITile t : toBeRemoved) {
			removeFromBoard(t);
		}
	}

	public void ensureEnoughTilesWithCharacters() {
		ArrayList<RealTile> allAndOnlyRealTiles;  // all 'real' tiles on the board
		allAndOnlyRealTiles =  allAndOnlyRealTiles();

		// check if game is over
		if (allAndOnlyRealTiles().size() == 0) {
			System.out.println("Game over!");
			System.exit(0);
		}
		
		// count how many tiles have a letter
		int howManyTilesWithLetters = 0;
		for (RealTile t : allAndOnlyRealTiles) {
			if (t.getCharacter() != ' ') {
				howManyTilesWithLetters = howManyTilesWithLetters + 1;
			}
		}
		
		// make sure new letters are placed randomly
		Collections.shuffle(allAndOnlyRealTiles);

		// replenish the supply of letter tiles on board
		int numberOfLettersWeWantToAddToBoard = COLS-howManyTilesWithLetters;
		int numberOfPlaceHolderTilesOnBoard = allAndOnlyRealTiles.size() - howManyTilesWithLetters;
		int maximumNumberOfLettersWeCanAddToBoard = Math.min(numberOfLettersWeWantToAddToBoard, numberOfPlaceHolderTilesOnBoard);
		for (int i=0; i < maximumNumberOfLettersWeCanAddToBoard; i = i + 1) {
			RealTile t = getRealTileWithoutLetter(allAndOnlyRealTiles);
			t.setCharacter(_letters.remove(0));
		}
	}

	public RealTile getRealTileWithoutLetter(ArrayList<RealTile> tiles) {
		for (RealTile t : tiles) {
			if (t.getCharacter() == ' ') {
				return t;
			}
		}
		return null; // this should never happen
	}

	// find all the RealTiles on the board, return as an ArrayList
	public ArrayList<RealTile> allAndOnlyRealTiles() {
		ArrayList<RealTile> allAndOnlyRealTiles;  // all 'real' tiles on the board
		allAndOnlyRealTiles = new ArrayList<RealTile>();
		for (int c=0; c<COLS; c++) {
			for (int r=0; r<ROWS; r++) {
				ITile t = get(c,r);
				if (t.isRealTile()) {
					allAndOnlyRealTiles.add((RealTile) t);  // add all and only RealTiles to collection
				}
			}
		}
		return allAndOnlyRealTiles;
	}

	public Point locationOfTile(ITile t) {
		for (int c=0; c<COLS; c++) {
			for (int r=0; r<ROWS; r++) {
				if ( t == get(c,r) ) { // '==' is correct here - we want to find exactly the tile t
					return new Point(c,r);
				}
			}
		}
		return null;
	}
	
	public void checkTile(int c, int r, ArrayList<ITile> toBeChecked, HashSet<ITile> toBeRemoved, ITile target) {
		ITile tileToBeChecked = get(c,r);
		if (target.matches(tileToBeChecked)) {
			if (!toBeRemoved.contains(tileToBeChecked)) {
				toBeChecked.add(tileToBeChecked);
			}
		}
	}
	
	public void removeFromBoard(ITile target) {
		Point p = locationOfTile(target);
		int c = p.x;
		int r = p.y;
		_board.get(c).remove(r);  // remove a RealTile
		_board.get(c).add(0,new PlaceHolderTile());  // add a PlaceHolder tile at top of column
	}
	public void dataToString()
	{
		String l;
		String g;
		for(int i=0; i<_board.size(); i++)
		{
			ArrayList<ITile> m = _board.get(i);
			for(int j=0;j<m.size(); j++ ){
				ITile s = m.get(j);
				 
				 if (s.getCharacter()== null || _save == null)
				 {
					 g = "  ";
					 _save = "  ";
				 }
				 else{
					 g = s.getCharacter()+" ";
				 }
				// l = s.getColor().color2char() +" ";
				 l = ColorUtility.color2char(s.getColor())+" ";
					_save += l+g;
			}
		
		}
		System.out.println(_save);
	
		
	}
	public String abc(){
		return _save;
	}
	public void restore (String s){
		
	}
}