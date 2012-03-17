<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Home Page</td>
  </tr>
  <tr>
    <td class="page-label">Welcome to bigrez.com!</td>
  </tr>
  <tr>
    <c:if test="${empty rezinfo.guestProfileId}">
        <td class="page-text">Please begin the reservation process by choosing a property
            <a class="sidebar-link" href="<c:url value='/chooseproperty.do'/>">here</a> or signing in
            <a class="sidebar-link" href="<c:url value='/login.do'/>">here</a>.
        </td>
    </c:if>
    <c:if test="${not empty rezinfo.guestProfileId}">
        <td class="page-text"><c:out value="${rezinfo.guestProfile.firstName}"/>,
            please begin the reservation process by choosing a property
            <a class="sidebar-link" href="<c:url value='/chooseproperty.do'/>">here</a>.
        </td>
    </c:if>
  </tr>

</table>
