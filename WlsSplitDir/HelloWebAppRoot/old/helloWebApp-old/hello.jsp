<!-- Copyright (c) 1999,2010, Oracle and/or its affiliates. All Rights Reserved.-->
<!doctype html public "-//w3c/dtd HTML 4.0//en">

<%@ page import="
  javax.ejb.EJBLocalHome,
  javax.naming.*,
  javax.rmi.PortableRemoteObject,
  examples.splitdir.hello.webutils.WebAppUtils,
  examples.splitdir.hello.HelloLocal,
  examples.splitdir.hello.HelloLocalHome,
  examples.splitdir.hello.apputils.AppUtils,
  java.util.Date,
  java.io.PrintWriter"%>

<%!
  //class level decleration, all requests will share object references
  AppUtils appUtils = AppUtils.getAppUtils();
  //This does not handle any security constraints on lookup of EJB
  //This is looking up context on local machine
  public Context getInitialContext() throws NamingException {
    return new InitialContext();
  }
%>

<jsp:include page="ExamplesHeader.jsp">
  <jsp:param name="title" value="Split Directory - Hello World"/>
</jsp:include>

<p>
<%
  try {
    //These objects are scoped at the service method, new for each request
    //Look up our EJB home
    //This uses the GenericResourceLoader in APP-INF/lib to look up a property that is in a properties file that lives in APP-INF/classes
    HelloLocalHome home = (HelloLocalHome) PortableRemoteObject.narrow(getInitialContext().lookup(appUtils.getEJBJNDIName()), HelloLocalHome.class);
    //Look up our local EJB
    HelloLocal helloEjb =(HelloLocal) home.create();
%>
<p><b>You request URI </b><%= request.getRequestURI() %></p>
<p><b>Date</b> <%= new Date()%></p>
<!-- This uses a utility class whose src lives in WEB-INF/src and compiles into DEST_DIR/WEB-INF/classes -->
<p><b>Your browser is  </b> <%= WebAppUtils.getBrowserName(request) %> </p>
<p><b>Our EJB return value is </b> <%= helloEjb.sayHello() %></p>
<%
  }
  catch (Exception ex) {
    ex.printStackTrace(new PrintWriter(out));
  }
%>

<%@ include file="ExamplesFooter.jsp" %>
