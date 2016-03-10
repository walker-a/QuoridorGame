/**
 * Created by walkera2 on 3/8/16.
 */
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

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

    public void endTurn() {
        if (playerOneTurn) {
            playerOneTurn = false;
        } else {
            playerOneTurn = true;
        }
        unclickPawnOne();
        unclickPawnTwo();
        view.updateTurn();
    }

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

    public void placeWall(int xCoord, int yCoord, boolean vertical) {
        if (vertical) {
            placeEastWall(xCoord, yCoord);
            placeEastWall(xCoord, yCoord + 1);
        } else {
            placeSouthWall(xCoord, yCoord);
            placeSouthWall(xCoord + 1, yCoord);
        }
    }

    public void wallPlaced() {
        if (isPlayerOneTurn() && playerOneWallCount > 0) {
            playerOneWallCount--;
            view.updatePlayerOneWallCount(playerOneWallCount);
        } else if (playerTwoWallCount > 0){
            playerTwoWallCount--;
            view.updatePlayerTwoWallCount(playerTwoWallCount);
        }
    }

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
        return playerOneWallCount;
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


    public class Coordinate {
        int xCoord;
        int yCoord;

        public Coordinate(int xCoordinate, int yCoordinate) {
            xCoord = xCoordinate;
            yCoord = yCoordinate;
        }

        public void updateCoords(int xCoordinate, int yCoordinate) {
            xCoord = xCoordinate;
            yCoord = yCoordinate;
        }

        public boolean isAdjacent(Coordinate coordinate) {
            if ((Math.abs(coordinate.getXCoord() - xCoord) == 1 && coordinate.getYCoord() == yCoord) || (coordinate.getXCoord() == xCoord && Math.abs(coordinate.getYCoord() - yCoord) == 1)) {
                return true;
            }
            return false;
        }

        public boolean equals(Coordinate coordinate) {
            return ((coordinate.getXCoord() == xCoord) && (coordinate.getYCoord() == yCoord));
        }

        public int getXCoord() {
            return xCoord;
        }

        public int getYCoord() {
            return yCoord;
        }

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
