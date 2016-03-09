/**
 * Created by walkera2 on 3/8/16.
 */
public class WallCoordinate {
    int centerPointX;
    int centerPointY;
    int[][] wallCoords;

    public WallCoordinate(int xCoord, int yCoord, String orientation) {
        wallCoords = new int[2][2];
        centerPointX = xCoord;
        centerPointY = yCoord;

        if (orientation == "vertical") {
            wallCoords[0][0] = xCoord;
            wallCoords[0][1] = yCoord - 1;
            wallCoords[1][0] = xCoord;
            wallCoords[1][1] = yCoord;

        } if (orientation == "horizontal") {
            wallCoords[0][0] = xCoord - 1;
            wallCoords[0][1] = yCoord;
            wallCoords[1][0] = xCoord;
            wallCoords[1][1] = yCoord;
        }
    }

    public int[][] getWallCoords() {
        return wallCoords;
    }

    public int getCenterPointX() {
        return centerPointX;
    }

    public int getCenterPointY() {
        return centerPointY;
    }

    public int[][] convertToGridPaneCoords() {
        int[][] gridPaneCoords = new int[2][2];
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                gridPaneCoords[i][j] = convertToGridPaneCoord(wallCoords[i][j]);
            }
        }
        return gridPaneCoords;
    }

    public static int convertToGridPaneCoord(int i) {
        return (i * 2 - 1);
    }
}
