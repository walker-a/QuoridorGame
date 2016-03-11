import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Overall view of the Quoridor game
 * Created by Isaac Garfinkle, Mitchell Biewen, and Alex Walker on 2/28/16.
 */
public class QuoridorGui extends Application {
    public static final int MIN_BUTTON_WIDTH = 60;
    public static final int SCENE_HEIGHT = 700;
    public static final int SCENE_WIDTH = (int) (1.4 * SCENE_HEIGHT);
    public QuoridorController controller;
    public QuoridorBoard board;
    public QuoridorModel model;
    public GridPane playerOnePane;
    public GridPane playerTwoPane;
    public Label[][][] playerPaneLabels = new Label[2][5][2];
    public Scene gameScene;
    public ImageCursor horizontalWallCursor;
    public ImageCursor verticalWallCursor;
    public boolean horizontalWallWasClicked = false;
    public boolean verticalWallWasClicked = false;
    public Stage gameStage;

    public QuoridorGui() {
    }

    public void setSystems(QuoridorController quoridorController, QuoridorModel quoridorModel) {
        model = quoridorModel;
        controller = quoridorController;
        board = new QuoridorBoard(controller, model, this);
    }

    /**
     * runs the GUI
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        gameStage = stage;
        setUpCursors();

        controller = new QuoridorController(this);
        BorderPane root = new BorderPane();
        Node boardPane = addBoard();
        Node menuAndTitlePane = addMenusAndTitles();
        Node playerOnePane = addPlayerOnePane("Player 1");
        Node playerTwoPane = addPlayerTwoPane("Player 2");

        root.setTop(menuAndTitlePane);
        root.setCenter(boardPane);
        root.setLeft(playerOnePane);
        root.setRight(playerTwoPane);

        gameScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        gameStage.setTitle("Quoridor Game");
        gameStage.setScene(gameScene);
        gameStage.show();
    }

    private void setUpCursors() {
        //Sets up the image cursors
        int tileWidth = (int) (getSceneHeight() / 29 * 2.25);
        int wallWidth = (int) (getSceneHeight() / 29 * 0.5);
        Image horizontalWallImage = new Image("HorizontalWall.png", tileWidth * 2 + wallWidth, wallWidth, true, true);
        Image verticalWallImage = new Image("VerticalWall.png", wallWidth, tileWidth * 2 + wallWidth, true, true);
        horizontalWallCursor = new ImageCursor(horizontalWallImage, tileWidth + wallWidth / 2, wallWidth / 2);
        verticalWallCursor = new ImageCursor(verticalWallImage, wallWidth / 2, tileWidth + wallWidth / 2);
    }

    public void setCursorToNormal() {
        gameScene.setCursor(Cursor.DEFAULT);
    }

    public void setCursorToHorizontalWall() {
        gameScene.setCursor(horizontalWallCursor);
    }

    public void setCursorToVerticalWall() {
        gameScene.setCursor(verticalWallCursor);
    }

    /**
     * Adds a node for all the player functions going on the side panel
     * @param playerName
     * @return playerPane node
     */
    private Node addPlayerOnePane(String playerName) {
        playerOnePane = new GridPane();
        playerOnePane.setAlignment(Pos.CENTER);
        playerOnePane.setHgap(5);
        playerOnePane.setVgap(5);
        playerOnePane.setPadding(new Insets(0, 10, 0, 10));

        Label playerNameLabel = new Label(playerName);

        Button placeHorizontalWallButton = new Button("Place Horizontal Wall");
        placeHorizontalWallButton.setOnMouseClicked(event -> {
            controller.playerOneHorizontalWallButtonOnClick();
        });

        Button placeVerticalWallButton = new Button("Place Vertical Wall");
        placeVerticalWallButton.setOnMouseClicked(event -> {
            controller.playerOneVerticalWallButtonOnClick();
        });
        Label playerOneWallCount = new Label("Wall Count: " + model.getPlayerOneWallCount());
        Label playerOneTurnLabel = new Label("Turn: ****");

        placeHorizontalWallButton.setMinWidth(MIN_BUTTON_WIDTH);
        placeVerticalWallButton.setMinWidth(MIN_BUTTON_WIDTH);

        playerOnePane.add(playerNameLabel, 0, 0);
        playerOnePane.add(placeHorizontalWallButton, 0, 1);
        playerOnePane.add(placeVerticalWallButton, 0, 2);

        playerOnePane.add(playerOneWallCount, 0, 3);
        playerPaneLabels[0][3][0] = playerOneWallCount;

        playerOnePane.add(playerOneTurnLabel, 0, 4);
        playerPaneLabels[0][4][0] = playerOneTurnLabel;

        return playerOnePane;
    }

