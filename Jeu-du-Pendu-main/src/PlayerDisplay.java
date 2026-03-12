import java.io.*;
import java.net.*;

public class PlayerDisplay {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java PlayerDisplay <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        if (port == 2025) {
            System.err.println("Port must not be 2025");
            System.exit(1);
        }

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("PlayerDisplay listening on port " + port);

            while (true) {
                try (Socket sock = server.accept()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                    String type = in.readLine();
                    if (type == null || !type.trim().equals("DISPLAY")) continue;

                    String masked = in.readLine();
                    String proposed = in.readLine();
                    String errors = in.readLine();
                    String status = in.readLine();

                    System.out.println("----- HANGMAN -----");
                    System.out.println("Word:     " + masked);
                    System.out.println("Letters:  " + (proposed == null ? "" : proposed));
                    System.out.println("Errors:   " + errors + "/8");
                    System.out.println("Status:   " + status);
                    System.out.println("-------------------");

                    if ("WIN".equals(status) || "LOSE".equals(status)) {
                        System.out.println("Game ended: " + status);
                        break;
                    }
                } catch (Exception e) {
                    System.err.println("Display error: " + e.getMessage());
                }
            }
        }
    }
}
