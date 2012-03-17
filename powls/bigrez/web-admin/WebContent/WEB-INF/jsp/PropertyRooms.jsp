
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Room Types</td>
  </tr>

  <tr>
  <td>
  <table width="100%" cellspacing="0" cellpadding="3" border="0">
  <c:forEach var="roomtype" items="${propertyRoomsForm.roomTypes}">
    <tr>
      <c:url var="propertyroom" value="/propertyroom.do">
        <c:param name="id" value="${roomtype.externalIdentity}"/>
      </c:url>
      <td width="30%" align="left">
        <a class="table-link" href="<c:out value='${propertyroom}'/>">
          <c:out value="${roomtype.description}"/>
        </a>
      </td>
      <td width="25%" class="table-data">
        <c:if test="${roomtype.smokingFlag}">Smoking</c:if>
        <c:if test="${not roomtype.smokingFlag}">Non-Smoking</c:if>
      </td>
      <td width="25%" class="table-data">
        <c:out value="${roomtype.maximumAdults}"/>&nbsp;Adults Max
      </td>
    </tr>
    <tr>
      <td colspan="3" class="table-data">
        <c:out value="${roomtype.features}"/>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </c:forEach>

  </table>
  </td>
  </tr>

  <tr>
    <td><a class="table-link" href="<c:url value='/propertyroom.do'/>">Create New Room Type</a></td>
  </tr>

</table>

