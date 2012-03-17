
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<form:form name="reviewReservationForm" method="post" commandName="reviewReservationForm">

<c:set var="property" value="${reviewReservationForm.property}"/>
<c:set var="reservation" value="${reviewReservationForm.reservation}"/>
<c:set var="reservationRates" value="${reviewReservationForm.reservationRates}"/>
<c:set var="profile" value="${reservation.guestProfile}"/>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Review Reservation</td>
  </tr>
  <spring:hasBindErrors name="reviewReservationForm">
  <tr><td>
    <span class="error-header1">Errors<br></span>
    <span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <tr>
    <td class="page-text">Please review the following information and confirm your reservation!</td>
  </tr>

  <%@ include file="/WEB-INF/jsp/common/ViewPropertyBlock.jspf" %>

  <%@ include file="/WEB-INF/jsp/common/ViewReservationBlock.jspf" %>
    
  <tr>
    <td colspan="2" align="center"><input type="submit" value="Confirm Reservation"></td>
  </tr>

</table>

</form:form>

