package crab2;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor1 {
  public void process(Request request, Response response) {
    // 获取请求的uri
    String uri = request.getUri();
    // 取得uri中"/"后面的字符串作为servletName
    String servletName = uri.substring(uri.lastIndexOf("/") + 1);
    URLClassLoader loader = null;

    try {
      // 创建一个URLClassLoader
      URL[] urls = new URL[1];
      URLStreamHandler streamHandler = null;
      File classPath = new File(Constants.WEB_ROOT);
      // 创建仓库的url
      // 这个url创建方法参考了org.apache.catalina.startup.ClassLoaderFactory类中的createClassLoader方法
      String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
      // 生成URL的方法取自org.apache.catalina.loader.StandardClassLoader类中的addRepository方法
      urls[0] = new URL(null, repository, streamHandler);
      loader = new URLClassLoader(urls);
    } catch (IOException e) {
      System.out.println(e.toString());
    }

    // 加载servlet类
    Class myClass = null;
    try {
      myClass = loader.loadClass(servletName);
    } catch (ClassNotFoundException e) {
      System.out.println(e.toString());
    }

    Servlet servlet = null;
    try {
      // 创建servlet实例
      servlet = (Servlet) myClass.newInstance();
      // 在服务请求和响应上调用servlet
      servlet.service((ServletRequest) request, (ServletResponse) response);
    } catch (Exception e) {
      System.out.println(e.toString());
    } catch (Throwable e) {
      System.out.println(e.toString());
    }
  }
}