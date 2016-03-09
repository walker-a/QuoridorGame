import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Overall view of the Quoridor game
 * Created by Isaac Garfinkle, Mitchell Biewen, and Alex Walker on 2/28/16.
 */
public class QuoridorGui extends Application {
    public static final int MIN_BUTTON_WIDTH = 60;
    public static final int SCENE_WIDTH = 1400;
    public static final int SCENE_HEIGHT = 1000;
    public QuoridorController controller;
    public QuoridorBoard board;
    public QuoridorModel model;
    public GridPane playerOnePane;
    public GridPane playerTwoPane;
    public Label[][][] playerPaneLabels = new Label[2][5][2];

    public QuoridorGui() {
    }

    public void setSystems(QuoridorController quoridorController, QuoridorModel quoridorModel) {
        model = quoridorModel;
        controller = quoridorController;
        board = new QuoridorBoard(controller, model);
    }

    /**
     * runs the GUI
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        controller = new QuoridorController(this, model);
        BorderPane root = new BorderPane();
        Node boardPane = addBoard();
        Node menuAndTitlePane = addMenusAndTitles();
        Node playerOnePane = addPlayerOnePane("Player 1");
        Node playerTwoPane = addPlayerTwoPane("Player 2");

        root.setTop(menuAndTitlePane);
        root.setCenter(boardPane);
        root.setLeft(playerOnePane);
        root.setRight(playerTwoPane);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setTitle("Quoridor Game");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        Label playerOneWallCount = new Label("Wall Count: 10");
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
        Label playerTwoWallCount = new Label("Wall Count: 10");

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

    public void updatePlayerOneWallCount() {
        Label playerOneWallCount = new Label("Wall Count: " + model.getPlayerOneWallCount() + " ");
        playerOnePane.getChildren().remove(playerPaneLabels[0][3][0]);
        playerOnePane.add(playerOneWallCount, 0, 3);
        playerPaneLabels[0][3][0] = playerOneWallCount;
    }

    public void updatePlayerTwoWallCount() {
        Label playerTwoWallCount = new Label("Wall Count: " + model.getPlayerTwoWallCount() + " ");
        playerTwoPane.getChildren().remove(playerPaneLabels[0][3][1]);
        playerTwoPane.add(playerTwoWallCount, 0, 3);
        playerPaneLabels[0][3][1] = playerTwoWallCount;
    }

    public void updateTurn() {
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
        titleLabel.setFont(new Font("Arial", 50));
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
            public void handle(ActionEvent event) {}
        });
        fileMenu.getItems().addAll(newGame);

        menuBar.getMenus().addAll(gameMenu, fileMenu);
        menuPane.getChildren().add(menuBar);

        return menuPane;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
