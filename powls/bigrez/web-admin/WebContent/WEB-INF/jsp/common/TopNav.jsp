<%@ include file="Include.jspf" %>

<a href="<c:url value='/propertylist.do'/>" class="menu-text">Properties</a>

<c:if test='${empty currentProperty}'>
  <span class='disabled-link'>&nbsp;|&nbsp;Main&nbsp;|&nbsp;Rooms&nbsp;|&nbsp;Rates
  &nbsp;|&nbsp;Availability&nbsp;|&nbsp;Offers</span>
</c:if>

<c:if test='${not empty currentProperty}'>
  &nbsp;|&nbsp;
  <a href="<c:url value='/propertymain.do'/>" class="menu-text">Main</a>
  &nbsp;|&nbsp;
  <a href="<c:url value='/propertyrooms.do'/>" class="menu-text">Rooms</a>
  &nbsp;|&nbsp;
  <a href="<c:url value='/propertyrates.do'/>" class="menu-text">Rates</a>
  &nbsp;|&nbsp;
  <a href="<c:url value='/propertyavails.do'/>" class="menu-text">Availability</a>
  &nbsp;|&nbsp;
  <a href="<c:url value='/propertyoffers.do'/>" class="menu-text">Offers</a>
</c:if>
