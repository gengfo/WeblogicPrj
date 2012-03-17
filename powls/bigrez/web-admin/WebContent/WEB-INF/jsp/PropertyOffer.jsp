<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<script LANGUAGE="JavaScript">
  function maybeDelete() {
	  if (confirm("Are you sure you want to delete this Offer?")) {
		  document.propertyOfferForm.action.value='delete';
      document.propertyOfferForm.submit();
	  }
  }
</script>

<form:form name="propertyOfferForm" method="post" commandName="propertyOfferForm">
<input type="hidden" name="action" value=""/>

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Offer Information</td>
  </tr>
  <spring:hasBindErrors name="propertyOfferForm">
  <tr><td>
    <span class="error-header1">Errors<br></span>
    <span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <tr>
    <td>
      <table width="100%" cellspacing="5" cellpadding="0" border="0">
        <tr>
          <td width="25%" class="page-label">Caption:</td>
          <td><form:input path="offer.caption" size="40" maxlength="60"/></td>
        </tr>
        <tr>
          <td width="25%" class="page-label">Description:</td>
          <td><form:textarea path="offer.description" cols="50" rows="10"/></td>
        </tr>
        <tr>
          <td class="page-label">Image File:</td>
          <td><form:input path="offer.imageFile" size="25" maxlength="60"/></td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" align="center">
            <input type="submit" value="Save Changes">&nbsp;
            <c:if test="${not empty propertyOfferForm.id}">
              <input type="button" value="Delete Offer" onClick="javascript:maybeDelete();">
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

</form:form>
