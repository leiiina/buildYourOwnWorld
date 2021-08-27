package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class KeyboardSource implements InputSource {
    private StringSource ss;

    public KeyboardSource(String input) {
        ss = new StringSource(input);
    }
    public char getNextKey() {
        if (ss.possibleNextInput()) {
            return ss.getNextKey();
        }
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                return c;
            }
        }
    }

    public boolean possibleNextInput() {
        return true;
    }
}
