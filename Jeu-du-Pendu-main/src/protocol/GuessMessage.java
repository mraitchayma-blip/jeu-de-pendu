package protocol;

public class GuessMessage {
    public final char letter;

    private GuessMessage(char letter) {
        this.letter = letter;
    }

    public static GuessMessage parse(String letterLine) {
        if (letterLine == null) throw new IllegalArgumentException("Missing letter");
        String s = letterLine.trim();
        if (s.length() != 1) throw new IllegalArgumentException("Must be 1 char");
        char c = s.charAt(0);
        if (c < 'a' || c > 'z') throw new IllegalArgumentException("Must be lowercase a-z");
        return new GuessMessage(c);
    }
}
