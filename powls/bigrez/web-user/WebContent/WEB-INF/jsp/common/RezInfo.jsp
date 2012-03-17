<%@ include file="Include.jspf" %>

<c:set var="rezrates" value="${rezinfo.rezRates}"/>

<table width="175" cellpadding="0" cellspacing="2" bgcolor="#EEEEEE">

  <tr>
    <td class="sidebar-header">Your Reservation</td>
  </tr>

  <tr>
    <td width="175" bgcolor="#000066"><img src="../images/space.gif" width="175" height="1"></td>
  </tr>

  <tr>
    <td>
      <span class="sidebar-title">Property:</span><br>
      &nbsp;<a class="sidebar-link" href="<c:url value='/chooseproperty.do'/>">
        <c:if test="${empty rezinfo.propertyId}">
          Choose Property
        </c:if>
        <c:if test="${not empty rezinfo.propertyId}">
          <c:out value="${rezinfo.property.description}"/>
        </c:if>
      </a>
    </td>
  </tr>

  <tr>
    <td>
      <span class="sidebar-title">Arrive:</span><br>
      &nbsp;<a class="sidebar-link" href="<c:url value='/selectdates.do'/>">
        <c:if test="${empty rezinfo.arriveDate}">
          Choose Dates
        </c:if>
        <c:if test="${not empty rezinfo.arriveDate}">
          <f:formatDate value="${rezinfo.arriveDate}" pattern="MMM dd, yyyy"/>
        </c:if>
      </a>
    </td>
  </tr>

  <tr>
    <td>
      <span class="sidebar-title">Depart:</span><br>
      &nbsp;<a class="sidebar-link" href="<c:url value='/selectdates.do'/>">
        <c:if test="${empty rezinfo.departDate}">
          Choose Dates
        </c:if>
        <c:if test="${not empty rezinfo.departDate}">
          <f:formatDate value="${rezinfo.departDate}" pattern="MMM dd, yyyy"/>
        </c:if>
      </a>
    </td>
  </tr>

  <tr>
    <td>
      <span class="sidebar-title">Room Type:</span><br>
      &nbsp;<a class="sidebar-link" href="<c:url value='/selectroomtype.do'/>">
        <c:if test="${empty rezinfo.roomTypeId}">
          Choose Room Type
        </c:if>
        <c:if test="${not empty rezinfo.roomTypeId}">
          <c:out value="${rezinfo.roomType.description}"/>
        </c:if>
      </a>
    </td>
  </tr>

  <tr>
    <td>
      <span class="sidebar-title">Rate:</span>
      <c:forEach var="rezrate" items="${rezrates}">
        <br>&nbsp;<span class="sidebar-data"><c:out value="${rezrate.numberOfNights}"/>
        nts&nbsp;@&nbsp;$<c:out value="${rezrate.price.amount}"/>/nt</span>
      </c:forEach>
    </td>
  </tr>

  <tr>
    <td>
      <span class="sidebar-title">Guest:</span><br>
      &nbsp;
        <c:if test="${empty rezinfo.guestProfileId}">
          <a class="sidebar-link" href="<c:url value='/login.do'/>">
          Please Sign In
          </a>
        </c:if>
        <c:if test="${not empty rezinfo.guestProfileId}">
          <a class="sidebar-link" href="<c:url value='/viewprofile.do'/>">
          <c:out value="${rezinfo.guestProfile.firstName}"/>&nbsp;<c:out value="${rezinfo.guestProfile.lastName}"/>
          </a>
        </c:if>
      
    </td>
  </tr>

  <tr>
    <td width="175" bgcolor="#000066"><img src="../images/space.gif" width="175" height="1"></td>
  </tr>

</table>
