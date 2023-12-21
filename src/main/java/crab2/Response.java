package crab2;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;
public class Response implements ServletResponse {
  private static final int BUFFER_SIZE = 1024;
  Request request;
  OutputStream output;
  PrintWriter writer;

  private static final String CONTENT_TYPE = "Content-Type: text/html\r\n";  // 内容类型头信息
  private static final String HTTP_OK = "HTTP/1.1 200 OK\r\n";  // HTTP 200 OK 响应
  private String getHeader(String httpResponse, long contentLength) {  // 获取头信息
    return httpResponse +
            CONTENT_TYPE +
            "Content-Length: " + contentLength + "\r\n" +
            "\r\n";  // 返回头信息
  }

  public Response(OutputStream output) {
    this.output = output;
  }
  public void setRequest(Request request) {
    this.request = request;
  }
  /* 这个方法用来服务一个静态页面 */
  public void sendStaticResource() throws IOException {
    byte[] bytes = new byte[BUFFER_SIZE];
    FileInputStream fis = null;
    try {
      /* request.getUri has been replaced by request.getRequestURI */
      File file = new File(Constants.WEB_ROOT, request.getUri());
      fis = new FileInputStream(file);
      /*
         HTTP 响应 = 状态行
          *(( 通用头部 | 响应头部 | 实体头部 ) CRLF)
          CRLF
          [ 消息体 ]
         状态行 = HTTP-Version SP 状态码 SP 原因短语 CRLF
      */
      int ch = fis.read(bytes, 0, BUFFER_SIZE);
      output.write(getHeader(HTTP_OK, file.length()).getBytes());
      while (ch != -1) {
        output.write(bytes, 0, ch);
        ch = fis.read(bytes, 0, BUFFER_SIZE);
      }
    }
    catch (FileNotFoundException e) {
      String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
              "Content-Type: text/html\r\n" +
              "Content-Length: 23\r\n" +
              "\r\n" +
              "<h1>File Not Found</h1>";
      output.write(errorMessage.getBytes());
    }
    finally {
      if (fis != null)
        fis.close();
    }
  }

  /** 实现 ServletResponse 的方法 */
  public void flushBuffer() throws IOException {
  }
  public int getBufferSize() {
    return 0;
  }
  public String getCharacterEncoding() {
    return null;
  }
  @Override
  public String getContentType() {
    return null;
  }
  public Locale getLocale() {
    return null;
  }
  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }
  public PrintWriter getWriter() throws IOException {
    //手动刷新是 true，println() 将刷新，
    //但 print() 不会。
    writer = new PrintWriter(output, true);
    return writer;
  }
  @Override
  public void setCharacterEncoding(String s) {
  }
  public boolean isCommitted() {
    return false;
  }
  public void reset() {
  }
  public void resetBuffer() {
  }
  public void setBufferSize(int size) {}

  public void setContentLength(int length) {
  }

  @Override
  public void setContentLengthLong(long l) {

  }

  public void setContentType(String type) {
  }

  public void setLocale(Locale locale) {
  }
}