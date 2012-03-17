<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<h1>Domain A - SAML Identity Provider (IdP) Example</h1>
<FORM action="Login" method="POST">

	<INPUT TYPE="HIDDEN" name="returnURL" 
		value="<%= request.getParameter("returnURL") %>">
	
	
	<table>
		<tr>
			<td>Username:</td><td> <INPUT TYPE="TEXT" name="user"></td>
		</tr>
		<tr>
			<td>Password:</td><td><INPUT TYPE="PASSWORD" name="password"></td>
		</tr>
		<tr>
			<td colspan="2"><INPUT TYPE="Submit" name="LogIn" value="LogIn"></td>
		</tr>
	</table>
</FORM>
