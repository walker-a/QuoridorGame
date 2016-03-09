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

    public void placeWallClicked() {
    }

    public void rotateWallClicked() {
    }

    public void movePawn() {
    }

    public void placeWall() {
    }

    public void rotateWall() {
    }

    public void playerOnePawnClicked() {
        if (model.isPlayerOneTurn()) {
            model.clickPawnOne();
            view.board.playerOne.changeColorToYellow();
        }
    }

    public void playerTwoPawnClicked() {
        if (!model.isPlayerOneTurn()) {
            model.clickPawnTwo();
            view.board.playerTwo.changeColorToYellow();
        }
    }

    public void emptyCellClicked(QuoridorBoard.Cell cell) {
        int xCoord = convertFromGridPaneCoord(cell.getxCoordinate());
        int yCoord = convertFromGridPaneCoord(cell.getyCoordinate());
        if (model.pawnOneIsClicked()) {

            if (model.pawnCanMoveTo(xCoord, yCoord)) {
                view.board.playerOne.setXCoord(cell.getxCoordinate());
                view.board.playerOne.setYCoord(cell.getyCoordinate());
                model.updatePawnCoords(xCoord, yCoord);
                model.endTurn();

                view.board.boardPane.setRowIndex(view.board.playerOne.getGraphicsNode(), view.board.playerOne.getYCoord());
                view.board.boardPane.setColumnIndex(view.board.playerOne.getGraphicsNode(), view.board.playerOne.getXCoord());
                view.board.playerOne.changeColorToStart();
            }

        } else if (model.pawnTwoIsClicked()) {

            if (model.pawnCanMoveTo(xCoord, yCoord)) {
                view.board.playerTwo.setXCoord(cell.getxCoordinate());
                view.board.playerTwo.setYCoord(cell.getyCoordinate());
                model.updatePawnCoords(xCoord, yCoord);
                model.endTurn();
            }

            view.board.boardPane.setRowIndex(view.board.playerTwo.getGraphicsNode(), view.board.playerTwo.getYCoord());
            view.board.boardPane.setColumnIndex(view.board.playerTwo.getGraphicsNode(), view.board.playerTwo.getXCoord());
            view.board.playerTwo.changeColorToStart();
        }
    }

    public void playerOneHorizontalWallButtonOnClick(){ //Hopefully these will call something in model rather than view.
        if (model.isPlayerOneTurn()) {
            view.board.setHorizontalWallWasClickedToTrue();
            view.board.setVerticalWallWasClickedToFalse();
            view.board.playerOne.changeColorToStart();
        }
    }

    public void playerTwoHorizontalWallButtonOnClick(){ //Hopefully these will call something in model rather than view.
        if (!model.isPlayerOneTurn()) {
            view.board.setHorizontalWallWasClickedToTrue();
            view.board.setVerticalWallWasClickedToFalse();
            view.board.playerTwo.changeColorToStart();
        }
    }

    public void playerOneVerticalWallButtonOnClick(){ //Hopefully these will call something in model rather than view.
        if (model.isPlayerOneTurn()) {
            view.board.setVerticalWallWasClickedToTrue();
            view.board.setHorizontalWallWasClickedToFalse();
            view.board.playerOne.changeColorToStart();
        }
    }

    public void playerTwoVerticalWallButtonOnClick(){ //Hopefully these will call something in model rather than view.
        if (!model.isPlayerOneTurn()) {
            view.board.setVerticalWallWasClickedToTrue();
            view.board.setHorizontalWallWasClickedToFalse();
            view.board.playerTwo.changeColorToStart();
        }
    }

    public static int convertFromGridPaneCoord(int gridPaneCoord) {
        return gridPaneCoord / 2 + 1;
    }
}
