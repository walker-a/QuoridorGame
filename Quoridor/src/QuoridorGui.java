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
    QuoridorBoard board = new QuoridorBoard();

    /**
     * runs the GUI
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Node boardPane = addBoard();
        Node menuAndTitlePane = addMenusAndTitles();
        Node playerOnePane = addPlayerPane("Player 1");
        Node playerTwoPane = addPlayerPane("Player 2");

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
    private Node addPlayerPane(String playerName) {
        GridPane playerPane = new GridPane();
        playerPane.setAlignment(Pos.CENTER);
        playerPane.setHgap(5);
        playerPane.setVgap(5);
        playerPane.setPadding(new Insets(0, 10, 0, 10));

        Label playerNameLabel = new Label(playerName);

        //not going to be present in final prouduct
        Label instructions = new Label("Click a center gap");
        Label instructions2 = new Label("to place wall");

        Button placeHorizontalWallButton = new Button("Place Horizontal Wall");
        placeHorizontalWallButton.setOnMouseClicked(event -> {
            board.setHorizontalWallWasClicked();
            playerPane.add(instructions, 0, 10);
            playerPane.add(instructions2, 0, 11);});
        Button placeVerticalWallButton = new Button("Place Vertical Wall");
        placeVerticalWallButton.setOnMouseClicked(event -> {
            board.setVerticalWallWasClicked();
            playerPane.add(instructions, 0, 10);
            playerPane.add(instructions2, 0, 11);});
        Label wallCountLabel = new Label("Wall Count: 10");

        placeHorizontalWallButton.setMinWidth(MIN_BUTTON_WIDTH);
        placeVerticalWallButton.setMinWidth(MIN_BUTTON_WIDTH);

        playerPane.add(playerNameLabel, 0, 0);
        playerPane.add(placeHorizontalWallButton, 0, 1);
        playerPane.add(placeVerticalWallButton, 0, 2);
        playerPane.add(wallCountLabel, 0, 3);

        return playerPane;
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
