/**
 * Created by garfinklei on 2/28/16.
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.input.*;

public class Pawn {
    private Shape imageNode;
    private int xCoord;
    private int yCoord;
    private int pawnRadius;

    public Pawn(int x, int y, int radius, String color) {
        imageNode = new Circle(radius, Paint.valueOf(color));

        xCoord = x;
        yCoord = y;
        pawnRadius = radius;

    }

    public Node getGraphicsNode() {
        return imageNode;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setXCoord( int x) {
        xCoord = x;
    }

    public void setYCoord(int y) {
        yCoord = y;
    }

    public int getPawnRadius() {
        return pawnRadius;
    }

    public Node changeColor() {
        Circle tempCircle = new Circle(pawnRadius, (Paint.valueOf("yellow")));
        return tempCircle;
    }
}
