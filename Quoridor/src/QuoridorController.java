/**
 * The controller portion of the MVC implementation.
 * Created by Mitchell Biewen, Isaac Garfinkle, and Alex Walker on 2/29/16.
 */

public class QuoridorController {
    QuoridorModel model;
    QuoridorGui view;

    /**
     * Instantiates references to the model and view.
     * @param view
     */
    public QuoridorController(QuoridorGui view) {
        this.view = view;
        this.model = new QuoridorModel(this, view);
    }

    /**
     * Check to see if Player 1 has indicated intention to move pawn
     */
    public void playerOnePawnClicked() {
        if (model.gameIsOver()) {
            return;
        }

        if (model.isPlayerOneTurn() && !model.pawnOneIsClicked()) {
            model.clickPawnOne();
            view.board.playerOne.changeColor("lightgrey");
            view.setCursorToNormal();
            resetWalls();
        }
        else {
            resetTurn();
        }
    }

    /**
     * Check to see if Player 2 has indicated intention to move pawn
     */
    public void playerTwoPawnClicked() {
        if (model.gameIsOver()) {
            return;
        }

        if (!model.isPlayerOneTurn() && !model.pawnTwoIsClicked()) {
            model.clickPawnTwo();
            view.board.playerTwo.changeColor("lightgrey");
            view.setCursorToNormal();
            resetWalls();
        }
        else {
            resetTurn();
        }
    }

    /**
     * Moves player's pawn to clicked cell if move is legal.
     * @param cell
     */
    public void emptyCellClicked(QuoridorBoard.Cell cell) {
        if (model.gameIsOver()) {
            return;
        }

        int xCoord = convertFromGridPaneCoord(cell.getxCoordinate());
        int yCoord = convertFromGridPaneCoord(cell.getyCoordinate());

        if (model.pawnOneIsClicked()) {

            if (model.pawnCanMoveTo(xCoord, yCoord)) {
                view.board.playerOne.setXCoord(cell.getxCoordinate());
                view.board.playerOne.setYCoord(cell.getyCoordinate());
                model.updatePawnCoords(xCoord, yCoord);
                model.endTurn();
            }

            view.board.boardPane.setRowIndex(view.board.playerOne.getGraphicsNode(), view.board.playerOne.getYCoord());
            view.board.boardPane.setColumnIndex(view.board.playerOne.getGraphicsNode(), view.board.playerOne.getXCoord());

        } else if (model.pawnTwoIsClicked()) {

            if (model.pawnCanMoveTo(xCoord, yCoord)) {
                view.board.playerTwo.setXCoord(cell.getxCoordinate());
                view.board.playerTwo.setYCoord(cell.getyCoordinate());
                model.updatePawnCoords(xCoord, yCoord);
                model.endTurn();
            }

            view.board.boardPane.setRowIndex(view.board.playerTwo.getGraphicsNode(), view.board.playerTwo.getYCoord());
            view.board.boardPane.setColumnIndex(view.board.playerTwo.getGraphicsNode(), view.board.playerTwo.getXCoord());
        }
        resetTurn();
    }

    /**
     * Check to see if Player 1 has indicated intention to place horizontal wall.
     */
    public void playerOneHorizontalWallButtonOnClick() {
        resetPawns();
        if (model.gameIsOver()) {
            return;
        }

        if (model.isPlayerOneTurn() && !view.getHorizontalWallWasClicked() && model.getPlayerOneWallCount() > 0) {
            view.setHorizontalWallWasClickedToTrue();
            view.setVerticalWallWasClickedToFalse();
            view.gameScene.setCursor(view.getHorizontalWallCursor());
        } else {
            resetTurn();
        }
    }

    /**
     * Check to see if Player 2 has indicated intention to place horizontal wall.
     */
    public void playerTwoHorizontalWallButtonOnClick() {
        resetPawns();
        if (model.gameIsOver()) {
            return;
        }

        if (!model.isPlayerOneTurn() && !view.getHorizontalWallWasClicked() && model.getPlayerTwoWallCount() > 0) {
            view.setHorizontalWallWasClickedToTrue();
            view.setVerticalWallWasClickedToFalse();
            view.board.playerTwo.changeColorToStart();
            view.setCursorToHorizontalWall();
        } else {
            resetTurn();
        }
    }

