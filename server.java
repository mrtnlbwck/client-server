import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class server {
    public static void main(String[] args) {
        ServerSocket server;
        Socket socket;
        String host = "localhost";
        PrintStream streamOut;
        BufferedReader streamIn;
        String url;

        if (args.length > 0) {
            host = args[0];
        }

        try {
            server = new ServerSocket(8080);
            int clientCount = 0;
            while (clientCount < 5) {
                System.out.println("Waiting for connection");
                socket = server.accept();
                System.out.println("Client connected");
                Thread t = new ConnectionHandler(socket);
                t.start();
                clientCount++;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static class ConnectionHandler extends Thread {
        private Socket socket;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            PrintStream streamOut = null;
            BufferedReader streamIn = null;
            String url;

            try {
                streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                streamOut = new PrintStream(socket.getOutputStream());

                while ((url = streamIn.readLine()) != null) {
                    System.out.println(url);
                    streamOut.println("<html><body>");
                    streamOut.println("<h1>Server Response</h1>");
                    streamOut.println("<p>URL: " + url + "</p>");
                    streamOut.println("<p>Date: " + new Date() + "</p>");
                    InetAddress address = socket.getLocalAddress();
                    int port = socket.getLocalPort();
                    streamOut.println("<p>Server IP Address: " + address.getHostAddress() + "</p>");
                    streamOut.println("<p>Server Port: " + port + "</p>");
                    streamOut.println("</body></html>");
                    streamOut.flush();
                    break;
                }

                socket.close();
                System.out.println("Client disconnected");
            } catch (SocketException e) {
                System.out.println("Connection lost");
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    if (streamIn != null) {
                        streamIn.close();
                    }
                    if (streamOut != null) {
                        streamOut.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }
}
