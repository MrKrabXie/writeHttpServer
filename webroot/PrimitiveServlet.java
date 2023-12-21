import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

// 实现了 Servlet 接口的服务器组件，可以接收和响应来自客户端的请求
public class PrimitiveServlet implements Servlet {

  // 初始化函数，在 Servlet 生命周期中只会被调用一次
  public void init(ServletConfig config) throws ServletException {
    System.out.println("初始");
  }

  // service 函数，每次接收到客户端请求时被调用
  public void service(ServletRequest request, ServletResponse response)
          throws ServletException, IOException {
    System.out.println("来自 service 函数");
    PrintWriter out = response.getWriter();
    String errorMessage = "HTTP/1.1 404 文件未找到\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: 23\r\n" +
            "\r\n" +
            "<p>hi rose.<\\p>\r\n" +
            "<p>it is blue..<\\p>";
    out.println(errorMessage);
  }

  // 销毁函数，在 Servlet 生命周期结束时被调用
  public void destroy() {
    System.out.println("销毁");
  }

  // 返回 Servlet 的信息，此方法可选
  public String getServletInfo() {
    return null;
  }

  // 返回 ServletConfig 对象，它包含了 Servlet 的初始化和启动配置
  public ServletConfig getServletConfig() {
    return null;
  }
}
