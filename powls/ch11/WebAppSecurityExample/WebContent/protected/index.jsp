<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<H1>Protected Resources</H1>

Welcome <%= request.getUserPrincipal() %>

<H2>Run As Example</H2>
<A HREF="runas">This link</A> executes a servlet as another user.
<H2>Special Resources</H2>
<P>
Special Resources are protected by a separate role called <I>SpecialRole</I>.  
</P>
<P>
Only users in this role will be able to access <A HREF="special/index.jsp">this page</A>
</P>
<H2>EJB Client</H2>
<p>
This example shows how security on EJBs varies based on the Security Mode.  In the deployment descriptor the <I>secureHello()</I>
method is restricted to users in the <i>SecureUsers</i> role.  The <i>someotheruser</i> is assigned to this role.
</P>
<ul>
<li>When the domain is deployed with the realm in security mode <i>DDOnly</i>, then only the <i>someotheruser</i> can access the method.
<li>When the domain is deployed with the realm in security mode <i>CustomRoles</i>, the user <i>someuser</i> is assigned to the role <i>SecureUsers</i>, so they can get access.
<li>When the domain is deployed with the realm in security mode <i>CustomRolesAndPolicies</i>, both <i>someuser</i> and <i>someotheruser</i> can access the EJB, but only if the value of the amount is &lt; 100.
</ul>

<table>
<form method="GET" action="ejbclient">

	<tr>
	<td><B>In:</B></td><td><input type="text" name="in"></td>
	</tr>
	<tr>
	<td>
	<B>Amount:</B></td><td><input type="text" name="amount">
	</td></tr>
	<tr>
	<td colspan="2">
	<input type="submit" value="Test EJB Client">
	</tr>
</form>
</table>