    /**
     * Adds a node for all the player functions going on the side panel
     * @param playerName
     * @return playerPane node
     */
    private Node addPlayerTwoPane(String playerName) {
        playerTwoPane = new GridPane();
        playerTwoPane.setAlignment(Pos.CENTER);
        playerTwoPane.setHgap(5);
        playerTwoPane.setVgap(5);
        playerTwoPane.setPadding(new Insets(0, 10, 0, 10));

        Label playerNameLabel = new Label(playerName);

        Button placeHorizontalWallButton = new Button("Place Horizontal Wall");
        placeHorizontalWallButton.setOnMouseClicked(event -> {
            controller.playerTwoHorizontalWallButtonOnClick();
        });

        Button placeVerticalWallButton = new Button("Place Vertical Wall");
        placeVerticalWallButton.setOnMouseClicked(event -> {
            controller.playerTwoVerticalWallButtonOnClick();
        });
        Label playerTwoWallCount = new Label("Wall Count: " + model.getPlayerTwoWallCount());

        Label playerTwoTurnLabel = new Label("Turn:    ");

        placeHorizontalWallButton.setMinWidth(MIN_BUTTON_WIDTH);
        placeVerticalWallButton.setMinWidth(MIN_BUTTON_WIDTH);

        playerTwoPane.add(playerNameLabel, 0, 0);
        playerTwoPane.add(placeHorizontalWallButton, 0, 1);
        playerTwoPane.add(placeVerticalWallButton, 0, 2);

        playerTwoPane.add(playerTwoWallCount, 0, 3);
        playerPaneLabels[0][3][1] = playerTwoWallCount;

        playerTwoPane.add(playerTwoTurnLabel, 0, 4);
        playerPaneLabels[0][4][1] = playerTwoTurnLabel;

        return playerTwoPane;
    }

    public void updatePlayerOneWallCount(int count) {
        Label playerOneWallCount = new Label("Wall Count: " + count + " ");
        playerOnePane.getChildren().remove(playerPaneLabels[0][3][0]);
        playerOnePane.add(playerOneWallCount, 0, 3);
        playerPaneLabels[0][3][0] = playerOneWallCount;
    }

    public void updatePlayerTwoWallCount(int count) {
        Label playerTwoWallCount = new Label("Wall Count: " + count + " ");
        playerTwoPane.getChildren().remove(playerPaneLabels[0][3][1]);
        playerTwoPane.add(playerTwoWallCount, 0, 3);
        playerPaneLabels[0][3][1] = playerTwoWallCount;
    }

    public void updateTurnLabels() {
        Label playerOneTurnLabel;
        Label playerTwoTurnLabel;
        if (model.isPlayerOneTurn()) {
            playerOneTurnLabel = new Label("Turn: ****");
            playerTwoTurnLabel = new Label("Turn:  ");
        } else {
            playerTwoTurnLabel = new Label("Turn: ****");
            playerOneTurnLabel = new Label("Turn:  ");
        }
        playerOnePane.getChildren().remove(playerPaneLabels[0][4][0]);
        playerTwoPane.getChildren().remove(playerPaneLabels[0][4][1]);
        playerOnePane.add(playerOneTurnLabel, 0, 4);
        playerTwoPane.add(playerTwoTurnLabel, 0, 4);
        playerPaneLabels[0][4][0] = playerOneTurnLabel;
        playerPaneLabels[0][4][1] = playerTwoTurnLabel;
    }

