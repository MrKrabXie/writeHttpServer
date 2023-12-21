package crab2;
import java.io.IOException;
public class StaticResourceProcessor {
  // 处理方法，接受请求和响应为参数
  public void process(Request request, Response response) {
    try {
      // 响应发送静态资源
      response.sendStaticResource();
    }
    // 捕获并打印可能出现的IOException异常
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}