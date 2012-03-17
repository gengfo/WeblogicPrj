
<html>
<body>
<H1>SSL Client</H2>

<H2>Response from domainB.bizreg.com</H2>
<%


java.net.URL url = new java.net.URL("https://domainB.bizreg.com:9012/httpsSampleWebApp/index.jsp");
weblogic.net.http.HttpsURLConnection sslConn =
new weblogic.net.http.HttpsURLConnection(url);
sslConn.connect();

java.io.InputStream in = sslConn.getInputStream();

int c;

while ((c=in.read())!=-1) {
	
	response.getWriter().print((char)c);
	
}


%>

<hr>
If domainB is configured for two-way SSL, then the server will use its own certificate (domainA) as the client certificate.