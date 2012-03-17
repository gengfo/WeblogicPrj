<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<H1>Web Application Security Example</H1>

<p>
The purpose of this application is to demonstrate some of the security
features discussed in the chapter.
</p> 
<h2>User Data Constraint</h2>
<A href="confidential/index.jsp"/>This link</A> can only be accessed over SSL.

<h2>Auth Constraint</h2>
<p>
<A href="protected/index.jsp"/>This link</A> can only be accessed by authorized users.
</p>
The sample domain comes configured with 3 different realms.
<ul>  
<li>The default realm called myrealm is configured with DDOnly security model.  This means the definition of roles and policies are
contained inside of the deployment descriptor.  When this application is deployed when the server is running with this realm as its 
active realm, all users can access the <A href="protected/index.jsp"/>/protected</A> resource, but only the user <I>someotheruser</I> 
can access the <A href="protected/special/index.jsp"/>/protected/special</A> resource, because the <I>someotheruser</I> is in the
<I>SpecialRole</I> in the deployment descriptor.
<li>The CustomRolesRealm is configured with the CustomRoles security model.  This means role membership is configured from the 
WebLogic Console, but the policy (which roles can access which resources) is defined inside of the deployment descriptor.
When this application is deployed when the server is running with this realm as its 
active realm, both users can access the <A href="protected/index.jsp"/>/protected</A> resource, but only the user <I>someuser</I> 
can access the <A href="protected/special/index.jsp"/>/protected/special</A> resource, because the <I>someuser</I> is in the
<I>SpecialRole</I> in the WebLogic Console.
<li>The CustomRolesAndPoliciesRealm is configures with the CustomRolesAndPolicies security model.  This means that roles and polcies are
managed from the WebLogic Console, and the information in the deployment descriptor is ignored. When this application is deployed when the server is running with this realm as its 
active realm, all users in the <I>Ch11ProtectedUser</I> group can access the <A href="protected/index.jsp"/>/protected</A> resource, and both the <I>someotheruser</I> and <I>someuser</I> 
can access the <A href="protected/special/index.jsp"/>/protected/special</A> resource, because that's the policy in the WebLogic Console.
Neither of these users are in the <I>SpecialRole</I> 
</ul>
<B>To try a different user, close your browser and log-in again</B>
<h2>WebLogic SSL Client</h2>
This <A href="ssl.jsp">link</A> makes a call over SSL to domainB.bizreg.com


