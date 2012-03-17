<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<form:form action="/admin/propertymain.do" method="post" commandName="propertyMainForm">

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Property Information</td>
  </tr>
  <spring:hasBindErrors name="propertyMainForm">
  <tr><td>
    <span class="error-header1">Errors<br></span>
    <span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <c:if test="${param.success}">
    <tr><td><span class="error-header2">&nbsp;Property was updated successfully.</span></td></tr>
  </c:if>
  <tr>
  <td>
  <table width="100%" cellspacing="5" cellpadding="0" border="0">
    <tr>
      <td width="25%" class="page-label">Description:</td>
      <td><form:input path="description" size="40" maxlength="60"/></td>
    </tr>
    <tr>
      <td class="page-label">Address:</td>
      <td><form:input path="address1" size="40" maxlength="60"/></td>
    </tr>
    <tr>
      <td class="page-label">&nbsp;</td>
      <td><form:input path="address2" size="40" maxlength="60"/></td>
    </tr>
    <tr>
      <td class="page-label">City:</td>
      <td><form:input path="city" size="25" maxlength="30"/></td>
    </tr>
    <tr>
      <td class="page-label">State Code:</td>
      <td><form:input path="stateCode" size="5" maxlength="2"/></td>
    </tr>
    <tr>
      <td class="page-label">Postal Code:</td>
      <td><form:input path="postalCode" size="10" maxlength="10"/></td>
    </tr>
    <tr>
      <td class="page-label">Phone:</td>
      <td><form:input path="phone" size="25" maxlength="30"/></td>
    </tr>
    <tr>
      <td class="page-label">Image File:</td>
      <td><form:input path="imageFile" size="25" maxlength="100"/></td>
    </tr>
    <tr>
      <td class="page-label">Features:</td>
      <td><form:textarea path="features" cols="50" rows="10"/></td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan="2" align="center">
        <input type="submit" value="Save Changes">
      </td>
    </tr>

  </table>
  </td>
  </tr>

</table>

</form:form>
