package model;

import java.util.Set;
import java.util.TreeSet;

public class GameState {
    private final String secret;               // lowercase, no accents
    private final Set<Character> guessed = new TreeSet<>();
    private int errors = 0;
    private final int maxErrors = 8;
    private GameStatus status = GameStatus.PLAYING;

    public GameState(String secret) {
        this.secret = secret;
    }

    public GameStatus getStatus() { return status; }
    public int getErrors() { return errors; }

    public boolean alreadyGuessed(char c) {
        return guessed.contains(c);
    }

    public Set<Character> getGuessed() {
        return guessed;
    }

    public boolean applyGuess(char c) {
        // returns true if state changed, false if ignored
        if (status != GameStatus.PLAYING) return false;
        if (c < 'a' || c > 'z') return false;
        if (guessed.contains(c)) return false;

        guessed.add(c);

        if (secret.indexOf(c) < 0) {
            errors++;
        }

        recomputeStatus();
        return true;
    }

    private void recomputeStatus() {
        boolean allFound = true;
        for (int i = 0; i < secret.length(); i++) {
            char s = secret.charAt(i);
            if (s >= 'a' && s <= 'z' && !guessed.contains(s)) {
                allFound = false;
                break;
            }
        }

        if (allFound) status = GameStatus.WIN;
        else if (errors >= maxErrors) status = GameStatus.LOSE;
        else status = GameStatus.PLAYING;
    }

    public String maskedWord() {
        // format like: _ _ a _ _
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < secret.length(); i++) {
            char s = secret.charAt(i);
            if (i > 0) sb.append(' ');
            if (s >= 'a' && s <= 'z' && guessed.contains(s)) sb.append(s);
            else sb.append('_');
        }
        return sb.toString();
    }

    public String guessedLettersLine() {
        // letters separated by spaces
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (char c : guessed) {
            if (!first) sb.append(' ');
            sb.append(c);
            first = false;
        }
        return sb.toString();
    }
}
