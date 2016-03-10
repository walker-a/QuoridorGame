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
            view.board.playerOne.changeColor("seagreen");
        }
        else {
            model.unclickPawnOne();
            view.board.playerOne.changeColorToStart();
            model.unclickPawnTwo();
            view.board.playerTwo.changeColorToStart();
        }
    }

    public void playerTwoPawnClicked() {
        if (model.gameIsOver()) {
            return;
        }

        if (!model.isPlayerOneTurn() && !model.pawnTwoIsClicked()) {
            model.clickPawnTwo();
            view.board.playerTwo.changeColor("seagreen");
        }
        else {
            model.unclickPawnOne();
            view.board.playerOne.changeColorToStart();
            model.unclickPawnTwo();
            view.board.playerTwo.changeColorToStart();
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
            else {
                model.unclickPawnOne();
            }

            view.board.boardPane.setRowIndex(view.board.playerOne.getGraphicsNode(), view.board.playerOne.getYCoord());
            view.board.boardPane.setColumnIndex(view.board.playerOne.getGraphicsNode(), view.board.playerOne.getXCoord());
            view.board.playerOne.changeColorToStart();

        } else if (model.pawnTwoIsClicked()) {

            if (model.pawnCanMoveTo(xCoord, yCoord)) {
                view.board.playerTwo.setXCoord(cell.getxCoordinate());
                view.board.playerTwo.setYCoord(cell.getyCoordinate());
                model.updatePawnCoords(xCoord, yCoord);
                model.endTurn();
            }
            else {
                model.unclickPawnTwo();
            }

            view.board.boardPane.setRowIndex(view.board.playerTwo.getGraphicsNode(), view.board.playerTwo.getYCoord());
            view.board.boardPane.setColumnIndex(view.board.playerTwo.getGraphicsNode(), view.board.playerTwo.getXCoord());
            view.board.playerTwo.changeColorToStart();
        }
    }

    public void playerOneHorizontalWallButtonOnClick(){ //Hopefully these will call something in model rather than view.
        if (model.gameIsOver()) {
            return;
        }

        if (model.isPlayerOneTurn()) {
            view.board.setHorizontalWallWasClickedToTrue();
            view.board.setVerticalWallWasClickedToFalse();
            view.board.playerOne.changeColorToStart();
            view.gameScene.setCursor(view.getHorizontalWallCursor());
        }
    }

    public void playerTwoHorizontalWallButtonOnClick() { //Hopefully these will call something in model rather than view.
        if (model.gameIsOver()) {
            return;
        }

        if (!model.isPlayerOneTurn()) {
            view.board.setHorizontalWallWasClickedToTrue();
            view.board.setVerticalWallWasClickedToFalse();
            view.board.playerTwo.changeColorToStart();
            view.setCursorToHorizontalWall();
        }
    }

    public void playerOneVerticalWallButtonOnClick() { //Hopefully these will call something in model rather than view.
        if (model.gameIsOver()) {
            return;
        }

        if (model.isPlayerOneTurn()) {
            view.board.setVerticalWallWasClickedToTrue();
            view.board.setHorizontalWallWasClickedToFalse();
            view.board.playerOne.changeColorToStart();
            view.setCursorToVerticalWall();
        }
    }

    public void playerTwoVerticalWallButtonOnClick() { //Hopefully these will call something in model rather than view.
        if (model.gameIsOver()) {
            return;
        }

        if (!model.isPlayerOneTurn()) {
            view.board.setVerticalWallWasClickedToTrue();
            view.board.setHorizontalWallWasClickedToFalse();
            view.board.playerTwo.changeColorToStart();
            view.setCursorToVerticalWall();
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

    public static int convertFromGridPaneCoord(int gridPaneCoord) {
        return gridPaneCoord / 2 + 1;
    }
}
