
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="reservation" value="${sessionScope.finalreservation}"/>
<c:set var="reservationRates" value="${reservation.reservationRates}"/>
<c:set var="property" value="${reservation.roomType.property}"/>
<c:set var="profile" value="${reservation.guestProfile}"/>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Thank You!</td>
  </tr>
  <tr>
    <td class="page-text">Thank you for your reservation!</td>
  </tr>

  <%@ include file="/WEB-INF/jsp/common/ViewPropertyBlock.jspf" %>

  <%@ include file="/WEB-INF/jsp/common/ViewReservationBlock.jspf" %>

</table>


