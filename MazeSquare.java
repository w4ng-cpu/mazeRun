import java.util.ArrayList;
/**
 * This class, once completed, should provide a recursive depth first solution
 * to finding the SHORTEST route between two squares on a GameBoard.
 * 
 * @see GameBoard
 * @see GameSquare
 * @author Joe Finney
 */
public class MazeSquare extends GameSquare
{
	private GameBoard board;			// A reference to the GameBoard this square is part of.
	private boolean target;				// true if this square is the target of the search.

	private static MazeSquare currentSquare;
	private static MazeSquare endPoint;

	private static ArrayList<MazeSquare> shortestPath = new ArrayList<MazeSquare>();
	private static ArrayList<MazeSquare> currentPath = new ArrayList<MazeSquare>();

	private static int shortestCount;	// The shortest path found so far in this search.

	/**
	 * Create a new GameSquare, which can be placed on a GameBoard.
	 * 
	 * @param x the x co-ordinate of this square on the game board.
	 * @param y the y co-ordinate of this square on the game board.
	 * @param board the GameBoard that this square resides on.
	 */
	public MazeSquare(int x, int y, GameBoard board)
	{
		super(x, y);
		this.board = board;
	}

	public void searchMethod(MazeSquare current, int count)
	{
		shortestPath.add(current);
		current.setHighlight(true);
		if (current == endPoint) {
			if (count < shortestCount) {
				shortestCount = count;
				shortestPath = (ArrayList)currentPath.clone();
			}
		}
		for (int i = 0; i < 4; i++) {
			switch (i) {
				case(0): //left
					if (!current.getWall(0)) {
						MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation() - 1, current.getYLocation());
						if (!temp.isHighlighted()) {
							searchMethod(temp, count + 1);
						}
					}
					break;
				case(1): //right
					if (!current.getWall(1)) {
						MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation() + 1, current.getYLocation());
						if (!temp.isHighlighted()) {
							searchMethod(temp, count + 1);
						}
					}
					break;
				case(2): //top
					if (!current.getWall(2)) {
						MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() - 1);
						if (!temp.isHighlighted()) {
							searchMethod(temp, count + 1);
						}
					}
					break;
				case(3): //bottom
					if (!current.getWall(3)) {
						MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() + 1);
						if (!temp.isHighlighted()) {
							searchMethod(temp, count + 1);
						}
					}
					break;
				default:
					break;	
			}
		}
		currentPath.remove(current);
	}

	/**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the end point for the search.
	 * Allan- Wouldnt this be the starting point?
	 */	
    public void leftClicked()
	{
		this.target = true;
		this.endPoint = this;
	}
    
    /**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the start point for the search. 
	 * Allan- Wouldn't this be the end point
	 */	
	public void rightClicked()
	{
		MazeSquare.shortestCount = 1000;
		searchMethod(this, 0);
		reset(1);
		for (int i = 0; i < shortestPath.size(); i++) {
			shortestPath.get(i).setHighlight(false);
		}
		System.out.println(" *** COMPLETE: SHORTEST ROUTE " + (MazeSquare.shortestCount == 1000 ? "IMPOSSIBLE" : MazeSquare.shortestCount) + " ***");
	}

	/**
	 * A method that is invoked when a reset() method is called on GameBoard.
	 * 
	 * @param n An unspecified value that matches that provided in the call to GameBoard reset()
	 */
	public void reset(int n)
	{
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				board.getSquareAt(i, j).setHighlight(true);
			}
		}
	}
}
