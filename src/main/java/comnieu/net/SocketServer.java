// src/main/java/comnieu/net/SocketServer.java
package comnieu.net;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SocketServer {
    private final int port;
    private ServerSocket serverSocket;
    private Thread acceptThread;
    private final List<Socket> clients = new CopyOnWriteArrayList<>();
    private final Gson gson = new Gson();
    private final List<Consumer<Message>> listeners = new CopyOnWriteArrayList<>();

    public SocketServer(int port) { this.port = port; }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        acceptThread = new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket s = serverSocket.accept();
                    clients.add(s);
                    new Thread(() -> listenClient(s)).start();
                }
            } catch (IOException ignored) {}
        }, "Server-Accept");
        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    public void stop() {
        try { serverSocket.close(); } catch (Exception ignored) {}
        for (Socket c : clients) try { c.close(); } catch (Exception ignored) {}
        clients.clear();
    }

    private void listenClient(Socket s) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                Message msg = gson.fromJson(line, Message.class);
                for (Consumer<Message> l : listeners) l.accept(msg);
            }
        } catch (IOException ignored) {
        } finally {
            clients.remove(s);
            try { s.close(); } catch (IOException ignored) {}
        }
    }

    // Cho UI (BillJDialog) đăng ký nhận message
    public void onMessage(Consumer<Message> listener) {
        listeners.add(listener);
    }
}
