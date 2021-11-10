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

    public boolean checkSquareInArray(ArrayList<MazeSquare> array, MazeSquare square) {
        for (MazeSquare arraySquare : array) {
            if (square == arraySquare) {
                return true;
            } 
        }
        return false;
    }

    /**
     * This is the searchMethod
     *
     */
	public void searchMethod(MazeSquare current, int count)
	{
        System.out.println("new stack " + count);
        System.out.println("NewSquare: " + current.toString());
        //acts as a flag
        boolean targetFound = false;

        //adds current MazeSquare to currentPath
		currentPath.add(current);
		current.setHighlight(true);
        

        //when we find a path-to/reached endPoint
		if (current.target == true) {
			if (count < shortestCount) {    //checks if this path is the shortest path, if yes then update shortestPath
				shortestCount = count;
				shortestPath = (ArrayList<MazeSquare>) currentPath.clone();
                System.out.println("new shortest path: " + count);
			}
            targetFound = true;
		}

        //don't recurse once target is found or count has exceeded shortest count
        //run, target isnt found and count is smaller than shortestcount
        if (!targetFound && (count < shortestCount)) {
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case(0): //left
                        //check for walls
                        if (!current.getWall(0)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation() - 1, current.getYLocation());
                            //check if next square is fully traverse or is previous square, if not fully traversed or not on currentPath squares
                            /*
                            move left only when 
                            left square isn't on currentPath (previous square) (method returns true if on currentPath)
                                and when left square hasn't been fully explored
                            */
                            if (!(checkSquareInArray(currentPath, temp))) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    case(1): //top
                        if (!current.getWall(2)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() - 1);
                            if (!(checkSquareInArray(currentPath, temp))) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    case(2): //right
                        if (!current.getWall(1)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation() + 1, current.getYLocation());
                            if (!(checkSquareInArray(currentPath, temp))) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    case(3): //bottom
                        if (!current.getWall(3)) {
                            MazeSquare temp = (MazeSquare) board.getSquareAt(current.getXLocation(), current.getYLocation() + 1);
                            if (!(checkSquareInArray(currentPath, temp))) {
                                searchMethod(temp, count + 1);
                            }
                        }
                        break;
                    default:
                        break;	
                }
            }
        }
        
        if (!targetFound) {
            //beenToSquare.add(current); //square completely visited
            System.out.println("nowhere to go");
        }

        current.setHighlight(false);
		removeSquareFromArray(currentPath, current);

        System.out.println("exit stack");
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
        System.out.println("");
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
