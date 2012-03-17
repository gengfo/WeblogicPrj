<%@ include file="Include.jspf" %>

<a href="<c:url value='/home.do'/>" class="menu-text">Home</a>
&nbsp;|&nbsp;
<a href="<c:url value='/chooseproperty.do'/>" class="menu-text">Make a Reservation</a>
&nbsp;|&nbsp;
<a href="<c:url value='/clearreservation.do'/>" class="menu-text">Clear Reservation</a>
&nbsp;|&nbsp;

<c:if test="${empty rezinfo.guestProfileId}">
  <a href="<c:url value='/login.do'/>" class="menu-text">Sign In</a>
</c:if>

<c:if test="${not empty rezinfo.guestProfileId}">
  <!-- Sorry, we haven't implemented change reservation... -->
  <a href="<c:url value='/home.do'/>" class="menu-text">Change a Reservation</a>
  &nbsp;|&nbsp;
  <a href="<c:url value='/viewprofile.do'/>" class="menu-text">My Profile</a>
  &nbsp;|&nbsp;
  <a href="<c:url value='/logout.do'/>" class="menu-text">Sign Out</a>
</c:if>
