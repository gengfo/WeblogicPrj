<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Room Types</td>
  </tr>
  <tr>
    <td class="page-text">The <c:out value="${selectRoomTypeForm.property.description}"/> has the following room types:</td>
  </tr>

  <tr>
  <td>
  <table width="100%" cellspacing="0" cellpadding="3" border="0">
  <c:forEach var="availrate" items="${selectRoomTypeForm.availRates}">
    <c:set var="roomtype" value="${availrate.roomType}"/>
    <tr>
      <td width="30%" align="left" class="table-header">
        <c:out value="${roomtype.description}"/>
      </td>
      <td width="25%" class="table-data">
        <c:if test="${roomtype.smokingFlag}">Smoking</c:if>
        <c:if test="${not roomtype.smokingFlag}">Non-Smoking</c:if>
      </td>
      <td width="25%" class="table-data">
        <c:out value="${roomtype.maximumAdults}"/>&nbsp;Adults Max
      </td>
      <c:if test="${empty availrate.blockingDates}">
        <td width="20%" align="center">  
          <form method="post" action="<c:url value='/selectroomtype.do'/>"> 
            <input type="hidden" name="id" value="<c:out value='${roomtype.externalIdentity}'/>"/>
            <input type="image" src="<c:url value='/images/selectbutton.gif'/>" border="0"/>
          </form>
        </td>
      </c:if>
      <c:if test="${not empty availrate.blockingDates}">
        <td class="table-header" width="20%" align="center">  
          Unavailable
        </td>
      </c:if>
    </tr>
    <tr>
      <td colspan="3" class="table-data">
        <c:out value="${roomtype.features}" escapeXml="false"/>
      </td>
    </tr>
    <c:forEach var="ratedetail" items="${availrate.rates}"> 
      <tr>
          <td colspan="3" class="table-data">
            &nbsp;&nbsp;Rate:&nbsp;$<c:out value="${ratedetail.price.amount}"/>/night&nbsp;for&nbsp;<c:out value="${ratedetail.numberOfNights}"/>&nbsp;nts
          </td> 
      </tr>
    </c:forEach>
    <c:forEach var="blocker" items="${availrate.blockingDates}">
      <tr>
          <td colspan="3" class="table-data">
            &nbsp;&nbsp;Not Available on <f:formatDate value="${blocker}" pattern="MM/dd/yyyy"/>
          </td> 
      </tr>
    </c:forEach>
    <tr><td>&nbsp;</td></tr>
  </c:forEach>

  </table>
  </td>
  </tr>

</table>

