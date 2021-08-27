package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

/*
Implement HUD -> Use mouseX, mouseY from StdDraw, rely heavily on Lab13 code
    Heads up display shows the tile the mouse is hovering over!!!
        (prints TETile[i][j] for position i,j on the board) ->
        For HUD, use initialize w/offset method in TERenderer/look through lab 13
        Add a menu option to give your avatar a name which is displayed on the HUD.
        Triple check how the Room dimensions are used in WorldMaker.java
Remaining bug in the way we save the game
    (possibly adding writes, or gameState string is incorrect).
    Track what gameState becomes throughout the code

*/

public class Game {
    TETile[][] world;
    Point avatarPos;
    String avatarName;
    InputSource inputSource;
    TETile[][] miniTable;
    static final int LOS_WIDTH = 8;
    static final int LOS_HEIGHT = 8;

    public Game(TETile[][] board, InputSource inputSource) {
        world = board;
        Random random = new Random(240);
        avatarPos = new Point(0, 0);
        while (world[avatarPos.x][avatarPos.y] != Tileset.FLOOR) {
            avatarPos.x = RandomUtils.uniform(random, 78) + 1;
            avatarPos.y = RandomUtils.uniform(random, 28) + 1;
        }
        world[avatarPos.x][avatarPos.y] = Tileset.AVATAR;
        miniTable = new TETile[world.length][world[0].length];
        this.inputSource = inputSource;
        avatarName = "";
    }


    //:Q should NOT be returned from Game
    public String playGame() {
        String endString = "";
        TERenderer render = new TERenderer();
        if (inputSource.getClass().equals(KeyboardSource.class)) {
            render.initialize(80, 30);
            updateMiniTable();
            render.renderFrame(miniTable); // input with string bug here
        }
        while (inputSource.possibleNextInput()) {
            displayHUD();
            char c = inputSource.getNextKey();
            if (c == 'W') {
                updateAvatar(avatarPos.x, avatarPos.y + 1, render);
                endString = endString + c;
            } else if (c == 'A') {
                updateAvatar(avatarPos.x - 1, avatarPos.y, render);
                endString = endString + c;
            } else if (c == 'S') {
                updateAvatar(avatarPos.x, avatarPos.y - 1, render);
                endString = endString + c;
            } else if (c == 'D') {
                updateAvatar(avatarPos.x + 1, avatarPos.y, render);
                endString = endString + c;
            } else if (inputSource.getClass().equals(KeyboardSource.class) && (c == 'C')) {
                updateAvatarName();
                displayHUD();
            } else if (c == ':') {
                c = inputSource.getNextKey();
                if (c == 'Q') {
                    break;
                }
            }
        }
        if (inputSource.getClass().equals(KeyboardSource.class)) {
            render.renderFrame(world);
            displayHUD();
        }
        return endString;
    }

    public void updateAvatar(int x, int y, TERenderer renderer) {
        if (world[x][y] == Tileset.FLOOR) {
            world[avatarPos.x][avatarPos.y] = Tileset.FLOOR;
            world[x][y] = Tileset.AVATAR;
            avatarPos = new Point(x, y);
            if (inputSource.getClass().equals(KeyboardSource.class)) {
                updateMiniTable();
                renderer.renderFrame(miniTable);
                displayHUD();
            }
        }
    }

    public void updateMiniTable() {
        Room lineOfSight = new Room(LOS_WIDTH, LOS_HEIGHT, avatarPos.x, avatarPos.y);
        initializeMini();
        for (int i = lineOfSight.bLX(); i <= lineOfSight.uRX(); i++) {
            if (i >= 0 && i < world.length) {
                for (int j = lineOfSight.bLY(); j <= lineOfSight.uRY(); j++) {
                    if (j >= 0 && j < world[0].length) {
                        miniTable[i][j] = world[i][j];
                    }
                }
            }
        }
    }

    private void initializeMini() {
        for (int i = 0; i < miniTable.length; i++) {
            for (int j = 0; j < miniTable[0].length; j++) {
                miniTable[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void updateAvatarName() {
        StdDraw.text(25, 29, "Name your avatar, then hit Enter: ");
        StdDraw.show();
        char nextChar;
        while (true) {
            nextChar = inputSource.getNextKey();
            if (nextChar == '\n') {
                break;
            } else {
                avatarName += nextChar;
                StdDraw.textLeft(33, 29, avatarName); // Write name on screen
                StdDraw.show();
            }
        }
    }

    private void displayHUD() {
        if (inputSource.getClass().equals(KeyboardSource.class)) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(1, 29, currentTile().description()); // Write name on screen
            StdDraw.textLeft(10, 29, "Avatar: " + avatarName);
            StdDraw.show();
        }
    }

    private TETile currentTile() {
        TETile currTile;
        int xPos = (int) StdDraw.mouseX();
        int yPos = (int) StdDraw.mouseY();
        if (yPos >= world[0].length) {
            currTile = Tileset.NOTHING;
        } else {
            currTile = world[xPos][yPos];
        }
        System.out.println(currTile.description());
        return currTile;
    }

}
