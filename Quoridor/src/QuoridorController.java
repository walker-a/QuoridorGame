

/**
 * Created by labuser on 2/29/16.
 */
public class QuoridorController {
    public QuoridorModel model;
    QuoridorGui view;
    int playerOneWallsRemaining = 10;
    int playerTwoWallsRemaining = 10;



    public QuoridorController(QuoridorGui view) {
        model = new QuoridorModel();
        this.view = view;
        this.view.setController(this);
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

    public void horizontalWallButtonOnClick(){ //Hopefully these will call something in model rather than view.
        view.board.setHorizontalWallWasClickedToTrue();
        view.board.setVerticalWallWasClickedToFalse();
    }

    public void verticalWallButtonOnClick() {
        view.board.setVerticalWallWasClickedToTrue();
        view.board.setHorizontalWallWasClickedToFalse();
    }

    public static int convertFromGridPaneCoord(int gridPaneCoord) {
        return gridPaneCoord / 2 + 1;
    }
}
