<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<script src="<c:url value='/js/DatePicker.js'/>"></script>

<table width="100%" cellspacing="5" cellpadding="0">

  <form:form name="selectDatesForm" method="post" commandName="selectDatesForm">

  <tr>
    <td class="page-header" align="right">Select Dates</td>
  </tr>
  <spring:hasBindErrors name="selectDatesForm">
  <tr><td>
    <span class="error-header1">Errors<br></span>
    <span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <tr>
    <td class="page-text">Please enter your arrival and departure dates:</td>
  </tr>
  <tr><td>&nbsp;</td></tr>
	<tr>
    <td>
      <table width="100%" cellspacing="3" cellpadding="0">
        <TR>
          <td width="30%" class="page-label" nowrap>Arrive Date:</td>
          <td>
            <form:input path="arriveDate" size="25"/>
            <a href="javascript:show_calendar('selectDatesForm.arriveDate',selectDatesForm.arriveDate.value)" 
					        onmouseout="window.status='';return true;" 
					        onmouseover="window.status='Choose Arrive Date';return true;">
				      <img align="middle" alt="Choose Arrive Date" border=0 src="<c:url value='/images/calendar_picker.gif'/>">
            </a> 

          </td>
        </tr>
        <tr>
          <td class="page-label" nowrap>Depart Date:</td>
          <td>
            <form:input path="departDate" size="25"/>
            <a href="javascript:show_calendar('selectDatesForm.departDate',selectDatesForm.departDate.value)" 
					        onmouseout="window.status='';return true;" 
					        onmouseover="window.status='Choose Depart Date';return true;">
				      <img align="middle" alt="Choose Depart Date" border=0 src="<c:url value='/images/calendar_picker.gif'/>">
            </a> 
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center"><input type="submit" value="Continue"></td>
        </tr>
      </table>
    </td>
	</tr>
	
  </form:form>

</table>