    /**
     * Check to see if Player 1 has indicated intention to place vertical wall.
     */
    public void playerOneVerticalWallButtonOnClick() {
        resetPawns();
        if (model.gameIsOver()) {
            return;
        }

        if (model.isPlayerOneTurn() && !view.getVerticalWallWasClicked() && model.getPlayerOneWallCount() > 0) {
            view.setVerticalWallWasClickedToTrue();
            view.setHorizontalWallWasClickedToFalse();
            view.board.playerOne.changeColorToStart();
            view.setCursorToVerticalWall();
        } else {
            resetTurn();
        }
    }

    /**
     * Check to see if Player 2 has indicated intention to place vertical wall.
     */
    public void playerTwoVerticalWallButtonOnClick() {
        resetPawns();
        if (model.gameIsOver()) {
            return;
        }

        if (!model.isPlayerOneTurn() && !view.getVerticalWallWasClicked() && model.getPlayerTwoWallCount() > 0) {
            view.setVerticalWallWasClickedToTrue();
            view.setHorizontalWallWasClickedToFalse();
            view.board.playerTwo.changeColorToStart();
            view.setCursorToVerticalWall();
        } else {
            resetTurn();
        }
    }

    /**
     * Adds a wall to the boardPane and updates the model and view.
     * @param horizontalWall
     * @param verticalWall
     */
    public void OnClickForPlaceWallCells(QuoridorBoard.Cell horizontalWall, QuoridorBoard.Cell verticalWall) {
        if (view.verticalWallWasClicked && model.canPlaceWall(((verticalWall.getxCoordinate() + 1) / 2),
                ((verticalWall.getyCoordinate() + 3) / 2), true)) {
            view.board.boardPane.add(verticalWall, verticalWall.getxCoordinate(), verticalWall.getyCoordinate(), 1, 3);
            model.placeWall(((verticalWall.getxCoordinate() + 1) / 2), ((verticalWall.getyCoordinate() + 3) / 2), true);
            model.wallPlaced();
            view.verticalWallWasClicked = false;
            model.endTurn();
        }
        else if (view.horizontalWallWasClicked && model.canPlaceWall(((horizontalWall.getxCoordinate() + 3) / 2),
                ((horizontalWall.getyCoordinate() + 1) / 2), false)) {
            view.board.boardPane.add(horizontalWall, horizontalWall.getxCoordinate(), horizontalWall.getyCoordinate(), 3, 1);
            model.placeWall(((horizontalWall.getxCoordinate() + 3) / 2), ((horizontalWall.getyCoordinate() + 1) / 2), false);
            model.wallPlaced();
            view.horizontalWallWasClicked = false;
            model.endTurn();
        }
        resetTurn();
    }

    /**
     * Checks for a winner.
     */
    public void win() {
        if (model.isPlayerOneTurn()) {
            view.playerOneWins();
        }
        else {
            view.playerTwoWins();
        }
    }

    /**
     * Game state reset to the beginning of a turn.
     */
    public void resetTurn() {
        view.setCursorToNormal();
        resetWalls();
        resetPawns();
    }

    /**
     * Indicates that no walls have been clicked.
     */
    public void resetWalls() {
        view.setHorizontalWallWasClickedToFalse();
        view.setVerticalWallWasClickedToFalse();
    }

    /**
     * Indicates that pawns have not been clicked.
     */
    public void resetPawns() {
        model.unclickPawnOne();
        model.unclickPawnTwo();
        view.board.playerOne.changeColorToStart();
        view.board.playerTwo.changeColorToStart();
    }

    /**
     * Changes the gridPane coordinates to the "game coordinates" (used to figure out which cells were clicked).
     * @param gridPaneCoord
     * @return
     */
    public static int convertFromGridPaneCoord(int gridPaneCoord) {
        return gridPaneCoord / 2 + 1;
    }

    /**
     * Accessor method for the model.
     * @return
     */
    public QuoridorModel getModel() {
        return model;
    }

    /**
     * Accessor method for the view.
     * @return
     */
    public QuoridorGui getView() {
        return view;
    }
}
