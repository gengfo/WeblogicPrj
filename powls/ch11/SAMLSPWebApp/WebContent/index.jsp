<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<H1>Domain B - SAML Service Provider (SP) Example</H1>

<%
	
	String host = request.getHeader("Host");

	if (host.toLowerCase().indexOf("domainb.bigrez.com")==-1) {
		
		response.getWriter().println("<h2>WARNING: This sample does not work properly if you access it with a hostname other than domainb.bizreg.com</h2>");
		
	}

%>

When you click on a <A HREF="protected/index.jsp">protected</A> resource,
you will be redirected back to the IdentityProvider (IdP) running on domainA.<BR>
When prompted, login as <i>someotheruser</i>.  
<P>
<B>Make sure that you've updated your /etc/hosts (Unix) or c:\windows\system32\drivers\etc\hosts (Windows) to have <I>domainA.bigrez.com</I>
resolve to localhost</B>
</P>