/**
 * Created by Alex Walker, Isaac Garfinkle, and Mitchell Biewen on 3/8/16.
 */
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The model implementation of the MVC design.
 */

public class QuoridorModel {
    private Coordinate playerOnePawn;
    private Coordinate playerTwoPawn;
    private boolean[][][] wallsOnBoard;
    private int playerOneWallCount;
    private int playerTwoWallCount;
    private boolean playerOneTurn;
    private boolean pawnOneClicked;
    private boolean pawnTwoClicked;
    private QuoridorController controller;
    private QuoridorGui view;
    private int playerOneYGoal;
    private int playerTwoYGoal;
    private boolean playerHasWon;

    /**
     * Instantiates possible places for walls and conditions for which to start the game.
     * @param controller
     * @param view
     */
    public QuoridorModel(QuoridorController controller, QuoridorGui view) {
        this.controller = controller;
        this.view = view;

        wallsOnBoard = new boolean[9][9][4];
        for(int xCoord = 0; xCoord<=8; xCoord++){
            for(int yCoord = 0; yCoord<=8; yCoord++){
                for (int direction = 0; direction<=3; direction++){
                    wallsOnBoard[xCoord][yCoord][direction]=false;
                }
                if (xCoord == 0) {
                    wallsOnBoard[xCoord][yCoord][3]=true;
                }
                if (xCoord == 8) {
                    wallsOnBoard[xCoord][yCoord][1]=true;
                }
                if (yCoord == 0) {
                    wallsOnBoard[xCoord][yCoord][0]=true;
                }
                if (yCoord == 8) {
                    wallsOnBoard[xCoord][yCoord][2]=true;
                }
            }
        }

        playerOneWallCount = 10;
        playerTwoWallCount = 10;
        playerOnePawn = new Coordinate(5, 9);
        playerTwoPawn = new Coordinate(5, 1);
        playerOneTurn = true;
        playerOneYGoal = 1;
        playerTwoYGoal = 9;
        playerHasWon = false;
    }

    /**
     * Switches the player whose turn it is.
     */
    public void endTurn() {
        if (playerOneTurn) {
            playerOneTurn = false;
        } else {
            playerOneTurn = true;
        }
        unclickPawnOne();
        unclickPawnTwo();
        view.updateLabels(getPlayerOneWallCount(), getPlayerTwoWallCount());
    }

    /**
     * Determines whether or not a wall can be placed at given coordinates.
     * @param xCoord
     * @param yCoord
     * @param vertical
     * @return
     */
    public boolean canPlaceWall(int xCoord, int yCoord, boolean vertical) {
        Coordinate coordinate = new Coordinate(xCoord,yCoord);

        int direction;
        if (vertical) {
            direction = 1;
        }
        else {
            direction = 2;
        }

        if (playerOneTurn){
            if (playerOneWallCount == 0){
                return false;
            }
        }
        else {
            if (playerTwoWallCount ==0) {
                return false;
            }
        }

        boolean canPlaceFirstHalf =  !isWall(coordinate,direction);

        boolean canPlaceSecondHalf;
        if (vertical) {
            canPlaceSecondHalf = !isWall(getNextCell(coordinate,2),direction);
        }
        else {
            canPlaceSecondHalf = !isWall(getNextCell(coordinate,1),direction);
        }

        if (!(canPlaceFirstHalf && canPlaceSecondHalf)){

            return false;
        }

        placeWall(xCoord,yCoord,vertical);

        if (hasPath(playerOnePawn, playerOneYGoal) && hasPath(playerTwoPawn,playerTwoYGoal)) {
            removeWall(xCoord,yCoord,vertical);
            return true;
        }

        removeWall(xCoord,yCoord,vertical);
        return false;
    }

    /**
     * Determines whether or not a pawn still has a path to the other side given wall placements.
     * @param pawn
     * @param goalRow
     * @return
     */
    public boolean hasPath(Coordinate pawn, int goalRow) {
        Queue<Coordinate> toRead = new LinkedList<Coordinate>();
        Queue<Coordinate> read = new LinkedList<Coordinate>();

        toRead.offer(pawn);
        while(!toRead.isEmpty()){
            Coordinate currentSquare = toRead.poll();
            read.offer(currentSquare);
            for(Coordinate neighbor : currentSquare.neighborList()) {
                if (neighbor.getYCoord()== goalRow) {
                    return true;
                }
                boolean hasSeen = false;
                for (Coordinate readSquare : read){
                    if (readSquare.equals(neighbor)){
                        hasSeen = true;
                    }
                }
                if (!hasSeen) {
                    toRead.offer(neighbor);
                }
            }
        }
        return false;

    }

    /**
     * Updates storage of whether or not a wall exists at given coordinates.
     * @param xCoord
     * @param yCoord
     * @param vertical
     */
    public void placeWall(int xCoord, int yCoord, boolean vertical) {
        if (vertical) {
            placeEastWall(xCoord, yCoord);
            placeEastWall(xCoord, yCoord + 1);
        } else {
            placeSouthWall(xCoord, yCoord);
            placeSouthWall(xCoord + 1, yCoord);
        }
    }

