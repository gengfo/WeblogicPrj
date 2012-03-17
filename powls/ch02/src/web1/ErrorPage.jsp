<%@ page isErrorPage="true" %>
<%@ page import="java.io.*, java.util.*" %>
<html>
<head><title>Error During Processing</title></head>
<body>
<h2>An error has occurred during the processing of your request.</h2>
<hr>
<h3><%= exception %></h3>
<pre>
<%
    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
    exception.printStackTrace(new PrintStream(ostr));
    out.print(ostr);
%>
</pre>
<hr>
<h3>Requested URL</h3>
<pre>
<%= HttpUtils.getRequestURL(request) %>
</pre>

<h3>Request Parameters</h3>
<pre>
<%
Enumeration ee = request.getParameterNames();
while(ee.hasMoreElements()){
  String key = (String)ee.nextElement();
  String[] paramValues = request.getParameterValues(key);
  for(int i=0;i < paramValues.length;i++){
      out.println(key + " : "  + paramValues[i]); 
  }
}
%>
</pre>

<h3>Request Attributes</h3>
<pre>
<%
ee = request.getAttributeNames();
while(ee.hasMoreElements()){
  String key = (String)ee.nextElement();
  String value = request.getAttribute(key).toString();
  out.println(key + " : "  + value); 
}
%>
</pre>

<h3>Request Information</h3>
<pre>
Request Method: <%= request.getMethod() %>
Request URI: <%= request.getRequestURI() %>
Request Protocol: <%= request.getProtocol() %>
Servlet Path: <%= request.getServletPath() %>
Path Info: <%= request.getPathInfo() %>
Path Translated: <%= request.getPathTranslated() %>
Query String: <%= request.getQueryString() %>
Content Length: <%= request.getContentLength() %>
Content Type: <%= request.getContentType() %>
Server Name: <%= request.getServerName() %>
Server Port: <%= request.getServerPort() %>
Remote User: <%= request.getRemoteUser() %>
Remote Address: <%= request.getRemoteAddr() %>
Remote Host: <%= request.getRemoteHost() %>
Authorization Scheme: <%= request.getAuthType() %>
</pre>

<h3>Request Headers</h3>
<pre>
<%
ee = request.getHeaderNames();
while (ee.hasMoreElements()) {
  String name = (String)ee.nextElement();
  out.println(name + ": " + request.getHeader(name));
}
%>
</pre>

<hr>
</body>
</html>
