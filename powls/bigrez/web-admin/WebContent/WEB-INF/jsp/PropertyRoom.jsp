<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<script LANGUAGE="JavaScript">
  function maybeDelete() {
	  if (confirm("Are you sure you want to delete this Room Type?")) {
		  document.propertyRoomForm.action.value='delete';
      document.propertyRoomForm.submit();
	  }
  }
</script>

<form:form name="propertyRoomForm" method="post" commandName="propertyRoomForm">
<input type="hidden" name="action" value=""/>

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Room Type Information</td>
  </tr>
  <spring:hasBindErrors name="propertyRoomForm">
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
          <td width="25%" class="page-label">Description:</td>
          <td><form:input path="roomType.description" size="40" maxlength="60"/></td>
        </tr>
        <tr>
          <td class="page-label">Smoking Allowed:</td>
          <td><form:checkbox path="roomType.smokingFlag"/></td>
        </tr>
        <tr>
          <td class="page-label">Max Adults:</td>
          <td><form:input path="roomType.maximumAdults" size="5" maxlength="3"/></td>
        </tr>
        <tr>
          <td class="page-label">Number of Rooms:</td>
          <td><form:input path="roomType.numberOfRooms" size="10" maxlength="5"/></td>
        </tr>
        <tr>
          <td class="page-label">Features:</td>
          <td><form:textarea path="roomType.features" cols="50" rows="10"/></td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" align="center">
            <input type="submit" value="Save Changes">&nbsp;
            <c:if test="${not empty propertyRoomForm.id}">
              <input type="button" value="Delete Room Type" onClick="javascript:maybeDelete();">
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

</form:form>
