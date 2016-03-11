/**
 * Created by labuser on 2/29/16.
 */
public class QuoridorController {
    QuoridorModel model;
    QuoridorGui view;


    public QuoridorController(QuoridorGui view, QuoridorModel model) {
        this.view = view;
        this.model = new QuoridorModel(this, view);
        this.view.setSystems(this, this.model);
    }

    public void playerOnePawnClicked() {
        if (model.gameIsOver()) {
            return;
        }

        if (model.isPlayerOneTurn() && !model.pawnOneIsClicked()) {
            model.clickPawnOne();
            view.board.playerOne.changeColor("slateblue");
            view.setCursorToNormal();
            resetWalls();
        }
        else {
            resetTurn();
        }
    }

    public void playerTwoPawnClicked() {
        if (model.gameIsOver()) {
            return;
        }

        if (!model.isPlayerOneTurn() && !model.pawnTwoIsClicked()) {
            model.clickPawnTwo();
            view.board.playerTwo.changeColor("seagreen");
            view.setCursorToNormal();
            resetWalls();
        }
        else {
            resetTurn();
        }
    }

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

    public void playerOneHorizontalWallButtonOnClick(){ //Hopefully these will call something in model rather than view.
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

    public void playerTwoHorizontalWallButtonOnClick() { //Hopefully these will call something in model rather than view.
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

    public void playerOneVerticalWallButtonOnClick() { //Hopefully these will call something in model rather than view.
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

    public void playerTwoVerticalWallButtonOnClick() { //Hopefully these will call something in model rather than view.
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

    public void win() {
        if (model.isPlayerOneTurn()) {
            view.playerOneWins();
        }
        else {
            view.playerTwoWins();
        }
    }

    public void resetTurn() {
        view.setCursorToNormal();
        view.setHorizontalWallWasClickedToFalse();
        view.setVerticalWallWasClickedToFalse();
        model.unclickPawnOne();
        model.unclickPawnTwo();
        view.board.playerOne.changeColorToStart();
        view.board.playerTwo.changeColorToStart();
    }

    public void resetWalls() {
        view.setHorizontalWallWasClickedToFalse();
        view.setVerticalWallWasClickedToFalse();
    }

    public static int convertFromGridPaneCoord(int gridPaneCoord) {
        return gridPaneCoord / 2 + 1;
    }
}
