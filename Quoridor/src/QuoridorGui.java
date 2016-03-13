import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * Overall view of the Quoridor game
 * Created by Isaac Garfinkle, Mitchell Biewen, and Alex Walker on 2/28/16.
 */
public class QuoridorGui extends Application {
    public static final int MIN_BUTTON_WIDTH = 60;
    public static final int SCENE_HEIGHT = 700;
    public static final int SCENE_WIDTH = (int) (1.38 * SCENE_HEIGHT);
    public QuoridorController controller;
    public QuoridorBoard board;
    public QuoridorModel model;
    public GridPane playerOnePane;
    public GridPane playerTwoPane;
    public Label[][][] playerPaneLabels = new Label[2][5][2];
    public Scene gameScene;
    public Stage gameStage;
    public ImageCursor horizontalWallCursor;
    public ImageCursor verticalWallCursor;
    public boolean horizontalWallWasClicked = false;
    public boolean verticalWallWasClicked = false;
    public Stage mainMenuStage;
    public Scene mainMenuScene;
    public Stage winStateStage;
    public Scene winStateScene;
    public Stage rulesStage;
    public Scene rulesScene;
    public final int menuHeight = 350;
    public final int menuWidth = (int) (menuHeight * 1.7);
    public final int rulesHeight = 700;
    public final int rulesWidth = (int) (700 * 1.5);
    public final int winStateHeight = 400;
    public final int winStateWidth = (int) (winStateHeight * 1.5);
    public double boardWidth;

    public QuoridorGui() {
    }

    /**
     * Sets up references to the model and controller.
     * @param quoridorController
     * @param quoridorModel
     */
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
        controller = new QuoridorController(this);
        setSystems(controller, controller.getModel());

