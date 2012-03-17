<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<form:form name="guestInformationForm" method="post" commandName="guestInformationForm">

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Guest Information</td>
  </tr>
  <spring:hasBindErrors name="guestInformationForm">
  <tr><td>
    <span class="error-header1">Errors<br></span>
    <span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <c:if test="${empty guestInformationForm.profileId}">
    <tr>
      <td class="page-text">Please sign in -or- enter guest information to create a new profile:</td>
    </tr>
  </c:if>
  <c:if test="${not empty guestInformationForm.profileId}">
    <tr>
      <td class="page-text">Please confirm guest and guarantee information:</td>
    </tr>
  </c:if>
  <tr><td>&nbsp;</td></tr>
	<tr>
    <td>
      <table width="100%" cellspacing="3" cellpadding="0">

        <c:if test="${empty guestInformationForm.profileId}">
          <tr>
            <td width="35%" class="page-label" nowrap>Username:</td>
            <td>
              <form:input path="existingLogon" size="15"/>
            </td>
          </tr>

          <tr>
            <td class="page-label" nowrap>Password:</td>
            <td>
              <form:password path="existingPassword" size="15"/>
            </td>
          </tr>

          <tr>
            <td class="table-header" colspan="2" align="center">- OR -</td>
          </tr>
        </c:if>

        <tr>
          <td width="35%" class="page-label" nowrap>First Name:</td>
          <td>
            <form:input path="profile.firstName" size="25"/>
          </td>
        </tr>

        <tr>
          <td class="page-label" nowrap>Last Name:</td>
          <td>
            <form:input path="profile.lastName" size="25"/>
          </td>
        </tr>

        <tr>
          <td class="page-label" nowrap>Phone:</td>
          <td>
            <form:input path="profile.phone" size="25"/>
          </td>
        </tr>

        <tr>
          <td class="page-label" nowrap>Email Address:</td>
          <td>
            <form:input path="profile.email" size="30"/>
          </td>
        </tr>

        <tr>
          <td class="page-label" nowrap>Card Type:</td>
          <td>
            <form:select path="profile.card.type">
              <form:option value="">Choose...</form:option>
              <form:options items="${cardTypeList}" itemValue="value" itemLabel="label"/>
            </form:select>
          </td>
        </tr>

        <tr>
          <td class="page-label" nowrap>Card Expiration:</td>
          <td>
            <form:select path="profile.card.expiry">
              <form:option value="">Choose...</form:option>
              <form:options items="${cardExpList}" itemValue="value" itemLabel="label"/>
            </form:select>
          </td>
        </tr>

        <tr>
          <td class="page-label" nowrap>Card Number:</td>
          <td>
            <form:input path="profile.card.number" size="25"/>
          </td>
        </tr>

        <c:if test="${empty guestInformationForm.profileId}">
          <tr>
            <td width="35%" class="page-label" nowrap>Desired Username:</td>
            <td>
              <form:input path="desiredLogon" size="15"/>
            </td>
          </tr>

          <tr>
            <td class="page-label" nowrap>Desired Password:</td>
            <td>
              <form:password path="desiredPassword" size="15"/>
            </td>
          </tr>
        </c:if>

        <tr>
          <td colspan="2" align="center"><input type="submit" value="Continue"></td>
        </tr>
      </table>
    </td>
	</tr>

</table>

</form:form>
