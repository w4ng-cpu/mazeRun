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

    private boolean visited;
    private int count;

	private static int shortestCount;	// The shortest path found so far in this search.


	private static MazeSquare currentSquare;    //the current Square at

	private static ArrayList<MazeSquare> shortestPath = new ArrayList<MazeSquare>();
	private static ArrayList<MazeSquare> currentPath = new ArrayList<MazeSquare>();
    private static ArrayList<MazeSquare> visitedSquare = new ArrayList<MazeSquare>();
    private static ArrayList<MazeSquare> finalPath = new ArrayList<MazeSquare>();



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
     */
    public String toString() {
        String temp = "X:" + getXLocation() + " Y:" + getYLocation();
        return temp;
    }

    public void removeSquareFromArray(ArrayList<MazeSquare> array, MazeSquare s) {
        for (int i = 0; i < array.size(); i++) {
            if (s == array.get(i)) {
                array.remove(i);
            }
        }
    }

    public boolean checkSquareInArray(ArrayList<MazeSquare> array, MazeSquare square) {
        for (MazeSquare arraySquare : array) {
            if (square == arraySquare) {
                return true;
            } 
        }
        return false;
    }

    public void copyArray(ArrayList<MazeSquare> copyFrom, ArrayList<MazeSquare> copyTo) {
        copyTo.clear();
        for (MazeSquare s : copyFrom) {
            copyTo.add(s);
        }
    }

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
     * This is the searchMethod using depth first search
     *
     */
	public void searchMethod(MazeSquare current, int count)
	{
        //print out count/recursion level and the current square we are at
        System.out.println("new stack: " + count);
        System.out.println("current square: " + current.toString());

        //adds current square to currentpath and adds weight (the layer of recursion at) to current
        currentPath.add(current);
        current.count = count;

        //if we reach goal/end point
		if (current.target == true) {
			if (count < shortestCount) {    //checks if this path is shorter than shortest path, if yes then update shortestPath
				shortestCount = count;
				copyArray(currentPath, shortestPath);
                System.out.println("new shortest path: " + count);
                System.out.println("");
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
        System.out.println("square ceompletely visited: " + toString());

        current.setHighlight(false);

		removeSquareFromArray(currentPath, current);

        System.out.println("exiting stack: " + count);
	}


	/**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the end point for the search.
	 * resets the board
	 */	
    public void leftClicked()
	{
        System.out.println("leftClicked");
        reset(1);
		this.target = true;
        System.out.println("endPoint: " + toString());
        
	}
    
    /**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the start point for the search. 
	 * Runs the searchMethod
	 */	
	public void rightClicked()
	{
        //left click must have set target
        System.out.println("");
        System.out.println("rightClicked");
		MazeSquare.shortestCount = 1000; //sets shortestCount to illogically high number

        System.out.println("starting search");
		searchMethod(this, 0);
        System.out.println("finished search");

		for (MazeSquare s : shortestPath) {
			s.setHighlight(true);
		}

		System.out.println(" *** COMPLETE: SHORTEST ROUTE " + (MazeSquare.shortestCount == 1000 || MazeSquare.shortestCount == 0  ? "IMPOSSIBLE" : MazeSquare.shortestCount) + " ***");
	}

    public void resetTarget() {
        this.target = false;
    }

	/**
	 * A method that is invoked when a reset() method is called on GameBoard.
	 * 
	 * @param n An unspecified value that matches that provided in the call to GameBoard reset()
	 */
	public void reset(int n)
	{
        System.out.println("starting reset");
        //reset hightlighted squares and reset targetsquare
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				board.getSquareAt(i, j).setHighlight(false);

                MazeSquare temp = (MazeSquare) board.getSquareAt(i, j);
                temp.resetTarget();
			}
		}
        //reset shortestPath and beenToSquare
        shortestPath.clear();
        visitedSquare.clear();

        System.out.println("finished reset");
	}
}
