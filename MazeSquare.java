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

	private static int shortestCount;	// The shortest path found so far in this search.


	private static MazeSquare currentSquare;    //the current Square at

	private static ArrayList<MazeSquare> shortestPath = new ArrayList<MazeSquare>();
	private static ArrayList<MazeSquare> currentPath = new ArrayList<MazeSquare>();
    private static ArrayList<MazeSquare> beenToSquare = new ArrayList<MazeSquare>();



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

    public boolean beenTo(MazeSquare square) {
        for (MazeSquare s : beenToSquare) {
            if (s == square) {
                return true;
            }
        }
        return false;
    }

    public void removeSquareFromArray(ArrayList<MazeSquare> array, MazeSquare s) {
        for (int i = 0; i < array.size(); i++) {
            if (s == array.get(i)) {
                array.remove(i);
            }
        }
    }

    /**
     * This is the searchMethod
     *
     */
	public void searchMethod(MazeSquare current, int count)
	{
        System.out.println("NewSquare: " + current.toString());
        //acts as a flag
        boolean targetFound = false;

        //adds current MazeSquare to currentPath
		currentPath.add(current);
        beenToSquare.add(current);
		current.setHighlight(true);
        

        //when we find a path-to/reached endPoint
		if (current.target == true) {
			if (count < shortestCount) {    //checks if this path is the shortest path, if yes then update shortestPath
				shortestCount = count;
				shortestPath = (ArrayList<MazeSquare>) currentPath.clone();
			}
            targetFound = true;
		}

        if (!targetFound) {
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case(0): //left
                        if (!current.getWall(0)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation() - 1, current.getYLocation());
                            if (!beenTo(temp)) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    case(1): //right
                        if (!current.getWall(1)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation() + 1, current.getYLocation());
                            if (!beenTo(temp)) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    case(2): //top
                        if (!current.getWall(2)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() - 1);
                            if (!beenTo(temp)) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    case(3): //bottom
                        if (!current.getWall(3)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() + 1);
                            if (!beenTo(temp)) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    default:
                        break;	
                }
            }
        }

        current.setHighlight(false);
		removeSquareFromArray(currentPath, current);
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
	}
    
    /**
	 * A method that is invoked when a user clicks on this square.
	 * This defines the start point for the search. 
	 * Runs the searchMethod
	 */	
	public void rightClicked()
	{
        System.out.println("rightClicked");
		MazeSquare.shortestCount = 1000; //sets shortestCount to illogically high number

        System.out.println("starting search");
		searchMethod(this, 0);
        System.out.println("finished search");

		for (MazeSquare s : shortestPath) {
			s.setHighlight(true);
		}

		System.out.println(" *** COMPLETE: SHORTEST ROUTE " + (MazeSquare.shortestCount == 1000 ? "IMPOSSIBLE" : MazeSquare.shortestCount) + " ***");
	}

    public void resetTarget() {
        this.target = false;
    }

    public void resetPath(ArrayList<MazeSquare> array) {
        System.out.println("size " + array.size());
        for (int i = 0; i < array.size(); i++) {
            System.out.println("removing: " + array.get(i).toString());
            array.remove(i);
        }
        System.out.println("size " + array.size());
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
        System.out.println("size: " + shortestPath.size());
        beenToSquare.clear();
        System.out.println("size: " + beenToSquare.size());


        System.out.println("finished reset");
	}
}
