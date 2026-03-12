import model.*;
import protocol.*;

import java.io.*;
import java.net.*;

public class GameMaster {
    private static final int PORT = 2025;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java GameMaster <secret_word>");
            System.exit(1);
        }

        String secret = args[0].trim();
        GameState state = new GameState(secret);
        KnownDisplays known = new KnownDisplays();

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("GameMaster listening on " + PORT);

            while (state.getStatus() == GameStatus.PLAYING) {
                try (Socket sock = server.accept()) {
                    handleOneClient(sock, state, known);
                } catch (Exception e) {
                    // must be robust: ignore and continue
                    System.err.println("Invalid/failed client msg: " + e.getMessage());
                }
            }

            // game ended: send final state once more (optional but useful)
            broadcast(state, known);
            System.out.println("Game finished: " + state.getStatus());
        }
    }

    private static void handleOneClient(Socket sock, GameState state, KnownDisplays known) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        String type = in.readLine();
        if (type == null) return;

        type = type.trim();

        if (type.equals("HELLO")) {
            String ip = in.readLine();
            String port = in.readLine();
            HelloMessage msg = HelloMessage.parse(ip, port);
            known.add(msg.toTarget());
            // immediately send current state to newly registered displays
            sendDisplayTo(msg.toTarget(), state);
            System.out.println("Registered PlayerDisplay: " + msg.ip + ":" + msg.port);

        } else if (type.equals("GUESS")) {
            String letterLine = in.readLine();
            GuessMessage msg = GuessMessage.parse(letterLine);

            boolean changed = state.applyGuess(msg.letter);
            if (changed) {
                broadcast(state, known);
            }

        } else {
            // ignore malformed message type
        }
    }

    private static void broadcast(GameState state, KnownDisplays known) {
        for (DisplayTarget d : known.all()) {
            try {
                sendDisplayTo(d, state);
            } catch (Exception e) {
                // IMPORTANT: do not remove from list
                System.err.println("Display unreachable " + d.ip() + ":" + d.port() + " (" + e.getMessage() + ")");
            }
        }
    }

    private static void sendDisplayTo(DisplayTarget d, GameState state) throws IOException {
        SocketAddress addr = new InetSocketAddress(d.ip(), d.port());
        try (Socket s = new Socket()) {
            // connect timeout = 1 second (spec requirement)
            s.connect(addr, 1000);

            PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
            out.println("DISPLAY");
            out.println(state.maskedWord());
            out.println(state.guessedLettersLine());
            out.println(state.getErrors());
            out.println(state.getStatus().name());
        }
    }
}
