package crab2;
import java.io.File;

public class Constants {

  // 设置WEB_ROOT为系统当前工作目录下的webroot文件夹
  public static final String WEB_ROOT =
          System.getProperty("user.dir") + File.separator  + "webroot";
}