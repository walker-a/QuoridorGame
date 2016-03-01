/**
 * Created by Isaac Garfinkle, Alex Walker, and Mitchell Biewen on 2/28/16.
 */
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

/**
 * keeps track of Quoridor pawn information
 */
public class Pawn {
    private Shape imageNode;
    private int xCoord;
    private int yCoord;
    private int pawnRadius;
    private Paint pawnColor;

    /**
     * instantiates pawn object with a corresponding circle node and x y coordinate
     * @param x
     * @param y
     * @param radius
     * @param color
     */
    public Pawn(int x, int y, int radius, String color) {
        imageNode = new Circle(radius, Paint.valueOf(color));

        xCoord = x;
        yCoord = y;
        pawnRadius = radius;
        pawnColor = Paint.valueOf(color);

    }

    /**
     * gets the graphics node of the pawn object
     * @return imageNode
     */
    public Node getGraphicsNode() {
        return imageNode;
    }

    /**
     * gets the xCoordinate of the pawn
     * @return xCoord
     */
    public int getXCoord() {
        return xCoord;
    }

    /**
     * gets the yCoordinate of the pawn
     * @return yCoord
     */
    public int getYCoord() {
        return yCoord;
    }

    /**
     * sets the xCoordinate of the pawn to 'x'
     * @param x
     */
    public void setXCoord( int x) {
        xCoord = x;
    }

    /**
     * sets the yCoordinate of the pawn to 'y'
     * @param y
     */
    public void setYCoord(int y) {
        yCoord = y;
    }

    /**
     * gets the pawn's radius
     * @return pawnRadius
     */
    public int getPawnRadius() {
        return pawnRadius;
    }

    /**
     * changes the pawn's color to yellow
     * @return
     */
    public void changeColorToYellow() {
        imageNode.setFill(Paint.valueOf("yellow"));
    }

    /**
     * changes the pawn's color to yellow
     * @return
     */
    public void changeColorToStart() {
        imageNode.setFill(pawnColor);
    }
}
