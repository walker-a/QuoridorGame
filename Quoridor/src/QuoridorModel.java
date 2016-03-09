/**
 * Created by walkera2 on 3/8/16.
 */
public class QuoridorModel {
    private PawnCoordinate playerOnePawn;
    private PawnCoordinate playerTwoPawn;
    private boolean[][][] wallsOnBoard;
    private int playerOneWallCount;
    private int playerTwoWallCount;
    private boolean playerOneTurn;
    private boolean pawnOneClicked;
    private boolean pawnTwoClicked;

    public QuoridorModel() {
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
                if (yCoord == 9) {
                    wallsOnBoard[xCoord][yCoord][2]=true;
                }
            }
        }

        playerOneWallCount = 10;
        playerTwoWallCount = 10;
        playerOnePawn = new PawnCoordinate(5, 9);
        playerTwoPawn = new PawnCoordinate(5, 1);
        playerOneTurn = true;
    }

    public void endTurn() {
        if (playerOneTurn) {
            playerOneTurn = false;
        } else {
            playerOneTurn = true;
        }
        unclickPawnOne();
        unclickPawnTwo();
    }

    public boolean canPlaceWall(int xCoord, int yCoord, boolean vertical) {
        System.out.println("xCoord: " + xCoord + " yCoord: " + yCoord);
        int direction;
        if (vertical) {
            direction = 1;
        }
        else {
            direction = 2;
        }

        boolean canPlaceFirstHalf =  wallsOnBoard[xCoord-1][yCoord-1][direction];

        boolean canPlaceSecondHalf;
        if (vertical) {
            canPlaceSecondHalf = wallsOnBoard[xCoord-1][yCoord][direction];
        }
        else {
            canPlaceSecondHalf = wallsOnBoard[xCoord][yCoord-1][direction];
        }

        if (canPlaceFirstHalf != canPlaceSecondHalf) {
            return false;
        }
        return (!(canPlaceFirstHalf && canPlaceSecondHalf));
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

    public boolean pawnCanMoveTo(int xCoord, int yCoord) {
        PawnCoordinate pawnCoord;
        if (playerOneTurn) {
            pawnCoord = playerOnePawn;
        } else {
            pawnCoord = playerTwoPawn;
        }
        if (pawnCoord.isAdjacent(xCoord, yCoord) && !checkIfWallBetweenTiles(pawnCoord.getXCoord(), pawnCoord.getYCoord(), xCoord, yCoord)) {
            return true;
        }
        return false;
    }

    public boolean checkIfWallBetweenTiles(int xInit, int yInit, int xEnd, int yEnd) {
        if (xInit - xEnd == 1) {
            return wallsOnBoard[xEnd-1][yEnd-1][1];
        } else if (xInit - xEnd == -1) {
            return wallsOnBoard[xInit-1][yInit-1][1];
        } else if (yInit - yEnd == 1) {
            return wallsOnBoard[xInit-1][yInit-1][2];
        } else if (yInit - yEnd == -1) {
            return wallsOnBoard[xEnd-1][yEnd-1][2];
        }
        return true;
    }

    public void updatePawnCoords(int xCoord, int yCoord) {
        if (playerOneTurn) {
            playerOnePawn.updateCoords(xCoord, yCoord);
        } else {
            playerTwoPawn.updateCoords(xCoord, yCoord);
        }
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
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

    public class PawnCoordinate {
        int xCoord;
        int yCoord;

        public PawnCoordinate(int xCoordinate, int yCoordinate) {
            xCoord = xCoordinate;
            yCoord = yCoordinate;
        }

        public void updateCoords(int xCoordinate, int yCoordinate) {
            xCoord = xCoordinate;
            yCoord = yCoordinate;
        }

        public boolean isAdjacent(int xCoordinate, int yCoordinate) {
            if ((Math.abs(xCoordinate - xCoord) == 1 && yCoordinate == yCoord) || (xCoordinate == xCoord && Math.abs(yCoordinate - yCoord) == 1)) {
                return true;
            }
            return false;
        }

        public int getXCoord() {
            return xCoord;
        }

        public int getYCoord() {
            return yCoord;
        }
    }
}
