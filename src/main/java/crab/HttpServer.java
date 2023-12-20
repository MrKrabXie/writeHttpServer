package crab;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
public class HttpServer {
  public static final String WEB_ROOT =
          System.getProperty("user.dir") + File.separator  + "webroot";
  private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
  private boolean serverShutdownRequested = false; // 重命名变量
  public static void main(String[] args) {
    HttpServer server = new HttpServer();
    server.await();
  }
  public void await() {
    int port = 8080;
    try (ServerSocket serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"))) {
      waitAndProcessRequestUntilShutdown(serverSocket);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
  // 提取出来的方法，等待并处理请求直到收到关闭指令
  private void waitAndProcessRequestUntilShutdown(ServerSocket serverSocket) {
    while (!serverShutdownRequested) {
      processRequestWithExceptionHandler(serverSocket);
    }
  }
  // 处理异常的请求
  private void processRequestWithExceptionHandler(ServerSocket serverSocket) {
    try {
      processClientRequest(serverSocket);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  // 提取出的方法，处理客户端的请求
  private void processClientRequest(ServerSocket serverSocket) throws IOException {
    try (Socket socket = serverSocket.accept();
         InputStream input = socket.getInputStream();
         OutputStream output = socket.getOutputStream()) {

      // 创建 Request 对象并解析
      Request request = new Request(input);
      request.parse();

      // 创建 Response 对象
      Response response = new Response(output);
      response.setRequest(request);
      response.sendStaticResource();

      // 检查如果上一个 URI 是一个关闭指令
      serverShutdownRequested = request.getUri().equals(SHUTDOWN_COMMAND);
    }
  }
}
