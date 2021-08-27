package byow.Core;

public class StringSource implements InputSource {
    private String input;
    private int index;

    public StringSource(String input) {
        this.input = input;
        index = 0;
    }
    public char getNextKey() {
        char c = input.charAt(index);
        index += 1;
        return Character.toUpperCase(c);
    }
    public boolean possibleNextInput() {
        return index < input.length();
    }
}
