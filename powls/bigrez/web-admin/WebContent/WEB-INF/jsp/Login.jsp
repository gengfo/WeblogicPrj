
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Login</td>
  </tr>
  <tr>
    <td class="page-text">Please log in to Administration Site:</td>
  </tr>
  <c:if test="${not empty param.error}">
    <tr><td>&nbsp;</td></tr>
    <tr><td class="error-header2">Invalid Administrator ID or Password. Please try again.</td></tr>
  </c:if>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td>
      <form method="POST" action="j_security_check">
        <table width="50%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="50%" class="page-label">Administrator ID:</td>
            <td width="50">
              <input type="text" name="j_username" size="15" maxlength="15" value="">
            </td>
          </tr>
          <tr>
            <td class="page-label">Password:</td>
            <td>
              <input type="password" name="j_password" size="15" maxlength="15" value="">
            </td>
          </tr>
          <tr><td colspan="2">&nbsp;</td></tr>
          <tr>
            <td align="center" colspan="2">
               <input type="submit" value="Submit">
            </td>
          </tr>
        </table>
      </form>
    </td>
  </tr>
  
</table>

