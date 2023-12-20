package crab;
import java.io.*;
public class Response {
  private static final int BUFFER_SIZE = 1024;  //缓冲区大小
  private static final String CONTENT_TYPE = "Content-Type: text/html\r\n";  //内容类型头信息
  private static final String HTTP_OK = "HTTP/1.1 200 OK\r\n";  //HTTP 200 OK响应
  private static final String HTTP_NOT_FOUND = "HTTP/1.1 404 File Not Found\r\n";  //HTTP 404 Not Found响应
  private static final String FILE_NOT_FOUND = "<h1>File Not Found</h1>";  //文件未找到的消息
  private static final int FILE_NOT_FOUND_LENGTH = FILE_NOT_FOUND.length();  //文件未找到消息的长度
  private Request request;  //用户的请求
  private OutputStream output; // 输出流

  public Response(OutputStream output) {  //构造函数
    this.output = output;  //设置输出流
  }

  public void setRequest(Request request) {  //设置用户的请求
    this.request = request;  //存储用户的请求
  }

  public void sendStaticResource() throws IOException {  //发送静态资源
    byte[] bytes = new byte[BUFFER_SIZE];  //创建缓冲区
    FileInputStream fis = null;  //文件输入流
    try {
      File file = new File(getFilePath());  //获取文件路径
      if (file.exists()) {  //如果文件存在
        output.write(getHeader(HTTP_OK, file.length()).getBytes());  //发送HTTP 200 OK头信息
        fis = new FileInputStream(file);  //创建文件输入流
        writeFileToOutput(fis, bytes);  //将文件写入到输出流
      } else {  //如果文件不存在
        output.write(getHeader(HTTP_NOT_FOUND, FILE_NOT_FOUND_LENGTH).getBytes());  //发送HTTP 404 Not Found头信息
        output.write(FILE_NOT_FOUND.getBytes());  //发送文件未找到的消息
      }
    } catch (Exception ex) {  //捕获并处理异常
      System.out.println(ex.toString());  //打印异常信息
    } finally {  //最终
      if (fis != null)
        fis.close();  //关闭文件输入流
    }
  }

  private String getFilePath() {  //获取文件路径
    return new File(HttpServer.WEB_ROOT, request.getUri()).getPath().replace("\\", "/");  //返回文件路径
  }

  private String getHeader(String httpResponse, long contentLength) {  //获取头信息
    return httpResponse +
            CONTENT_TYPE +
            "Content-Length: " + contentLength + "\r\n" +
            "\r\n";  //返回头信息
  }

  private void writeFileToOutput(FileInputStream fis, byte[] bytes) throws IOException {  //将文件写入到输出流
    int ch = fis.read(bytes, 0, BUFFER_SIZE);  //从文件输入流中读取数据
    while (ch != -1) {  //当还有数据可读时
      output.write(bytes, 0, ch);  //写入数据到输出流
      ch = fis.read(bytes, 0, BUFFER_SIZE);  //从文件输入流中读取数据
    }
  }
}