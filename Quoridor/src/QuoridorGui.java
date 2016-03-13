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

        Label playerOneTurnLabel = getPlayerOneTextLabel("Turn: ****");


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

    private Button getVerticalWallButtonPlayerOne() {
        Button placeVerticalWallButton = new Button("Place Vertical Wall");
        placeVerticalWallButton.getStyleClass().add("darkOnLight");
        placeVerticalWallButton.setOnMouseClicked(event -> {
            controller.playerOneVerticalWallButtonOnClick();
        });
        return placeVerticalWallButton;
    }


    private Button getHorizontalWallButtonPlayerOne() {
        Button placeHorizontalWallButton = new Button("Place Horizontal Wall");
        placeHorizontalWallButton.getStyleClass().add("darkOnLight");
        placeHorizontalWallButton.setOnMouseClicked(event -> {
            controller.playerOneHorizontalWallButtonOnClick();
        });
        return placeHorizontalWallButton;
    }

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

        Label playerTwoTurnLabel = getPlayerTwoTextLabel("Turn:    ");

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

    private Button getVerticalWallButtonPlayerTwo() {
        Button placeVerticalWallButton = new Button("Place Vertical Wall");
        playerTwoPane.setHalignment(placeVerticalWallButton, HPos.RIGHT);
        placeVerticalWallButton.getStyleClass().add("lightOnDark");
        placeVerticalWallButton.setOnMouseClicked(event -> {
            controller.playerTwoVerticalWallButtonOnClick();
        });
        return placeVerticalWallButton;
    }

    private Button getHorizontalWallButtonPlayerTwo() {
        Button placeHorizontalWallButton = new Button("Place Horizontal Wall");
        playerTwoPane.setHalignment(placeHorizontalWallButton, HPos.RIGHT);
        placeHorizontalWallButton.getStyleClass().add("lightOnDark");
        placeHorizontalWallButton.setOnMouseClicked(event -> {
            controller.playerTwoHorizontalWallButtonOnClick();
        });
        return placeHorizontalWallButton;
    }

    private Label getPlayerTwoTextLabel(String text) {
        Label playerNameLabel = new Label(text);
        playerNameLabel.getStyleClass().add("playerTwoLabel");
        playerTwoPane.setHalignment(playerNameLabel, HPos.RIGHT);
        return playerNameLabel;
    }

    public void updatePlayerOneWallCount(int count) {
        Label playerOneWallCount = getPlayerOneTextLabel("Wall Count: " + model.getPlayerOneWallCount());
        playerOnePane.getChildren().remove(playerPaneLabels[0][3][0]);
        playerOnePane.add(playerOneWallCount, 0, 3);
        playerPaneLabels[0][3][0] = playerOneWallCount;
    }

    public void updatePlayerTwoWallCount(int count) {
        Label playerTwoWallCount = getPlayerTwoTextLabel("Wall Count: " + model.getPlayerTwoWallCount());
        playerTwoPane.getChildren().remove(playerPaneLabels[0][3][1]);
        playerTwoPane.add(playerTwoWallCount, 0, 3);
        playerPaneLabels[0][3][1] = playerTwoWallCount;

        playerTwoPane.setHalignment(playerTwoWallCount, HPos.RIGHT);
    }

    public void updateTurnLabels() {
        Label playerOneTurnLabel;
        Label playerTwoTurnLabel;
        if (model.isPlayerOneTurn()) {
            playerOneTurnLabel = getPlayerOneTextLabel("Turn: ****");
            playerTwoTurnLabel = getPlayerTwoTextLabel("Turn:    ");
        } else {
            playerTwoTurnLabel = getPlayerTwoTextLabel("Turn: ****");
            playerOneTurnLabel = getPlayerOneTextLabel("Turn:     ");
        }
        playerOnePane.getChildren().remove(playerPaneLabels[0][4][0]);
        playerTwoPane.getChildren().remove(playerPaneLabels[0][4][1]);
        playerOnePane.add(playerOneTurnLabel, 0, 4);
        playerTwoPane.add(playerTwoTurnLabel, 0, 4);
        playerPaneLabels[0][4][0] = playerOneTurnLabel;
        playerPaneLabels[0][4][1] = playerTwoTurnLabel;

        playerTwoPane.setHalignment(playerTwoTurnLabel, HPos.RIGHT);
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

    public void newGame() {
        restartOnNewGame();
        showGameStage();
    }

    public void restartOnNewGame() {
        gameStage.close();
        controller = new QuoridorController(this);
        setSystems(controller, controller.getModel());
        gameStage = new Stage();
        setUpGameStage();
    }

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

    public void playerOneWins() {
        setUpWinStateStage("Player One");
        showWinStateStage();
    }

    public void playerTwoWins() {
        setUpWinStateStage("Player Two");
        showWinStateStage();
    }

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
