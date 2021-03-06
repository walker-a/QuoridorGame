import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

/**
 * The board portion of the game view. Mostly consists of a GridPane object
 * that is modified based on the state of the game
 * Created by Isaac Garfinkle, Mitchell Biewen, and Alex Walker on 2/28/16.
 */
public class QuoridorBoard {
    QuoridorController controller;
    GridPane boardPane;
    Pawn playerOne;
    Pawn playerTwo;
    Pawn wasJustClicked;
    QuoridorModel model;
    QuoridorGui view;

    /**
     * Instantiates the board as a new GridPane object with knowledge of the two pawns on the board
     */
    public QuoridorBoard(QuoridorController controller, QuoridorModel model, QuoridorGui view) {
        this.controller = controller;
        this.model = model;
        this.view = view;
        boardPane = new GridPane();
        boardPane.setHgap(0);
        boardPane.setVgap(0);

        int pawnRadius = (int) (view.getSceneHeight() / 27);

        playerOne = new Pawn(8, 16, pawnRadius, "slategrey");
        playerTwo = new Pawn(8, 0, pawnRadius, "seashell");

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
                    if (j == 0 || j == 16) {
                        cell.setFill(Paint.valueOf("rosybrown"));
                    }
                    cell.setOnMouseClicked(event -> {
                        controller.emptyCellClicked(cell);
                        if (wasJustClicked != null) {
                            wasJustClicked.setXCoord(cell.getxCoordinate());
                            wasJustClicked.setYCoord(cell.getyCoordinate());
                            boardPane.setRowIndex(wasJustClicked.getGraphicsNode(), wasJustClicked.getYCoord());
                            boardPane.setColumnIndex(wasJustClicked.getGraphicsNode(), wasJustClicked.getXCoord());
                            wasJustClicked.changeColorToStart();
                            wasJustClicked = null;
                        }
                    });
                    boardPane.add(cell, i, j);
                }

                if (i%2==1 && j%2==1) {
                    Cell cell = new Cell(i, j, wallWidth, wallWidth, Paint.valueOf("snow"));
                    Cell horizontalWall = new Cell(i - 1, j, tileSize * 2 + wallWidth, wallWidth, Paint.valueOf("sienna"));
                    horizontalWall.setOnMouseClicked(event -> {controller.resetTurn();});
                    Cell verticalWall = new Cell(i, j - 1, wallWidth, tileSize * 2 + wallWidth, Paint.valueOf("sienna"));;
                    verticalWall.setOnMouseClicked(event -> {controller.resetTurn();});
                    boardPane.add(cell, i ,j);

                    cell.setOnMouseClicked(event -> {
                        controller.OnClickForPlaceWallCells(horizontalWall, verticalWall);
                    });
                }

                if (i%2==0 && j%2==1){
                    Cell cell = new Cell(i, j, tileSize, wallWidth, Paint.valueOf("snow"));
                    cell.setOnMouseClicked(event -> {controller.resetTurn();});
                    boardPane.add(cell, i ,j);
                }

                if (i%2==1 && j%2==0){
                    Cell cell = new Cell(i, j, wallWidth, tileSize, Paint.valueOf("snow"));
                    cell.setOnMouseClicked(event -> {controller.resetTurn();});
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
            controller.playerOnePawnClicked();
        });
        playerOne.getGraphicsNode().setOnMouseEntered(event -> {
            if (model.isPlayerOneTurn()) {
                view.setCursorToNormal();
            }
        });
        playerOne.getGraphicsNode().setOnMouseExited(event -> {
            if (view.getHorizontalWallWasClicked()) {
                view.setCursorToHorizontalWall();
            }
            else if (view.getVerticalWallWasClicked()) {
                view.setCursorToVerticalWall();
            }
        });

        playerTwo.getGraphicsNode().setOnMouseClicked(event -> {
            controller.playerTwoPawnClicked();
        });
        playerTwo.getGraphicsNode().setOnMouseEntered(event -> {
            if (!model.isPlayerOneTurn()) {
                view.setCursorToNormal();
            }
        });
        playerTwo.getGraphicsNode().setOnMouseExited(event -> {
            if (view.getHorizontalWallWasClicked()) {
                view.setCursorToHorizontalWall();
            }
            else if (view.getVerticalWallWasClicked()) {
                view.setCursorToVerticalWall();
            }
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
     * Cell class that is a rectangle object, but knows its position on the board
     */
    public class Cell extends Rectangle {
        private final int xCoordinate;
        private final int yCoordinate;

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
