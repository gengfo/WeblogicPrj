<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>
<c:set var="roomtype" value="${propertyAvailForm.roomType}"/>

<form:form name="propertyAvailForm" method="post" commandName="propertyAvailForm">

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Edit Availability Information</td>
  </tr>
  <spring:hasBindErrors name="propertyAvailForm">
  <tr><td>
    <span class="error-header1">Errors<br></span>
    <span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <tr>
    <td>
      <table width="100%" cellspacing="0" cellpadding="3" border="0">
        <tr>
          <td width="70%" align="left" class="table-header">
            <c:out value="${roomtype.description}"/>
          </td>
        </tr>
        <tr>
          <td width="25%" class="table-data">
            Number of rooms in property: <c:out value="${roomtype.numberOfRooms}"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="table-header">
      Controls Starting <c:out value="${propertyAvailForm.editDateWithDay}"/>
    </td>
  </tr>
  <tr>
    <td>
      <table>
        <tr valign="top">
          <td width="20%">
            <table width="100%" cellspacing="5" cellpadding="0" border="0">

              <c:forEach var="thisvalue" items="${propertyAvailForm.availability}" varStatus="status">   
               <tr>
                 <td width="50%" class="page-label">Day&nbsp;<c:out value="${status.count}"/>:</td>
                 <td>
                  <form:input path="availability[${status.count-1}]" size="5"></form:input>
                 </td>
               </tr>

              <c:if test="${status.count == 10}">
            <c:out escapeXml="false" value='</table></td><td width="30%"><table width="100%" cellspacing="5" cellpadding="0" border="0">'/>
              </c:if>
              <c:if test="${status.count == 20}">
              <c:out escapeXml="false" value='</table></td><td width="30%"><table width="100%" cellspacing="5" cellpadding="0" border="0">'/>
              </c:if>
              </c:forEach>

            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td align="left">
      &nbsp;&nbsp;<input type="submit" value="Save Changes">
    </td>
  </tr>
</table>

</form:form>
