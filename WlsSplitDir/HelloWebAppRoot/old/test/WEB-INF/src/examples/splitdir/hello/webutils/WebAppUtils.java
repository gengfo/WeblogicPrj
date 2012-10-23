package examples.splitdir.hello.webutils;

import javax.servlet.http.HttpServletRequest;


/**
 * WebApppUtils is a utility class that will be used by just this single webapp
 *
 * If it was used by more than one webapp the source would live at 
 * helloWorldEar/webutils/examples/splitdir/hello/webutils
 * and would be automatically compiled into APP-INF/classes
 *
 * This is not a complete utility, you can imagine also wanting to get the 
 * version number of the browser.
 *
 * @author Copyright (c) 2010 by BEA Systems, Inc. All Rights Reserved.
 */

public class WebAppUtils {
  
  private final static String USER_AGENT = "User-Agent";
  //User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)
  //User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.0.1) Gecko/20020823 Netscape/7.0
  //User-Agent: Mozilla/4.7 [en] (WinNT; I)

  private final static String IE_INDEX = "MSIE";
  private final static String NES_INDEX = "Mozilla";

  public final static int INTERNET_EXPLORER = 3;
  public final static int NETSCAPE = 1;

  /**
   * Not an exhaustive list by any means
   */
  private static int getBrowserType(HttpServletRequest req) {
    String tempH = getHeader(req, USER_AGENT);
    if(tempH != null && tempH.indexOf(IE_INDEX)!= -1) {
      return 3;
    } else if(tempH != null && (tempH.indexOf(NES_INDEX) != -1 && tempH.indexOf(IE_INDEX) == -1)) {
      return 1;
    }
    return -1;
  }
  /**
   * Utility method to get the vendor name of the browser
   *
   * Again this is not a full list, just a sample.
   */

  public static String getBrowserName(HttpServletRequest req) {
    switch(getBrowserType(req)) {
    case INTERNET_EXPLORER:
      return "Internet Explorer";
    case NETSCAPE:
      return "Netscape";
    default:
      return "Unknown";
    }
  }

  private static String getHeader(HttpServletRequest req, String name) {
    return req.getHeader(name);
  }

}
