// src/main/java/comnieu/net/SocketClient.java
package comnieu.net;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class SocketClient implements Closeable {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private final Gson gson = new Gson();

    public SocketClient(String host, int port) {
        this.host = host; this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    public void send(Message msg) {
        if (out != null) out.println(gson.toJson(msg));
    }

    @Override
    public void close() throws IOException {
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        if (socket != null) socket.close();
    }
}