        gameStage = stage;
        setUpCursors();
        setUpMainMenuStage();
        showMainMenuStage();
        setUpGameStage();
        setUpRulesStage();
    }

    /**
     * Instantiates the main menu window.
     */
    public void setUpMainMenuStage() {
        mainMenuStage = new Stage();

        StackPane root = new StackPane();
        Node frontPane = addMenuButtons();
        ImageView backPane = addMenuGraphic();
        backPane.fitWidthProperty().bind(mainMenuStage.widthProperty());
        root.getChildren().addAll(backPane,frontPane);

        mainMenuScene = new Scene(root, menuWidth, menuHeight);

        mainMenuScene.getStylesheets().add("ButtonStyle.css");

        mainMenuStage.setScene(mainMenuScene);
        mainMenuStage.setResizable(false);
        mainMenuStage.setTitle("Main Menu");
    }

    /**
     * Adds buttons to the main menu window.
     * @return
     */
    public Node addMenuButtons() {
        GridPane menuButtons = new GridPane();

        Button rulesButton = new Button("Rules");
        rulesButton.getStyleClass().add("darkOnLight");
        rulesButton.setOnMouseClicked(event -> {
            showRulesStage();
        });
        rulesButton.setMinWidth(MIN_BUTTON_WIDTH);
        menuButtons.add(rulesButton, 0, 0);

        Button startGameButton = new Button("Start");
        startGameButton.getStyleClass().add("lightOnDark");
        startGameButton.setOnMouseClicked(event -> {
            hideMainMenuStage();
            showGameStage();
        });
        startGameButton.setMinWidth(MIN_BUTTON_WIDTH);
        menuButtons.add(startGameButton, 1, 0);

        menuButtons.setAlignment(Pos.BOTTOM_CENTER);

        return menuButtons;
    }

    /**
     * Adds title image to the main menu window.
     * @return
     */
    public ImageView addMenuGraphic() {
        ImageView menuGraphic = new ImageView(new Image("QuoridorMainMenuTitle.png"));
        return menuGraphic;
    }

    public void showMainMenuStage() {
        mainMenuStage.show();
    }

    public void hideMainMenuStage() {
        mainMenuStage.hide();
    }

    /**
     * Instantiates the rules window.
     */
    public void setUpRulesStage() {
        rulesStage = new Stage();

        StackPane root = new StackPane();
        Node frontPane = addRulesStageButtons();
        ImageView backPane = addRulesGraphic();
        backPane.fitWidthProperty().bind(rulesStage.widthProperty());
        root.getChildren().addAll(backPane,frontPane);

        rulesScene = new Scene(root);

        rulesScene.getStylesheets().add("ButtonStyle.css");

        rulesStage.setScene(rulesScene);
        rulesStage.setResizable(false);
        rulesStage.setTitle("Rules");
    }

    /**
     * Adds buttons to the rules window.
     * @return
     */
    public Node addRulesStageButtons() {
        GridPane rulesButtons = new GridPane();

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("darkOnLight");
        mainMenuButton.setOnMouseClicked(event -> {
            goToMainMenu();
        });
        mainMenuButton.setMinWidth(MIN_BUTTON_WIDTH);
        rulesButtons.add(mainMenuButton, 0, 0);

        Button startGameButton = new Button("Start Game");
        startGameButton.getStyleClass().add("lightOnDark");
        startGameButton.setOnMouseClicked(event -> {
            hideMainMenuStage();
            hideRulesStage();
            showGameStage();
        });
        startGameButton.setMinWidth(MIN_BUTTON_WIDTH);
        rulesButtons.add(startGameButton, 1, 0);

        rulesButtons.setAlignment(Pos.BOTTOM_CENTER);

        return rulesButtons;
    }

    /**
     * Adds a picture of the rules document to the rules window.
     * @return
     */
    public ImageView addRulesGraphic() {
        ImageView rulesGraphic = new ImageView(new Image("QuoridorRules.png", rulesWidth, rulesHeight, true, true));
        return rulesGraphic;
    }

    public void showRulesStage() {
        rulesStage.show();
    }

    public void hideRulesStage() {
        rulesStage.hide();
    }

    /**
     * Instantiates the actual gameplay window.
     */
    public void setUpGameStage() {
        BorderPane root = new BorderPane();
        root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);

        Node boardPane = addBoard();
        Node menuAndTitlePane = addMenusAndTitles();
        Node playerOnePane = addPlayerOnePane("Player 1");
        Node playerTwoPane = addPlayerTwoPane("Player 2");

        boardPane.setOnMouseEntered(event -> {
            setCorrectCursor();
        });
        playerOnePane.setOnMouseEntered(event -> {
            setCorrectCursor();
        });
        playerTwoPane.setOnMouseEntered(event -> {
            setCorrectCursor();
        });

        root.setTop(menuAndTitlePane);
        root.setCenter(boardPane);
        root.setLeft(playerOnePane);
        root.setRight(playerTwoPane);

        gameScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        gameScene.getStylesheets().add("ButtonStyle.css");

        gameStage.setTitle("Quoridor Game");
        gameStage.setScene(gameScene);
        gameStage.setResizable(false);

        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent key) {
                if (key.getCode() == KeyCode.ESCAPE) {
                   controller.resetTurn();
                }
            };
        });
    }

    public void showGameStage() {
        gameStage.show();
    }

    public void hideGameStage() {
        gameStage.hide();
    }

    /**
     * Adds a node for all the player functions going on the side panel
     * @param playerName
     * @return playerPane node
     */
    private Node addPlayerOnePane(String playerName) {
        playerOnePane = new GridPane();
        playerOnePane.setAlignment(Pos.CENTER_LEFT);
        playerOnePane.setHgap(5);
        playerOnePane.setVgap(5);
        playerOnePane.setPadding(new Insets(0, 10, 0, 10));

        Label playerNameLabel = getPlayerOneTextLabel(playerName);


        Button placeHorizontalWallButton = getHorizontalWallButtonPlayerOne();


        Button placeVerticalWallButton = getVerticalWallButtonPlayerOne();


        Label playerOneWallCount = getPlayerOneTextLabel("Wall Count: " + model.getPlayerOneWallCount());

        Label playerOneTurnLabel = getPlayerOneTextLabel("It's your turn!");


        placeHorizontalWallButton.setMinWidth(MIN_BUTTON_WIDTH);
        placeVerticalWallButton.setMinWidth(MIN_BUTTON_WIDTH);

        playerOnePane.add(playerNameLabel, 0, 0);
        playerOnePane.add(placeHorizontalWallButton, 0, 1);
        playerOnePane.add(placeVerticalWallButton, 0, 2);

        playerOnePane.add(playerOneWallCount, 0, 3);
        playerPaneLabels[0][3][0] = playerOneWallCount;

        playerOnePane.add(playerOneTurnLabel, 0, 4);
        playerPaneLabels[0][4][0] = playerOneTurnLabel;

        playerOnePane.setBackground(new Background(new BackgroundFill(Paint.valueOf("slategrey"), CornerRadii.EMPTY, Insets.EMPTY)));

        return playerOnePane;
    }

    /**
     * Instantiates button for Player 1 to click to place a vertical wall.
     * @return
     */
    private Button getVerticalWallButtonPlayerOne() {
        Button placeVerticalWallButton = new Button("Place Vertical Wall");
        placeVerticalWallButton.getStyleClass().add("darkOnLight");
        placeVerticalWallButton.setOnMouseClicked(event -> {
            controller.playerOneVerticalWallButtonOnClick();
        });
        return placeVerticalWallButton;
    }


    /**
     * Instantiates button for Player 1 to click to place a horizontal wall.
     * @return
     */
    private Button getHorizontalWallButtonPlayerOne() {
        Button placeHorizontalWallButton = new Button("Place Horizontal Wall");
        placeHorizontalWallButton.getStyleClass().add("darkOnLight");
        placeHorizontalWallButton.setOnMouseClicked(event -> {
            controller.playerOneHorizontalWallButtonOnClick();
        });
        return placeHorizontalWallButton;
    }

    /**
     * Instantiates a label that tells which player is Player 1.
     * @param text
     * @return
     */
    private Label getPlayerOneTextLabel(String text) {
        Label playerNameLabel = new Label(text);
        playerNameLabel.getStyleClass().add("playerOneLabel");
        playerNameLabel.setAlignment(Pos.CENTER_LEFT);
        return playerNameLabel;
    }

    /**
     * Adds a node for all the player functions going on the side panel
     * @param playerName
     * @return playerPane node
     */
    private Node addPlayerTwoPane(String playerName) {
        playerTwoPane = new GridPane();
        playerTwoPane.setAlignment(Pos.CENTER_RIGHT);
        playerTwoPane.setHgap(5);
        playerTwoPane.setVgap(5);
        playerTwoPane.setPadding(new Insets(0, 10, 0, 10));

        Label playerNameLabel = getPlayerTwoTextLabel(playerName);

        Button placeHorizontalWallButton = getHorizontalWallButtonPlayerTwo();

        Button placeVerticalWallButton = getVerticalWallButtonPlayerTwo();


        Label playerTwoWallCount = getPlayerTwoTextLabel("Wall Count: " + model.getPlayerTwoWallCount());

        Label playerTwoTurnLabel = getPlayerTwoTextLabel("");

        placeHorizontalWallButton.setMinWidth(MIN_BUTTON_WIDTH);
        placeVerticalWallButton.setMinWidth(MIN_BUTTON_WIDTH);

        playerTwoPane.add(playerNameLabel, 0, 0);
        playerTwoPane.add(placeHorizontalWallButton, 0, 1);
        playerTwoPane.add(placeVerticalWallButton, 0, 2);

        playerTwoPane.add(playerTwoWallCount, 0, 3);
        playerPaneLabels[0][3][1] = playerTwoWallCount;

        playerTwoPane.add(playerTwoTurnLabel, 0, 4);
        playerPaneLabels[0][4][1] = playerTwoTurnLabel;

        playerTwoPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("seashell"), CornerRadii.EMPTY, Insets.EMPTY)));

        return playerTwoPane;
    }

    /**
     * Instantiates button for Player 2 to click to place a vertical wall.
     * @return
     */
    private Button getVerticalWallButtonPlayerTwo() {
        Button placeVerticalWallButton = new Button("Place Vertical Wall");
        playerTwoPane.setHalignment(placeVerticalWallButton, HPos.RIGHT);
        placeVerticalWallButton.getStyleClass().add("lightOnDark");
        placeVerticalWallButton.setOnMouseClicked(event -> {
            controller.playerTwoVerticalWallButtonOnClick();
        });
        return placeVerticalWallButton;
    }

    /**
     * Instantiates button for Player 2 to click to place a horizontal wall.
     * @return
     */
    private Button getHorizontalWallButtonPlayerTwo() {
        Button placeHorizontalWallButton = new Button("Place Horizontal Wall");
        playerTwoPane.setHalignment(placeHorizontalWallButton, HPos.RIGHT);
        placeHorizontalWallButton.getStyleClass().add("lightOnDark");
        placeHorizontalWallButton.setOnMouseClicked(event -> {
            controller.playerTwoHorizontalWallButtonOnClick();
        });
        return placeHorizontalWallButton;
    }

    /**
     * Instantiates a label that tells which player is Player 1.
     * @param text
     * @return
     */
    private Label getPlayerTwoTextLabel(String text) {
        Label playerNameLabel = new Label(text);
        playerNameLabel.getStyleClass().add("playerTwoLabel");
        playerTwoPane.setHalignment(playerNameLabel, HPos.RIGHT);
        return playerNameLabel;
    }

    /**
     * Decreases Player 1 wall count after a wall is placed by Player 1.
     * @param count
     */
    public void updatePlayerOneWallCount(int count) {
        Label playerOneWallCount = getPlayerOneTextLabel("Wall Count: " + model.getPlayerOneWallCount());
        playerOnePane.getChildren().remove(playerPaneLabels[0][3][0]);
        playerOnePane.add(playerOneWallCount, 0, 3);
        playerPaneLabels[0][3][0] = playerOneWallCount;
    }

    /**
     * Decreases Player 2 wall count after a wall is placed by Player 2.
     * @param count
     */
    public void updatePlayerTwoWallCount(int count) {
        Label playerTwoWallCount = getPlayerTwoTextLabel("Wall Count: " + model.getPlayerTwoWallCount());
        playerTwoPane.getChildren().remove(playerPaneLabels[0][3][1]);
        playerTwoPane.add(playerTwoWallCount, 0, 3);
        playerPaneLabels[0][3][1] = playerTwoWallCount;

        playerTwoPane.setHalignment(playerTwoWallCount, HPos.RIGHT);
    }

    /**
     * Declares labels to indicate whose turn it is.
     */
    public void updateTurnLabels() {
        Label playerOneTurnLabel;
        Label playerTwoTurnLabel;
        if (model.isPlayerOneTurn()) {
            playerOneTurnLabel = getPlayerOneTextLabel("It's your turn!");
            playerTwoTurnLabel = getPlayerTwoTextLabel("");
        } else {
            playerTwoTurnLabel = getPlayerTwoTextLabel("It's your turn!");
            playerOneTurnLabel = getPlayerOneTextLabel("");
        }
        playerOnePane.getChildren().remove(playerPaneLabels[0][4][0]);
        playerTwoPane.getChildren().remove(playerPaneLabels[0][4][1]);
        playerOnePane.add(playerOneTurnLabel, 0, 4);
        playerTwoPane.add(playerTwoTurnLabel, 0, 4);
        playerPaneLabels[0][4][0] = playerOneTurnLabel;
        playerPaneLabels[0][4][1] = playerTwoTurnLabel;

        playerTwoPane.setHalignment(playerTwoTurnLabel, HPos.RIGHT);
    }

    /**
     * Changes label to indicate whose turn it is and updates wall counts.
     * @param playerOneCount
     * @param playerTwoCount
     */
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
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("snow"), CornerRadii.EMPTY, Insets.EMPTY)));

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
        titlePane.setOnMouseEntered(event -> {
            setCorrectCursor();
        });
        titleMenuPane.add(menuPane, 0, 0);
        titleMenuPane.add(titlePane, 0, 1);

        titleMenuPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("snow"), CornerRadii.EMPTY, Insets.EMPTY)));
        return titleMenuPane;
    }

    /**
     * Returns a node consisting of the game's title pane
     * @return titlePane node
     */
    private Node addTitlePane() {
        GridPane titlePane = new GridPane();

        Rectangle leftSide = new Rectangle(SCENE_WIDTH/2 + 1, SCENE_WIDTH/20, Paint.valueOf("slategrey"));
        Rectangle rightSide = new Rectangle(SCENE_WIDTH/2, SCENE_WIDTH/20, Paint.valueOf("seashell"));
        titlePane.setAlignment(Pos.BOTTOM_CENTER);

        Label titleLeftHalf = new Label("Q u o r ");
        Label titleRightHalf = new Label(" i d o r");

        titleLeftHalf.setFont(new Font("Hiragino Kaku Gothic Pro", SCENE_HEIGHT / 20));
        titleLeftHalf.setTextFill(Paint.valueOf("seashell"));
        titlePane.setHalignment(titleLeftHalf, HPos.LEFT);

        titleRightHalf.setFont(new Font("Hiragino Kaku Gothic Pro", SCENE_HEIGHT / 20));
        titleRightHalf.setTextFill(Paint.valueOf("slategrey"));
        titlePane.setHalignment(titleLeftHalf, HPos.RIGHT);

        titlePane.add(leftSide, 0, 0);
        titlePane.add(rightSide, 1, 0);
        titlePane.add(titleLeftHalf, 0, 0);
        titlePane.add(titleRightHalf, 1, 0);

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

        menuPane.setOnMouseEntered(event -> {
            setCursorToNormal();
        });

        Menu gameMenu = new Menu("Quoridor");

        MenuItem rules = new MenuItem("Rules");

        rules.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showRulesStage();
            }
        });

        MenuItem mainMenu = new MenuItem("MainMenu");

        mainMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                goToMainMenu();
            }
        });

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {System.exit(0);}
        });

        gameMenu.getItems().addAll(rules, mainMenu, quit);

        Menu fileMenu = new Menu("File");

        MenuItem newGameMenuItem = new MenuItem("New Game");
        newGameMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newGame();
            }
        });

        fileMenu.getItems().addAll(newGameMenuItem);

        menuBar.getMenus().addAll(gameMenu, fileMenu);
        menuPane.getChildren().add(menuBar);

        return menuPane;
    }

    /**
     * Instantiates cursors to show a wall after a place wall button was clicked.
     */
    private void setUpCursors() {
        //Sets up the image cursors
        int tileWidth = (int) (getSceneHeight() / 29 * 2.25);
        int wallWidth = (int) (getSceneHeight() / 29 * 0.5);
        Image horizontalWallImage = new Image("HorizontalWall.png", tileWidth * 2 + wallWidth, wallWidth, true, true);
        Image verticalWallImage = new Image("VerticalWall.png", wallWidth, tileWidth * 2 + wallWidth, true, true);
        horizontalWallCursor = new ImageCursor(horizontalWallImage, tileWidth + wallWidth / 2, wallWidth / 2);
        verticalWallCursor = new ImageCursor(verticalWallImage, wallWidth / 2, tileWidth + wallWidth / 2);
    }

    /**
     * Returns cursors to normal.
     */
    public void setCursorToNormal() {
        gameScene.setCursor(Cursor.DEFAULT);
    }

    /**
     * Changes cursor to show a horizontal wall after a place horizontal wall button was clicked.
     */
    public void setCursorToHorizontalWall() {
        gameScene.setCursor(horizontalWallCursor);
    }

    /**
     * Changes cursor to show a vertical wall after a place vertical wall button was clicked.
     */
    public void setCursorToVerticalWall() {
        gameScene.setCursor(verticalWallCursor);
    }

    public void newGame() {
        restartOnNewGame();
        showGameStage();
    }

    /**
     * Starts a new game.
     */
    public void restartOnNewGame() {
        gameStage.close();
        controller = new QuoridorController(this);
        setSystems(controller, controller.getModel());
        gameStage = new Stage();
        setUpGameStage();
    }

    /**
     * Goes back to the main menu title screen.
     */
    public void goToMainMenu() {
        try {
            gameStage.close();
        } catch (Exception e) {}
        try {
            rulesStage.close();
        } catch (Exception e) {}
        try {
            winStateStage.close();
        } catch (Exception e) {}
        try {
            mainMenuStage.close();
        } catch (Exception e) {}
        start(new Stage());
    }

    /**
     * Shows window indicating Player 1 has won.
     */
    public void playerOneWins() {
        setUpWinStateStage("Player One");
        showWinStateStage();
    }

    /**
     * Show window indicating Player 2 has won.
     */
    public void playerTwoWins() {
        setUpWinStateStage("Player Two");
        showWinStateStage();
    }

    /**
     * Builds window to declare a winner.
     * @param playerName
     */
    public void setUpWinStateStage(String playerName) {
        winStateStage = new Stage();
        StackPane root = new StackPane();
        Node frontPane = addWinStateStageButtons();
        ImageView backPane = addWinStateStageGraphic(playerName);
        backPane.fitWidthProperty().bind(winStateStage.widthProperty());
        backPane.fitHeightProperty().bind(winStateStage.heightProperty());
        root.getChildren().addAll(backPane,frontPane);

        winStateScene = new Scene(root, winStateWidth, winStateHeight);

        winStateScene.getStylesheets().add("ButtonStyle.css");

        winStateStage.setScene(winStateScene);
        winStateStage.setResizable(false);
        winStateStage.setTitle(playerName + " Wins");
    }

    /**
     * Adds buttons to the window that declares a winner.
     * @return
     */
    public Node addWinStateStageButtons() {
        GridPane winStateButtons = new GridPane();

        Button newGameButton = new Button("New Game");
        newGameButton.getStyleClass().add("darkOnLight");
        newGameButton.setOnMouseClicked(event -> {
            hideWinStateStage();
            newGame();
        });
        newGameButton.setMinWidth(MIN_BUTTON_WIDTH);
        winStateButtons.add(newGameButton, 0, 0);

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.getStyleClass().add("lightOnDark");
        mainMenuButton.setOnMouseClicked(event -> {
            goToMainMenu();
        });
        mainMenuButton.setMinWidth(MIN_BUTTON_WIDTH);
        winStateButtons.add(mainMenuButton, 1, 0);

        winStateButtons.setAlignment(Pos.BOTTOM_CENTER);

        return winStateButtons;
    }

    /**
     * Actually makes the window declaring a winner.
     * @param playerName
     * @return
     */
    public ImageView addWinStateStageGraphic(String playerName) {
        String pictureName = "PlayerOneWinsImage.png";
        if (playerName.equals("Player Two")) {
            pictureName = "PlayerTwoWinsImage.png";
        }
        ImageView winStateGraphic = new ImageView(new Image(pictureName, rulesWidth, rulesHeight, true, true));
        return winStateGraphic;
    }

    public void showWinStateStage() {
        winStateStage.show();
    }

    public void hideWinStateStage() {
        winStateStage.hide();
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

    public void setCorrectCursor() {
        if (getHorizontalWallWasClicked()) {
            setCursorToHorizontalWall();
        }
        else if (getVerticalWallWasClicked()) {
            setCursorToVerticalWall();
        } else {
            setCursorToNormal();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
