<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Chapter 11 - HTTPS Client Example Web Application</title>
</head>
<body>
The Subject is: <%= weblogic.security.Security.getCurrentSubject() %>

<%
java.security.cert.X509Certificate [] cert = 
	(java.security.cert.X509Certificate [])request.getAttribute("javax.servlet.request.X509Certificate");

if (cert!=null) {
%>

2 way SSL Request from:
<%=

	cert[0].getSubjectDN().getName()
%>

<%} else {%>

1 way SSL Request

<% } %>

</body>
</html>