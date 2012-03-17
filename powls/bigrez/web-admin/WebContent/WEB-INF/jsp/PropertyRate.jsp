<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<script LANGUAGE="JavaScript">
  function maybeDelete() {
	  if (confirm("Are you sure you want to delete this Rate?")) {
		  document.propertyRateForm.action.value='delete';
      document.propertyRateForm.submit();
	  }
  }
</script>

<c:set var="roomtype" value="${propertyRateForm.roomType}"/>

<form:form name="propertyRateForm" method="post" commandName="propertyRateForm">
<input type="hidden" name="action" value=""/>

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Rate Information</td>
  </tr>
  <spring:hasBindErrors name="propertyRateForm">
  <tr><td>
    <span class="error-header1">Errors<br></span>
    <span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <!-- First show a list of all rates for this roomtype with ours highlighted -->  
  <tr>
    <td>
      <table width="100%" cellspacing="0" cellpadding="3" border="0">
        <tr>
          <td width="70%" align="left" class="table-header">
            <c:out value="${roomtype.description}"/>
          </td>
        </tr>
        <tr>
          <td class="table-data">
            <c:out value="${roomtype.features}"/>
          </td>
        </tr>
          <c:forEach var="rate" items="${propertyRateForm.rates}">
            <tr>
              <c:choose>
                <c:when test="${propertyRateForm.id == rate.externalIdentity}">
                  <!-- current selected rate is not linkable on list -->
                  <td class="table-header" bgcolor="#EEEEEE">
                    &nbsp;&nbsp;$<c:out value='${rate.price.amount}'/>/night from 
                    <f:formatDate value="${rate.startDate}" pattern="MM/dd/yyyy"/> to
                    <f:formatDate value="${rate.endDate}" pattern="MM/dd/yyyy"/>
                  </td> 
                </c:when>
                <c:otherwise>
                  <!-- other rates for this roomtype are linkable in list -->
	              <c:url var="propertyrate" value="/propertyrate.do">
	                <c:param name="roomId" value="${roomtype.externalIdentity}"/>
	                <c:param name="id" value="${rate.externalIdentity}"/>
	              </c:url>
                  <td><a class="table-link" href="<c:out value='${propertyrate}'/>">
                    &nbsp;&nbsp;$<c:out value='${rate.price.amount}'/>/night from 
                    <f:formatDate value="${rate.startDate}" pattern="MM/dd/yyyy"/> to
                    <f:formatDate value="${rate.endDate}" pattern="MM/dd/yyyy"/></A>
                  </td> 
                </c:otherwise>
              </c:choose>
            </tr>
          </c:forEach>
        <tr>
          <c:choose>
            <c:when test="${empty propertyRateForm.id}">
              <td class="table-header" bgcolor="#EEEEEE">
              &nbsp;&nbsp;Create New Rate
              </td>
            </c:when>
            <c:otherwise>
  	          <c:url var="newpropertyrate" value="/propertyrate.do">
	            <c:param name="roomId" value="${roomtype.externalIdentity}"/>
	            <c:param name="id" value=""/>
	          </c:url>
              <td>
                &nbsp;&nbsp;<a class="table-link" href="<c:out value='${newpropertyrate}'/>">Create New Rate</a>
              </td>
            </c:otherwise>
          </c:choose>
        </tr>
      </table>
    </td>
  </tr>
  <!-- Now show the actual form for this rate -->  
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td>
      <table width="100%" cellspacing="5" cellpadding="0" border="0">
        <tr>
          <td width="25%" class="page-label">Start Date:</td>
          <td>
            <form:input path="startDate" size="20"/>
            <a href="javascript:show_calendar('propertyRateForm.startDate',propertyRateForm.startDate.value)" 
               onmouseout="window.status='';return true;" 
               onmouseover="window.status='Choose Start Date';return true;">
               <img align="middle" alt="Choose Date" border=0 src="./images/calendar_picker.gif">
            </a> 
          </td>
        </tr>
        <tr>
          <td width="25%" class="page-label">End Date:</td>
          <td>
            <form:input path="endDate" size="20"/>
            <a href="javascript:show_calendar('propertyRateForm.endDate',propertyRateForm.endDate.value)" 
               onmouseout="window.status='';return true;" 
               onmouseover="window.status='Choose End Date';return true;">
               <img align="middle" alt="Choose Date" border=0 src="./images/calendar_picker.gif">
            </a>       
          </td>
        </tr>
        <tr>
          <td class="page-label">Rate:</td>
          <td><form:input path="rate.price.amount" size="10"/></td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" align="center">
            <input type="submit" value="Save Changes">&nbsp;
            <c:if test="${not empty propertyRateForm.id}">
              <input type="button" value="Delete Rate" onClick="javascript:maybeDelete();">
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

</form:form>
