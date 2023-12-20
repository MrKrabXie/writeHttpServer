package crab;
import java.io.InputStream;
import java.io.IOException;

// 请求类
public class Request {
  private InputStream input; // 输入流
  private String uri; // URI

  // 构造函数
  public Request(InputStream input) {
    this.input = input;
  }

  // 解析请求
  public void parse() {
    uri = parseUri(readFromInput());
  }

  // 从输入流中读取请求
  private String readFromInput() {
    StringBuilder request = new StringBuilder(); // 请求内容
    byte[] buffer = new byte[2048]; // 缓冲区
    int bytesRead; // 读取的字节
    try {
      bytesRead = input.read(buffer); // 读入缓冲区
      for (int i=0; i<bytesRead; i++) { // 构建请求
        request.append((char) buffer[i]);
      }
    } catch (IOException e) {
      e.printStackTrace(); // 输出错误
    }
    return request.toString(); // 返回请求内容
  }

  // 解析URI
  private String parseUri(String requestString) {
    int firstSpaceIndex = requestString.indexOf(' '); // 第一个空格的位置
    if (firstSpaceIndex != -1) {
      int secondSpaceIndex = requestString.indexOf(' ', firstSpaceIndex + 1); // 第二个空格的位置
      if (secondSpaceIndex != -1){
        return requestString.substring(firstSpaceIndex + 1, secondSpaceIndex); // 返回URI
      }
    }
    return null; // 没找到URI返回null
  }

  // 获取URI
  public String getUri() {
    return uri;
  }
}