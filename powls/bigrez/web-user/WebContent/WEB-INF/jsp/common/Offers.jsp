<%@ include file="Include.jspf" %>

<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#EEEEEE">
  <tr valign="top">
    <!-- force offer block to be at least 200 high -->
    <td width="1" bgcolor="#EEEEEE"><img src="../images/space.gif" width="1" height="200"></td>
    <td>
      <table width="100%" align="center" cellpadding="0" cellspacing="5" bgcolor="#EEEEEE">
      <c:forEach var="offer" items="${requestScope.offers}">
        <tr><td><img src="../images/space.gif" width="1" height="5"></td></tr>

        <tr align="center">
          <td><img src="<c:url value='/images/${offer.imageFile}'/>" 
               alt="<c:out value='${offer.description}'/>" width="70" height="70">
          </td>
        </tr>
        <tr align="center">
          <td>
            <c:url var="viewproperty" value="/viewproperty.do">
              <c:param name="id" value="${offer.property.externalIdentity}"/>
            </c:url>
            <a class="sidebar-link" href="<c:out value='${viewproperty}'/>">
              <c:out value='${offer.caption}'/><br>
              <c:out value='${offer.property.description}'/>            
            </a>
          </td>
        </tr>
      </c:forEach>
      </table>
    </td>
  </tr>
</table>
