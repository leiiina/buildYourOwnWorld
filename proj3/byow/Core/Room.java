package byow.Core;

import java.awt.*;

public class Room {
    int width;
    int height;
    Point bottomleft;
    Point center;

    public Room(int w, int h, Point bl) {
        width = w;
        height = h;
        bottomleft = bl;
        center = new Point((bl.x +  (w / 2)), (bl.y + (h / 2)));
    }

    public Room(int w, int h, int cx, int cy) {
        width = w;
        height = h;
        center = new Point(cx, cy);
        bottomleft = new Point(cx - (w / 2), cy - (h / 2));
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int bLX() {
        return bottomleft.x;
    }

    public int uRX() {
        return bottomleft.x + width - 1;
    } ///should be one less, right? How is it used in WorldMaker? Triple check!!!!

    public int bLY() {
        return bottomleft.y;
    }

    public int uRY() { //should be one less, right? How is it used in WorldMaker? Triple check!!!!
        return bottomleft.y + height - 1;
    }

    public int cX() {
        return center.x;
    }

    public int cY() {
        return center.y;
    }

    public boolean overlaps(Room otherRoom) {
        return rOverlaps(otherRoom);
        //return xsOverlap(otherRoom) && ysOverlap(otherRoom);
    }

    //Checks for overlap between two rooms.
    private boolean rOverlaps(Room r2) {
        return (bLX() <= r2.uRX() && r2.bLX() <= uRX()
                && bLY() <= r2.uRY() && r2.bLY() <= uRY());
    }
}
