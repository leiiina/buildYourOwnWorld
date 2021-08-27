//Sources: https://javaconceptoftheday.com/modify-replace-specific-string-in-text-file-in-java/
//Sources: https://stackoverflow.com/questions/
// 1016278/is-this-the-best-way-to-rewrite-the-content-of-a-file-in-java
//Sources: https://www.cis.upenn.edu/~cis110/15su/lectures/10stddraw.pdf

package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.io.*;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;

public class Engine {
    TERenderer ter = new TERenderer();
    String gameSaveFile = "C:\\Users\\Shifa\\Documents\\cs61b\\"
            + "fa20-s1391\\fa20-proj3-g537\\proj3\\byow\\gameState.txt";

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
/*
    For HUD, use initialize w/offset method in TERenderer/look through lab 13

*/
    //create a file and save the array to it
    private void saveGame(String gameSequence) {
        try {
            File gameStateFile = new File(gameSaveFile);
            FileWriter fileWriter = new FileWriter(gameStateFile, false); //overwrites
            fileWriter.write(gameSequence + "\n"); //is NOT overwriting correctly....
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error reading file. :/");
        }
    }

    private String getGameSequence() {
        String gameSequence = null;
        try {
            File file = new File(gameSaveFile);
            BufferedReader brin = new BufferedReader(new FileReader(gameSaveFile));
            gameSequence = brin.readLine(); //get input string from the text file
        } catch (IOException e) {

            System.out.println("Error reading file. :/");
        }
        System.out.println("Read from file: " + gameSequence);
        if (gameSequence == null) {
            return "";
        }
        return gameSequence;
    }

    public void interactWithKeyboard() {
        TETile[][] finalWorldFrame = null;
        String gameState = "";
        String userCommands = "";
        TERenderer render = new TERenderer(); //do not remove
        render.initialize(80, 32); //do not remove
        showMainMenu();
        char selection = waitForChar();
        if (selection == 'Q') {
            System.exit(0);
        } else if (selection == 'L') {
            gameState = getGameSequence(); //gameState is same as loaded game (N123123Swasd)
            userCommands = getUserCommands(gameState);
        } else if (selection == 'N') {
            StdDraw.clear(new Color(0, 0, 0));
            StdDraw.text(40, 17, "Enter a random seed: ");
            StdDraw.show();
            gameState += selection; // ('N')
            gameState += getInputSeed();
        }
        InputSource inputSource = new KeyboardSource(userCommands);
        finalWorldFrame = getFrame(gameState);
        Game boardGame = new Game(finalWorldFrame, inputSource);
        System.out.println("Calling playGame with no commands yet");
        gameState += boardGame.playGame();
        saveGame(gameState);
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    //"n123123s"  "n123123swasdwasd:q"  "lwsdw:q"
    public TETile[][] interactWithInputString(String input) {
        String gameState = "";
        String userCommands = "";
        String dummy;
        TETile[][] finalWorldFrame;
        if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            gameState = getGameSequence(); //"n123123swdsw"
            userCommands = getUserCommands(gameState) + input.substring(1); //"swdswwsdw:q"
        } else if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            gameState = input; //"n123123swasdwasd:q"
            userCommands = getUserCommands(gameState);
        } else if (input.charAt(0) == 'Q' || input.charAt(0) == 'q') {
            System.exit(0);
            return null; //don't think this will ever execute
        } else {
            return null;
        }
        finalWorldFrame = getFrame(gameState); //based off of "n123123s"
        InputSource inputSource = new StringSource(userCommands);
        //play game if there are more userCommands (besides :Q)
        if (!(userCommands.equals("")
                || ((userCommands.charAt(0) == ':') && (userCommands.charAt(1) == 'Q')))) {
            Game boardGame = new Game(finalWorldFrame, inputSource); //make a game
            //play the game: gameState: "n123123sswdswwsdw:q"
            gameState = getInputSeedString(gameState) + boardGame.playGame();
        }
        saveGame(gameState);
        return finalWorldFrame;
    }

    private TETile[][] getFrame(String input) {
        long seed = seedConverter(input);
        Random random = new Random(seed);
        WorldMaker wmkr = new WorldMaker(random);
        return wmkr.makeWorld();
    }

    private void showMainMenu() {
        StdDraw.clear(new Color(0, 0, 0));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(40, 24, "Main Menu");
        StdDraw.text(40, 20, "(N)ew game");
        StdDraw.text(40, 18, "(L)oad game");
        StdDraw.text(40, 16, "(Q)uit");
        StdDraw.show();
    }

    public char waitForChar() {
        boolean waiting = true;
        while (waiting) {
            if (StdDraw.hasNextKeyTyped()) {
                waiting = false;
                return Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }
        return 'x'; //this is never reached
    }

    //returns seed + 'S'
    public String getInputSeed() {
        char nextChar;
        String input = "";
        while (true) {
            nextChar = waitForChar();
            input += nextChar;
            StdDraw.textLeft(45, 17, input); // Write numbers on loading screen
            StdDraw.show();
            if (nextChar == 'S' || nextChar == 's') {
                return input;
            }
        }
    }

    private String getInputSeedString(String input) {
        int i = 1;
        while ((input.charAt(i) != 's') && (input.charAt(i) != 'S')) {
            i++;
        }
        return input.substring(0, i + 1);
    }

    private String getUserCommands(String input) {
        int i = 1;
        while ((input.charAt(i) != 's') && (input.charAt(i) != 'S')) {
            i++;
        }
        return input.substring(i + 1);
    }

    //Converts String input into long seed.
    private long seedConverter(String input) {
        long seed = 0;
        int i = 1;
        while ((input.charAt(i) != 's') && (input.charAt(i) != 'S')) {
            seed = seed * 10 + Long.parseLong(String.valueOf(input.charAt(i)));
            i++;
        }
        return seed;
    }

}
