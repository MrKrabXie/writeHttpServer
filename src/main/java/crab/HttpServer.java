package pyrmont;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;



// ... import statements ...

public class HttpServer {
  public static final String WEB_ROOT =
          System.getProperty("user.dir") + File.separator  + "webroot";

  // shutdown command
  private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

  // the shutdown command received
  private boolean shutdown = false;
  public static void main(String[] args) {
    HttpServer server = new HttpServer();
    server.await();
  }

  public void await() {
    ServerSocket serverSocket = initializeServerSocket();

    // Loop waiting for a request
    while (!shutdown) {
      try {
        processClientRequest(serverSocket);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private ServerSocket initializeServerSocket() {
    ServerSocket serverSocket = null;
    int port = 8080;
    try {
      serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return serverSocket;
  }

  private void processClientRequest(ServerSocket serverSocket) throws IOException {
    Socket socket = serverSocket.accept();
    InputStream input = socket.getInputStream();
    OutputStream output = socket.getOutputStream();

    // create Request object and parse
    Request request = new Request(input);
    request.parse();

    // create Response object
    Response response = new Response(output);
    response.setRequest(request);
    response.sendStaticResource();

    // Close the socket
    socket.close();

    //check if the previous URI is a shutdown command
    shutdown = SHUTDOWN_COMMAND.equals(request.getUri());
  }

  // ... some other code ...
}
