<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<form:form name="loginForm" method="post" commandName="loginForm">

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right">Please Login</td>
  </tr>
  <spring:hasBindErrors name="loginForm">
  <tr><td>
    <span class="error-header1">Errors<br></span><span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <tr>
    <td class="page-text">Please sign in to load your profile:</td>
  </tr>
  <tr><td>&nbsp;</td></tr>
	<tr>
    <td>
      <table width="100%" cellspacing="3" cellpadding="0">
        <tr>
          <td width="35%" class="page-label" nowrap>Username:</td>
          <td>
            <form:input path="logon" size="15"/>
          </td>
        </tr>
        <tr>
          <td class="page-label" nowrap>Password:</td>
          <td>
            <form:password path="password" size="15"/>
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center"><input type="submit" value="Sign In"></td>
        </tr>
      </table>
    </td>
	</tr>
</table>

</form:form>
