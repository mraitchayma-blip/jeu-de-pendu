package protocol;

import model.DisplayTarget;

public class HelloMessage {
    public final String ip;
    public final int port;

    private HelloMessage(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static HelloMessage parse(String ipLine, String portLine) throws IllegalArgumentException {
        if (ipLine == null || ipLine.trim().isEmpty())
            throw new IllegalArgumentException("IP empty");

        int port;
        try {
            port = Integer.parseInt(portLine.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Port not int");
        }

        if (port == 2025) throw new IllegalArgumentException("Port must not be 2025");
        if (port <= 0 || port > 65535) throw new IllegalArgumentException("Port range");

        return new HelloMessage(ipLine.trim(), port);
    }

    public DisplayTarget toTarget() {
        return new DisplayTarget(ip, port);
    }
}
