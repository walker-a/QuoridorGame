/**
 * Created by Alex Walker, Isaac Garfinkle, and Mitchell Biewen on 3/8/16.
 * Mainly used to convert coordinates of spaces into grid pane coordinates.
 */
public class TileCoordinate {
    int xCoord;
    int yCoord;

    public TileCoordinate(int xCoordinate, int yCoordinate) {
        xCoord = xCoordinate;
        yCoord = yCoordinate;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public static int convertToGridPaneCoords(int i) {
        return i * 2 - 1;
    }

    public int[] getXGridPanePoint() {
        int[] point = new int[2];
        point[0] = convertToGridPaneCoords(xCoord);
        point[1] = convertToGridPaneCoords(yCoord);
        return point;
    }
}
