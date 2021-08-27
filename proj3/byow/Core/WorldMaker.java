package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WorldMaker {
    private Random random;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MAX_ROOMS = 30;

    public WorldMaker(Random rand) {
        random = rand;
    }

    public TETile[][] makeWorld() {
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        List<Room> roomList = new ArrayList<>();
        List<Point> openingList = new ArrayList<>();
        initializeWorld(finalWorldFrame);
        makeAllRooms(finalWorldFrame, roomList);
        connectHallways(finalWorldFrame, roomList, openingList);
        traceWalls(finalWorldFrame);
        return finalWorldFrame;
    }


    private void initializeWorld(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void makeAllRooms(TETile[][] world, List<Room> roomList) {
        for (int x = 0; x < MAX_ROOMS; x++) {
            Room room = makeNewRandomRoom();
            oneRoomToTiles(world, roomList, room);
        }
    }

    //Places randomized rooms into world.
    private void oneRoomToTiles(TETile[][] world, List<Room> roomList, Room room) {
        if ((!verifyRoom(room)) || (overlaps(roomList, room))) {
            return;
        }

        roomList.add(room);
        for (int i = room.bLX(); i <= room.uRX(); i++) {
            for (int j = room.bLY(); j <= room.uRY(); j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }

    private Room makeNewRandomRoom() {
        int randX = RandomUtils.uniform(random, 78) + 1;
        int randY = RandomUtils.uniform(random, 28) + 1;
        int randWidth = RandomUtils.uniform(random, 6) + 1;
        int randHeight = RandomUtils.uniform(random, 6) + 1;
        return new Room(randWidth, randHeight, new Point(randX, randY));
    }

    private boolean overlaps(List<Room> roomList, Room newRoom) {
        for (Room r : roomList) {
            if (newRoom.overlaps(r)) {
                return true;
            }
        }
        return false;
    }

    private void traceWalls(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j].equals(Tileset.FLOOR)) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (world[k][l].equals(Tileset.NOTHING)) {
                                world[k][l] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }

    }

    //Connects hallways between rooms.
    private void connectHallways(TETile[][] world, List<Room> roomList,
                                 List<Point> openingList) {
        HashMap<Point, Integer> orientation = new HashMap<>();
        for (int i = 0; i < roomList.size(); i++) {
            Room temp = roomList.get(i);
            int randSide = RandomUtils.uniform(random, 4);
            Point opening;
            if (randSide == 0) {
                opening = new Point(temp.bLX(), temp.bLY()
                        + RandomUtils.uniform(random, temp.height)); //left
            } else if (randSide == 1) {
                opening = new Point(temp.uRX()
                        - RandomUtils.uniform(random, temp.width), temp.uRY()); //top
            } else if (randSide == 2) {
                opening = new Point(temp.uRX(), temp.uRY()
                        - RandomUtils.uniform(random, temp.height)); //right
            } else {
                opening = new Point(temp.bLX()
                        + RandomUtils.uniform(random, temp.width), temp.bLY()); //bottom
            }
            openingList.add(opening);
            orientation.put(opening, randSide);
        }

        for (int i = 0; i < openingList.size() - 1; i++) {
            Point currPoint = openingList.get(i);
            Point nextPoint = openingList.get(i + 1);
            if (orientation.get(currPoint) % 2 == 0) {
                horConnect(world, currPoint.x, nextPoint.x, currPoint.y);
                verConnect(world, currPoint.y, nextPoint.y, nextPoint.x);
            } else {
                verConnect(world, currPoint.y, nextPoint.y, currPoint.x);
                horConnect(world, currPoint.x, nextPoint.x, nextPoint.y);
            }

        }
    }

    //Creates horizontal hallway given start and ending X and Y.
    private void horConnect(TETile[][] world, int startX, int endX, int endY) {
        int hallLength = Math.abs(endX - startX) + 1;
        int leftMost = Math.min(endX, startX);
        for (int i = 0; i < hallLength; i++) {
            world[leftMost + i][endY] = Tileset.FLOOR;
        }
    }

    //Creates vertical hallway given start and ending Y and X.
    private void verConnect(TETile[][] world, int startY, int endY, int endX) {
        int hallLength = Math.abs(endY - startY) + 1;
        int bottomMost = Math.min(endY, startY);
        for (int i = 0; i < hallLength; i++) {
            world[endX][bottomMost + i] = Tileset.FLOOR;
        }
    }

    //Verifies if a room can exist.
    private boolean verifyRoom(Room room) {
        return (room.bLX() >= 1 && room.bLY() >= 1 && room.uRX() < 79 && room.uRY() < 29);
    }
}
