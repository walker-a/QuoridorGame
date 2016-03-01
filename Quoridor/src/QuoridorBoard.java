import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;

/**
 * Created by garfinklei on 2/28/16.
 */
public class QuoridorBoard {
    GridPane boardPane;
    Pawn playerOne;
    Pawn playerTwo;
    Pawn wasJustClicked;
    int xCoordinateForMove = -1;
    int yCoordinateForMove = -1;

    public QuoridorBoard() {
        boardPane = new GridPane();
        boardPane.setHgap(6);
        boardPane.setVgap(6);

        playerOne = new Pawn(0, 4, 35, "lightgrey");
        playerTwo = new Pawn(8, 4, 35, "darkgrey");

        setUpBoard();
        }

    public void setUpBoard() {
        addEmptyCells();
        addPawns();
    }

    public void addEmptyCells() {
        int width = playerOne.getPawnRadius() * 2 + playerOne.getPawnRadius() / 4;
        int height = playerOne.getPawnRadius() * 2 + playerOne.getPawnRadius() / 4;
        for  (int i = 0; i <= 8; i++) {
            for (int j = 0; j <= 8; j++) {
                Cell cell = new Cell(i,j,width, height, Paint.valueOf("tan"));
                cell.setOnMouseClicked(event -> {
                    if (wasJustClicked != null) {
                        wasJustClicked.setXCoord(cell.getxCoordinate());
                        wasJustClicked.setYCoord(cell.getyCoordinate());
                        boardPane.setRowIndex(wasJustClicked.getGraphicsNode(), wasJustClicked.getYCoord());
                        boardPane.setColumnIndex(wasJustClicked.getGraphicsNode(), wasJustClicked.getXCoord());
                        System.out.println("cell clicked");
                        wasJustClicked = null;
                    }
                });
                boardPane.add(cell, i, j);

            }
        }
    }

    public void addPawns() {
        boardPane.add(playerOne.getGraphicsNode(), playerOne.getXCoord(), playerOne.getYCoord());
        boardPane.add(playerTwo.getGraphicsNode(), playerTwo.getXCoord(), playerTwo.getYCoord());
        playerOne.getGraphicsNode().setOnMouseClicked(event -> {
            wasJustClicked = playerOne;
        });
        playerTwo.getGraphicsNode().setOnMouseClicked(event -> {
            wasJustClicked = playerTwo;
        });
        boardPane.setHalignment(playerOne.getGraphicsNode(), HPos.CENTER);
        boardPane.setHalignment(playerTwo.getGraphicsNode(), HPos.CENTER);
    }

    public GridPane getBoardPane() {
        return boardPane;
    }

    public class Cell extends Rectangle{
        public final int xCoordinate;
        public final int yCoordinate;

        public Cell(int xInput, int yInput, double width, double height, Paint color) {
            super(width, height, color);
            xCoordinate = xInput;
            yCoordinate = yInput;

        }

        public int getxCoordinate() {
            return xCoordinate;
        }

        public int getyCoordinate() {
            return yCoordinate;
        }

    }

}