    public void updateLabels(int playerOneCount, int playerTwoCount) {
        updateTurnLabels();
        updatePlayerOneWallCount(playerOneCount);
        updatePlayerTwoWallCount(playerTwoCount);
    }

    /**
     * gets the game board node from the QuoridorBoard class and positions it in the center
     * @return boardPane node
     */
    private Node addBoard() {
        GridPane boardPane = board.getBoardPane();
        boardPane.setAlignment(Pos.TOP_CENTER);

        return boardPane;
    }

    /**
     * Returns the node consisting of the menu bar and game title
     * @return titleMenuPane node
     */
    private Node addMenusAndTitles() {
        GridPane titleMenuPane = new GridPane();
        Node menuPane = addMenus();
        Node titlePane = addTitlePane();
        titleMenuPane.add(menuPane, 0, 0);
        titleMenuPane.add(titlePane, 0, 1);
        return titleMenuPane;
    }

    /**
     * Returns a node consisting of the game's title pane
     * @return titlePane node
     */
    private Node addTitlePane() {
        GridPane titlePane = new GridPane();
        Label titleLabel = new Label("Quoridor");
        titleLabel.setFont(new Font("Arial", SCENE_HEIGHT / 20));
        titleLabel.setPadding(new Insets(10, 10, 10, 10));
        titlePane.add(titleLabel, 0,  0);
        titlePane.setAlignment(Pos.CENTER);
        return titlePane;
    }

    /**
     * returns a node consisting of the game menu bar
     * @return
     */
    private Node addMenus() {
        HBox menuPane = new HBox();
        menuPane.setAlignment(Pos.TOP_LEFT);
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth(SCENE_WIDTH);

        menuBar.setOnMouseEntered(event -> {
            setCursorToNormal();
        });
        menuBar.setOnMouseExited(event -> {
            if (getHorizontalWallWasClicked()) {
                setCursorToHorizontalWall();
            }
            else if (getVerticalWallWasClicked()) {
                setCursorToVerticalWall();
            }
        });

        Menu gameMenu = new Menu("Quoridor");

        MenuItem rules = new MenuItem("Rules");
        rules.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {}
        });

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {System.exit(0);}
        });

        gameMenu.getItems().addAll(rules, quit);

        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameStage.close();
                Stage stage = new Stage();
                controller.resetTurn();
                start(stage);
            }
        });

        fileMenu.getItems().addAll(newGame);

        menuBar.getMenus().addAll(gameMenu, fileMenu);
        menuPane.getChildren().add(menuBar);

        return menuPane;
    }

    public void playerOneWins() {
        System.out.println("player one wins");
    }

    public void playerTwoWins() {
        System.out.println("player Two Wins");
    }

    public int getSceneWidth() {
        return SCENE_WIDTH;
    }

    public int getSceneHeight() {
        return SCENE_HEIGHT;
    }

    public Cursor getHorizontalWallCursor() {
        return horizontalWallCursor;
    }

    public Cursor getVerticalWallCursor() {
        return verticalWallCursor;
    }

    public boolean getVerticalWallWasClicked() {
        return verticalWallWasClicked;
    }

    public boolean getHorizontalWallWasClicked() {
        return horizontalWallWasClicked;
    }

    /**
     * Sets the state that the next click of mouse triggers a new horizontal wall
     */
    public void setHorizontalWallWasClickedToTrue() {
        horizontalWallWasClicked = true;
    }

    /**
     * changes the state of horizontalWallWasClicked to false
     */
    public void setHorizontalWallWasClickedToFalse() {
        horizontalWallWasClicked = false;
    }

    /**
     * Sets the state that the next click of mouse triggers a new vertical wall
     */
    public void setVerticalWallWasClickedToTrue() {
        verticalWallWasClicked = true;
    }

    /**
     * changes the state of verticalWallWasClicked to false
     */
    public void setVerticalWallWasClickedToFalse() {
        verticalWallWasClicked = false;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
