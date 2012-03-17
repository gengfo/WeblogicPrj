
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Find a Property</td>
  </tr>
  <spring:hasBindErrors name="personSearchForm">
  <tr><td>
    <span class="error-header1">Validation Errors<br></span><span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  <tr>
    <td class="page-text">Please enter the state or city you plan to visit:</td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td>
	  <form:form method="post" commandName="propertySearchForm">
      <table width="100%" cellspacing="6" cellpadding="0">
        <tr>
          <td width="25%" class="page-label" nowrap>State Code:</td>
          <td width="75%">
            <form:select path="stateCode">
              <form:option value="">Choose...</form:option>
              <form:options items="${stateCodeList}" itemValue="value" itemLabel="label"/>
            </form:select>
          </td>
        </tr>
        <tr>
          <td class="page-label" nowrap>City:</td>
          <td>
            <form:select path="city">
              <form:option value="">Choose...</form:option>
              <form:options items="${cityList}" itemValue="value" itemLabel="label"/>
            </form:select>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>
        <tr>
          <td colspan="2" align="left">
            &nbsp;&nbsp;<input type="submit" value="Find Properties">
          </td>
        </tr>
      </table>
      </form:form>
    </td>
  </tr>

</table>
