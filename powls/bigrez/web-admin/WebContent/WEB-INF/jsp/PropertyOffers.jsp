<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Offers</td>
  </tr>

  <tr>
  <td>
  <table width="100%" cellspacing="0" cellpadding="3" border="0">
  <c:forEach var="offer" items="${propertyOffersForm.offers}">
    <tr>
      <td width="100%" align="left">
	    <c:url var="propertyoffer" value="/propertyoffer.do">
	      <c:param name="id" value="${offer.externalIdentity}"/>
	    </c:url>
        <a class="table-link" href="<c:out value='${propertyoffer}'/>">
	      <c:out value="${offer.caption}"/>
	    </a>
      </td>
    </tr>
    <tr>
      <td class="table-data">
        <c:out value="${offer.description}"/>
      </td>
    </tr>
    <tr>
      <td class="table-data">
        <c:url var="imageurl" value="/images/${offer.imageFile}"/>
        <img src="<c:out value='${imageurl}'/>" width="70" height="70">
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </c:forEach>

  </table>
  </td>
  </tr>

  <tr>
    <c:url var="newpropertyoffer" value="/propertyoffer.do">
	  <c:param name="id" value=""/>
	</c:url>
    <td><a class="table-link" href="<c:out value='${newpropertyoffer}'/>">Create Offer</a></td>
  </tr>

</table>
