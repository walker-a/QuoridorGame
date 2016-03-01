import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;

/**
 * The board portion of the game view. Mostly consists of a GridPane object
 * that is modified based on the state of the game
 * Created by Isaac Garfinkle, Mitchell Biewen, and Alex Walker on 2/28/16.
 */
public class QuoridorBoard {
    GridPane boardPane;
    Pawn playerOne;
    Pawn playerTwo;
    Pawn wasJustClicked;
    Boolean horizontalWallWasClicked;
    Boolean verticalWallWasClicked;

    /**
     * Instantiates the board as a new GridPane object with knowledge of the two pawns on the board
     */
    public QuoridorBoard() {
        boardPane = new GridPane();
        boardPane.setHgap(0);
        boardPane.setVgap(0);

        playerOne = new Pawn(8, 0, 30, "lightgrey");
        playerTwo = new Pawn(8, 16, 30, "slategrey");

        horizontalWallWasClicked = false;
        verticalWallWasClicked = false;

        setUpBoard();
        }

    /**
     * draws the tiles of the board and then draws the pawns
     */
    public void setUpBoard() {
        addEmptyCells();
        addPawns();
    }

    /**
     * adds empty game tiles to the board GridPane and sets their reactions on click
     */
    public void addEmptyCells() {
        int tileSize = (int) (playerOne.getPawnRadius() * 2.25);
        int wallWidth = (int) (playerOne.getPawnRadius() * 0.5);
        for  (int i = 0; i <= 16; i++) {
            for (int j = 0; j <= 16; j++) {
                if (i%2==0 && j%2==0) {
                    Cell cell = new Cell(i, j, tileSize, tileSize, Paint.valueOf("tan"));
                    cell.setOnMouseClicked(event -> {
                        if (wasJustClicked != null) {
                            wasJustClicked.setXCoord(cell.getxCoordinate());
                            wasJustClicked.setYCoord(cell.getyCoordinate());
                            boardPane.setRowIndex(wasJustClicked.getGraphicsNode(), wasJustClicked.getYCoord());
                            boardPane.setColumnIndex(wasJustClicked.getGraphicsNode(), wasJustClicked.getXCoord());
                            System.out.println("cell clicked");
                            wasJustClicked.changeColorToStart();
                            wasJustClicked = null;
                        }
                    });
                    boardPane.add(cell, i, j);
                }

                if (i%2==1 && j%2==1) {
                    Cell cell = new Cell(i, j, wallWidth, wallWidth, Paint.valueOf("white"));
                    Cell horizontalWall = new Cell(i - 1, j, tileSize * 2 + wallWidth, wallWidth, Paint.valueOf("darkgrey"));
                    Cell verticalWall = new Cell(i, j - 1, wallWidth, tileSize * 2 + wallWidth, Paint.valueOf("darkgrey"));;
                    boardPane.add(cell, i ,j);
                    cell.setOnMouseClicked(event -> {
                        if (horizontalWallWasClicked) {
                            boardPane.add(horizontalWall, horizontalWall.getxCoordinate(), horizontalWall.getyCoordinate(), 3, 1);
                            horizontalWallWasClicked = false;
                        }
                        if (verticalWallWasClicked) {
                            boardPane.add(verticalWall, verticalWall.getxCoordinate(), verticalWall.getyCoordinate(), 1, 3);
                            verticalWallWasClicked = false;
                        }
                    });
                }

                if (i%2==0 && j%2==1){
                    Cell cell = new Cell(i, j, tileSize, wallWidth, Paint.valueOf("white"));
                    boardPane.add(cell, i ,j);
                }

                if (i%2==1 && j%2==0){
                    Cell cell = new Cell(i, j, wallWidth, tileSize, Paint.valueOf("white"));
                    boardPane.add(cell, i ,j);
                }

            }
        }
    }

    /**
     * Adds the pawns to the board GridPane and sets their actions on click
     */
    public void addPawns() {
        boardPane.add(playerOne.getGraphicsNode(), playerOne.getXCoord(), playerOne.getYCoord());
        boardPane.add(playerTwo.getGraphicsNode(), playerTwo.getXCoord(), playerTwo.getYCoord());
        playerOne.getGraphicsNode().setOnMouseClicked(event -> {
            wasJustClicked = playerOne;
            playerOne.changeColorToYellow();
        });
        playerTwo.getGraphicsNode().setOnMouseClicked(event -> {
            wasJustClicked = playerTwo;
            playerTwo.changeColorToYellow();
        });
        boardPane.setHalignment(playerOne.getGraphicsNode(), HPos.CENTER);
        boardPane.setHalignment(playerTwo.getGraphicsNode(), HPos.CENTER);
    }

    /**
     * returns the GridPane object associated with the game board
     * @return GridPane
     */
    public GridPane getBoardPane() {
        return boardPane;
    }

    /**
     * Sets the state that the next click of mouse triggers a new horizontal wall
     */
    public void setHorizontalWallWasClicked() {
        horizontalWallWasClicked = true;
    }

    /**
     * Sets the state that the next click of mouse triggers a new vertical wall
     */
    public void setVerticalWallWasClicked() {
        verticalWallWasClicked = true;
    }

    /**
     * Cell class that is a rectangle object, but knows its position on the board
     */
    public class Cell extends Rectangle {
        public final int xCoordinate;
        public final int yCoordinate;

        /**
         * instantiates cell object with these parameters:
         * @param xInput
         * @param yInput
         * @param width
         * @param height
         * @param color
         */
        public Cell(int xInput, int yInput, double width, double height, Paint color) {
            super(width, height, color);
            xCoordinate = xInput;
            yCoordinate = yInput;

        }

        /**
         * gets the x coordinate of the cell on board
         * @return xCoordinate
         */
        public int getxCoordinate() {
            return xCoordinate;
        }

        /**
         * gets the y coordinate of the cell on board
         * @return yCoordinate
         */
        public int getyCoordinate() {
            return yCoordinate;
        }

    }

}