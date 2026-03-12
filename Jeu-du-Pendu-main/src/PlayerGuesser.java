import java.io.*;
import java.net.*;

public class PlayerGuesser {
    private static final int PORT = 2025;

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: java PlayerGuesser <GameMasterIP> <PlayerDisplayIP> <PlayerDisplayPort>");
            System.exit(1);
        }

        String gmIp = args[0].trim();
        String displayIp = args[1].trim();
        int displayPort = Integer.parseInt(args[2]);

        // HELLO at startup
        sendHello(gmIp, displayIp, displayPort);

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Enter one letter (or _ to quit): ");
            String line = stdin.readLine();
            if (line == null) break;

            line = line.trim();

            if (line.equals("_")) {
                System.out.println("Quitting PlayerGuesser.");
                break;
            }

            if (line.length() != 1 || line.charAt(0) < 'a' || line.charAt(0) > 'z') {
                System.out.println("Error: enter exactly one lowercase letter a-z.");
                continue;
            }

            try {
                sendGuess(gmIp, line.charAt(0));
            } catch (Exception e) {
                System.out.println("Network error sending GUESS: " + e.getMessage());
            }
        }
    }

    private static void sendHello(String gmIp, String displayIp, int displayPort) throws IOException {
        try (Socket s = new Socket(gmIp, PORT)) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
            out.println("HELLO");
            out.println(displayIp);
            out.println(displayPort);
        }
    }

    private static void sendGuess(String gmIp, char letter) throws IOException {
        try (Socket s = new Socket(gmIp, PORT)) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
            out.println("GUESS");
            out.println(letter);
        }
    }
}
