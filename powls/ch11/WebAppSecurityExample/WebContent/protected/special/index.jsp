<h1>Special Content</h1>

Welcome <%= request.getUserPrincipal() %><br>
Are you in the SpecialRole? <%= request.isUserInRole("SpecialRole") %><BR>

<P>
The user should be in the <I>SpecialRole</I>, if the application is deployed with DDOnly or CustomRoles.<BR>
When the application is deployed with CustomRolesAndPolicies, the user is not in the SpecialRole, but granted access anyway.
</p>