package crab2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer1 {
  // 定义一个关闭命令
  private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
  // 判断是否需要关闭的标记
  private boolean shutdown = false;
  // 服务器端口
  private static final int serverPort = 8080;

  public static void main(String[] args) {
    HttpServer1 server = new HttpServer1();
    server.await(); // 开启服务器等待请求
  }

  public void await() {
    try (ServerSocket serverSocket =  new ServerSocket(serverPort, 1, InetAddress.getByName("127.0.0.1"))){
      // 循环等待请求
      while (!shutdown) {
        try {
          // 接收请求
          Socket socket = serverSocket.accept();
          // 处理客户端请求
          handleClientRequest(socket);
          // 关闭socket
          socket.close();
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void handleClientRequest(Socket socket) throws IOException {
    InputStream input = socket.getInputStream();
    OutputStream output = socket.getOutputStream();

    // 创建Request对象并解析
    Request request = new Request(input);
    request.parse();

    // 创建Response对象
    Response response = new Response(output);
    response.setRequest(request);

    // 判断请求是静态资源还是Servlet
    if (request.getUri().startsWith("/servlet/")) {
      // 处理Servlet
      ServletProcessor1 processor = new ServletProcessor1();
      processor.process(request, response);
    }
    else {
      // 处理静态资源
      StaticResourceProcessor processor = new StaticResourceProcessor();
      processor.process(request, response);
    }
    // 检查是否需要关闭服务器
    shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
  }
}