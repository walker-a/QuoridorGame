/**
 * Created by walkera2 on 3/8/16.
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
