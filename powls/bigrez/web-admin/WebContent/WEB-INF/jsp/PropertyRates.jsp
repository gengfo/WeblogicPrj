<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Rates</td>
  </tr>
  <tr>
    <td>
      <table width="100%" cellspacing="0" cellpadding="3" border="0">
        <c:forEach var="roomtype" items="${propertyRatesForm.roomTypes}">
          <tr>
	        <c:url var="propertyroom" value="/propertyroom.do">
	          <c:param name="id" value="${roomtype.externalIdentity}"/>
	        </c:url>
	        <td align="left">
	          <a class="table-link" href="<c:out value='${propertyroom}'/>">
	            <c:out value="${roomtype.description}"/>
	          </a>
	        </td>
          </tr>
          <tr>
            <td class="table-data">
              <c:out value="${roomtype.features}"/>
            </td>
          </tr>
          <c:set var="id" value="${roomtype.externalIdentity}"/>
          <c:forEach var="rate" items="${propertyRatesForm.ratesByRoomType[id]}">
            <tr>
	          <c:url var="propertyrateurl" value="/propertyrate.do">
	            <c:param name="roomId" value="${roomtype.externalIdentity}"/>
	            <c:param name="id" value="${rate.externalIdentity}"/>
	          </c:url>
                <td><a class="table-link" href="<c:out value='${propertyrateurl}'/>">
                  &nbsp;&nbsp;$<c:out value='${rate.price.amount}'/>/night from 
                    <f:formatDate value="${rate.startDate}" pattern="MM/dd/yyyy"/> to
                    <f:formatDate value="${rate.endDate}" pattern="MM/dd/yyyy"/></A>
                </td> 
            </tr>
          </c:forEach>
	      <c:url var="newpropertyrateurl" value="/propertyrate.do">
	        <c:param name="roomId" value="${roomtype.externalIdentity}"/>
	        <c:param name="id" value=""/>
	      </c:url>
          <tr>
            <td>&nbsp;&nbsp;<a class="table-link" href="<c:out value='${newpropertyrateurl}'/>">Create New Rate</a></td>
          </tr>
          <tr><td>&nbsp;</td></tr>
        </c:forEach>
      </table>
    </td>
  </tr>
</table>

