<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

 <h1>Domain B - SAML Service Provider (SP) Example</h1>
 
 Welcome
 <%= weblogic.security.Security.getCurrentSubject()%>
 
 <p>You've successfully performed SAML SSO</p>