    /**
     * Updates wall count.
     */
    public void wallPlaced() {
        if (isPlayerOneTurn() && playerOneWallCount > 0) {
            playerOneWallCount--;

        } else if (playerTwoWallCount > 0) {
            playerTwoWallCount--;
        }
    }

    /**
     * The following methods add and remove walls on a given side of each cell.
     * @param xCoord
     * @param yCoord
     * @param vertical
     */

    public void removeWall(int xCoord, int yCoord, boolean vertical) {
        if (vertical) {
            removeEastWall(xCoord, yCoord);
            removeEastWall(xCoord, yCoord + 1);
        } else {
            removeSouthWall(xCoord, yCoord);
            removeSouthWall(xCoord + 1, yCoord);
        }
    }

    public void placeNorthWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][0] = true;
        if (yCoordinate>1) {
            wallsOnBoard[xCoordinate - 1][yCoordinate - 2][2] = true;
        }
    }

    public void placeEastWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][1] = true;
        if (xCoordinate<9){
            wallsOnBoard[xCoordinate][yCoordinate - 1][3] = true;
        }
    }

    public void placeSouthWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][2] = true;
        if (yCoordinate<9){
            wallsOnBoard[xCoordinate - 1][yCoordinate][0] = true;
        }
    }

    public void placeWestWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][3] = true;
        if (xCoordinate>1){
            wallsOnBoard[xCoordinate - 2][yCoordinate - 1][2] = true;
        }
    }

    public void removeNorthWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][0] = false;
        if (yCoordinate>1) {
            wallsOnBoard[xCoordinate - 1][yCoordinate - 2][2] = false;
        }
    }

    public void removeEastWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][1] = false;
        if (xCoordinate<9){
            wallsOnBoard[xCoordinate][yCoordinate - 1][3] = false;
        }
    }

    public void removeSouthWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][2] = false;
        if (yCoordinate<9){
            wallsOnBoard[xCoordinate - 1][yCoordinate][0] = false;
        }
    }

    public void removeWestWall(int xCoordinate, int yCoordinate) {
        wallsOnBoard[xCoordinate - 1][yCoordinate - 1][3] = false;
        if (xCoordinate>1){
            wallsOnBoard[xCoordinate - 2][yCoordinate - 1][2] = false;
        }
    }

    /**
     * Determines whether or not a pawn can move to a given cell.
     * @param xCoord
     * @param yCoord
     * @return
     */
    public boolean pawnCanMoveTo(int xCoord, int yCoord) {
        Coordinate destination = new Coordinate(xCoord,yCoord);
        Coordinate pawnCoord;
        Coordinate otherPawnCoord;
        boolean pawnNeighborsIncludeOtherPawn = false;
        ArrayList<Coordinate> potentialDestinations = new ArrayList<Coordinate>();
        if (playerOneTurn) {
            pawnCoord = playerOnePawn;
            otherPawnCoord = playerTwoPawn;
        } else {
            pawnCoord = playerTwoPawn;
            otherPawnCoord = playerOnePawn;
        }

        //Add immediate neighbors so long as you disclude the other pawn
        for (Coordinate neighbor : pawnCoord.neighborList()) {
            if (!neighbor.equals(otherPawnCoord)) {
                potentialDestinations.add(neighbor);
            }
            else {
                pawnNeighborsIncludeOtherPawn = true;
            }
        }


        if (pawnCoord.isAdjacent(otherPawnCoord) && pawnNeighborsIncludeOtherPawn) {
            int direction = directionBetweenTiles(pawnCoord,otherPawnCoord);
            if (!isWall(otherPawnCoord,direction)) { //Add square on other side of other pawn
                potentialDestinations.add(getNextCell(otherPawnCoord,direction));
            }
            else { //Add other neighbors of other pawn if wall prevents jump.
                for(Coordinate neighbor: otherPawnCoord.neighborList()) {
                    if (!neighbor.equals(pawnCoord)) {
                        potentialDestinations.add(neighbor);
                    }
                }
            }
        }

        for(Coordinate potential: potentialDestinations){
            if (potential.equals(destination)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a wall between two adjacent tiles.
     * @param init
     * @param end
     * @return
     */
    public boolean checkIfWallBetweenTiles(Coordinate init, Coordinate end) { //Assumes tiles are adjacent
        if (init.getXCoord() - end.getXCoord() == 1) {
            return wallsOnBoard[end.getXCoord()-1][end.getYCoord()-1][1];
        } else if (init.getXCoord() - end.getXCoord() == -1) {
            return wallsOnBoard[init.getXCoord()-1][init.getYCoord()-1][1];
        } else if (init.getYCoord() - end.getYCoord() == 1) {
            return wallsOnBoard[init.getXCoord()-1][init.getYCoord()-1][2];
        } else if (init.getYCoord() - end.getYCoord() == -1) {
            return wallsOnBoard[end.getXCoord()-1][end.getYCoord()-1][2];
        }
        return true;
    }

    /**
     * Determines which side of the tile has a wall on it.
     * @param init
     * @param end
     * @return
     */
    public int directionBetweenTiles(Coordinate init, Coordinate end) { //Assumes tiles are adjacent
        if (init.getXCoord() - end.getXCoord() == 1) {
            return 3;
        } else if (init.getXCoord() - end.getXCoord() == -1) {
            return 1;
        } else if (init.getYCoord() - end.getYCoord() == 1) {
            return 0;
        } else {
            return 2;
        }
    }

    /**
     * Changes the location of a pawn.
     * @param xCoord
     * @param yCoord
     */
    public void updatePawnCoords(int xCoord, int yCoord) {
        if (playerOneTurn) {
            playerOnePawn.updateCoords(xCoord, yCoord);
            if (playerOnePawn.getYCoord() == playerOneYGoal) {
                controller.win();
                playerHasWon = true;
            }
        } else {
            playerTwoPawn.updateCoords(xCoord, yCoord);
            if (playerTwoPawn.getYCoord() == playerTwoYGoal) {
                controller.win();
                playerHasWon = true;
            }
        }
    }

    /**
     * Returns the tile on the other side of a wall direction.
     * @param coordinate
     * @param direction
     * @return
     */
    public Coordinate getNextCell(Coordinate coordinate, int direction) {
        if (direction == 1) {
            return new Coordinate(coordinate.getXCoord()+1, coordinate.getYCoord());
        }
        else if (direction == 3) {
            return new Coordinate(coordinate.getXCoord()-1, coordinate.getYCoord());
        }
        else if (direction == 0) {
            return new Coordinate(coordinate.getXCoord(), coordinate.getYCoord()-1);
        }
        else{
            return new Coordinate(coordinate.getXCoord(), coordinate.getYCoord()+1);
        }
    }

    /**
     * Determines if a wall is at a given direction of a given coordinate.
     * @param coordinate
     * @param direction
     * @return
     */
    public boolean isWall(Coordinate coordinate, int direction) {
        return wallsOnBoard[coordinate.getXCoord()-1][coordinate.getYCoord()-1][direction];
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public int getPlayerOneWallCount() {
        return playerOneWallCount;
    }

    public int getPlayerTwoWallCount() {
        return playerTwoWallCount;
    }

    public void clickPawnOne() {
        pawnOneClicked = true;
    }

    public void clickPawnTwo() {
        pawnTwoClicked = true;
    }

    public void unclickPawnOne() {
        pawnOneClicked = false;
    }

    public void unclickPawnTwo() {
        pawnTwoClicked = false;
    }

    public boolean pawnOneIsClicked() {
        return pawnOneClicked;
    }

    public boolean pawnTwoIsClicked() {
        return pawnTwoClicked;
    }

    public boolean gameIsOver() {
        return playerHasWon;
    }

    /**
     * A new class to create a coordinate object.
     */
    public class Coordinate {
        int xCoord;
        int yCoord;

        /**
         * Instantiates an instance of a Coordinate.
         * @param xCoordinate
         * @param yCoordinate
         */
        public Coordinate(int xCoordinate, int yCoordinate) {
            xCoord = xCoordinate;
            yCoord = yCoordinate;
        }

        /**
         * Updates where the Coordinate is.
         * @param xCoordinate
         * @param yCoordinate
         */
        public void updateCoords(int xCoordinate, int yCoordinate) {
            xCoord = xCoordinate;
            yCoord = yCoordinate;
        }

        /**
         * Determines if two coordinates are adjacent.
         * @param coordinate
         * @return
         */
        public boolean isAdjacent(Coordinate coordinate) {
            if ((Math.abs(coordinate.getXCoord() - xCoord) == 1 && coordinate.getYCoord() == yCoord) || (coordinate.getXCoord() == xCoord && Math.abs(coordinate.getYCoord() - yCoord) == 1)) {
                return true;
            }
            return false;
        }

        /**
         * Determines if two coordinates are the same.
         * @param coordinate
         * @return
         */
        public boolean equals(Coordinate coordinate) {
            return ((coordinate.getXCoord() == xCoord) && (coordinate.getYCoord() == yCoord));
        }

        public int getXCoord() {
            return xCoord;
        }

        public int getYCoord() {
            return yCoord;
        }

        /**
         * Stores the neighbors of a given coordinate if there is no wall in between.
         * @return
         */
        public ArrayList<Coordinate> neighborList() {
            ArrayList<Coordinate> neighbors = new ArrayList<Coordinate>();
            if (!isWall(this,0)) {
                neighbors.add(new Coordinate(xCoord,yCoord-1));
            }
            if (!isWall(this,1)) {
                neighbors.add(new Coordinate(xCoord+1,yCoord));
            }
            if (!isWall(this,2)) {
                neighbors.add(new Coordinate(xCoord,yCoord+1));
            }
            if (!isWall(this,3)) {
                neighbors.add(new Coordinate(xCoord-1,yCoord));
            }
            return neighbors;
        }

        //print coordinates for debugging purposes
        public void print() {
            System.out.println((xCoord+","+yCoord));
        }
    }
}
