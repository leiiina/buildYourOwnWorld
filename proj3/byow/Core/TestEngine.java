package byow.Core;

import byow.TileEngine.TERenderer;

public class TestEngine {
    public static void main(String[] args) {
        TERenderer render = new TERenderer();
        Engine engine = new Engine();
        render.initialize(80, 30);
        //System.out.println(engine.toString());
        ///render.renderFrame(engine.interactWithInputString("n123123s"));
        //render.renderFrame(engine.interactWithInputString("n123894s"));
        // this line should print a different board!!!
        engine.interactWithInputString("n123123sdcdd:q");
        // this line and the first one above should print the exact same board!!!
        //engine.interactWithKeyboard(); //test user input seeds
        System.out.println("Done playing!");
        System.exit(0);
    }
}
