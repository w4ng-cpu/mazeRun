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
    /**
     * board, a reference to the GameBoard this square is part of.
     */
	private GameBoard board;

    /** 
     * target, true if this square is the target of the search else false.
     */
	private boolean target;

    /**
     * count, keeps track how many recursion levels it took to reach this square
     */
    private int count;                  // Keeps track how many recursion levels it took to reach this square

	private static int shortestCount;	// The shortest path found so far in this search.

    // Keeps track of MazeSquares of paths and visisted
	private static ArrayList<MazeSquare> shortestPath = new ArrayList<MazeSquare>();
	private static ArrayList<MazeSquare> currentPath = new ArrayList<MazeSquare>();
    private static ArrayList<MazeSquare> visitedSquare = new ArrayList<MazeSquare>();

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

    /**
     * Overriding Object method
     * Returns the MazeSquare position in String
     * For debugging
     * @return MazeSuqare location
     */
    public String toString() {
        String temp = "X:" + getXLocation() + " Y:" + getYLocation();
        return temp;
    }

    /**
     * Removes a specific square object reference from array
     * Used to remove current square from currentPath
     * @param array the array selected
     * @param square the square selected
     */
    public void removeSquareFromArray(ArrayList<MazeSquare> array, MazeSquare square) {
        for (int i = 0; i < array.size(); i++) {
            if (square == array.get(i)) {
                array.remove(i);
            }
        }
    }

    /**
     * Checks for a specific object reference in array
     * Used to check if (next) square has been visited or not
     * @param array the array selected
     * @param square the square selected
     * @return boolean if square is in Array or not
     */
    public boolean checkSquareInArray(ArrayList<MazeSquare> array, MazeSquare square) {
        for (MazeSquare arraySquare : array) {
            if (square == arraySquare) {
                return true;
            } 
        }
        return false;
    }

    /**
     * Copys one arraylist's object references into another
     * Used to copy currentPath into shortestPath when new shortest path is found
     * @param copyFrom the arraylist we copying from
     * @param copyTo the arraylist we copying into
     */
    public void copyArray(ArrayList<MazeSquare> copyFrom, ArrayList<MazeSquare> copyTo) {
        copyTo.clear();
        for (MazeSquare s : copyFrom) {
            copyTo.add(s);
        }
    }

    /**
     * Get an adjacent square 
     * @param i represents the adajcent square you want to select, 0:top; 1:right; 2:bottom; 3:left;
     * @param current uses current square to find next square
     * @return the adjacent square
     */
    public MazeSquare getAdjacentSquare(int i, MazeSquare current) {
        MazeSquare temp;
        switch (i) {
            case(0): //top
                temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() - 1);
                return temp;

            case(1): //right
                temp = (MazeSquare) board.getSquareAt(current.getXLocation() + 1, current.getYLocation());
                return temp;

            case(2): //bottom
                temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() + 1);
                return temp;

            case(3): //left
                temp = (MazeSquare) board.getSquareAt(current.getXLocation() - 1, current.getYLocation());
                return temp;

            default: //default returns true, wall exists
                return current;
        }
    }

    /**
     * Checks walls of current square
     * @param i represent the wall you want to check, 0:top; 1:right; 2:bottom; 3:left;
     * @param current the current square being checked
     * @return boolean if wall if there or not
     */
    public boolean checkForWall(int i, MazeSquare current) {
        switch (i) {
            case(0): //top
                return current.getWall(2);

            case(1): //right
                return current.getWall(1);

            case(2): //bottom
                return current.getWall(3);

            case(3): //left
                return current.getWall(0);

            default: //default returns true, wall exists
                return true;
        }
    }

    /**
     * This is the recursive searchMethod using depth first search
     * @param current the current square we enter into
     * @param count the current layer of recursion we are at (starts from 0)
     */
	public void searchMethod(MazeSquare current, int count)
	{
        //print out count/recursion level and the current square we are at
        //System.out.println("new stack: " + count);
        //System.out.println("current square: " + current.toString());

        //adds current square to currentpath and adds weight (the layer of recursion at) to current
        currentPath.add(current);
        current.count = count;

        //if we reach goal/end point
		if (current.target == true) {
			if (count < shortestCount) {    //checks if this path is shorter than shortest path, if yes then update shortestPath
				shortestCount = count;
				copyArray(currentPath, shortestPath);
                //System.out.println("new shortest path: " + count);
                //System.out.println("");
			}
            //remove current from currentPath as we are backtracking/terminating this layer
            removeSquareFromArray(currentPath, current);
            return;
		}

        //if we don't reach goal/end point continue procedurally
        //adds current MazeSquare to visitedSquare arrayList and highlight current square
		visitedSquare.add(current);
		current.setHighlight(true);

        //run, if current count is less than shortestCount - 1 (so there is a reason to recurse again)
        if (count < (shortestCount - 1)) {
            for (int i = 0; i < 4; i++) {
                //checks top, right, bottom then left
                //run if there's no wall
                if (!checkForWall(i, current)) {
                    //get the next square
                    MazeSquare nextSquare = getAdjacentSquare(i, current);
                    //run if nextSquare hasn't been visited
                    if (!(checkSquareInArray(visitedSquare, nextSquare)) || (count < (nextSquare.count - 1))) {
                        searchMethod(nextSquare, count + 1);
                    }
                }
            }
        }
        
        //run, once square adajcent neightbours have been checked/explored
        //System.out.println("square ceompletely visited: " + toString());

        current.setHighlight(false);

		removeSquareFromArray(currentPath, current);

        //System.out.println("exiting stack: " + count);
	}


	/**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the end point/target for the search.
	 * Resets the board highlights, resets the paths, resets the target
	 */	
    public void leftClicked()
	{
        //System.out.println("leftClicked");
        //resets all highlights, target and paths
        reset(0);
        reset(1);
        reset(2);
		this.target = true;
        //System.out.println("endPoint: " + toString());
        
	}
    
    /**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the start point for the search. 
     * Resets paths, highlights and shortestCount
	 * Runs the searchMethod recursion
     * Once recursion finishes, hightlight shortestPath as long as shortestCount isn't 0
     * Print results
	 */	
	public void rightClicked()
	{
        //left click must have set target
        //System.out.println("");
        //System.out.println("rightClicked");
        reset(0);
        reset(2);
		MazeSquare.shortestCount = 1000; //sets shortestCount to illogically high number

        //System.out.println("starting search");
		searchMethod(this, 0);
        //System.out.println("finished search");

        if (MazeSquare.shortestCount != 0) {
            for (MazeSquare s : shortestPath) {
                s.setHighlight(true);
            }
        }

		System.out.println(" *** COMPLETE: SHORTEST ROUTE " + (MazeSquare.shortestCount == 1000 || MazeSquare.shortestCount == 0  ? "IMPOSSIBLE" : MazeSquare.shortestCount) + " ***");
	}

	/**
	 * A method that is invoked when a reset(int n) method is called on GameBoard.
	 * n:0 resets all highlights of squares
     * n:1 resets target square
     * n:2 reset shortestPath and visitedSquare arraylists
	 * @param n value determines type of reset
	 */
	public void reset(int n)
	{
        switch(n) {
            case(0):
                //case 0 resets highlights of all squares
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        board.getSquareAt(i, j).setHighlight(false);
                    }
                }
                break;
            case(1):
                //case 1 resets target
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        MazeSquare temp = (MazeSquare) board.getSquareAt(i, j);
                        temp.target = false;
                    }
                }
                break;
            case(2):
                //case 2 reset shortestPath and visitedSquare arraylists
                shortestPath.clear();
                visitedSquare.clear();
                break;
            default:
                //invalid n
                break;
        }
	}
}
