
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/xml" prefix="x" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Edit Person</title>
</head>
<link rel="stylesheet" type="text/css" href="common/style/StyleMaster.css">

<body bgcolor="white">

<form:form method="post" commandName="personForm">

<table align="left" width="100%" cellpadding="0" cellspacing="0" border="0">
  
<tr>
  <td><img src="/images/spacers/space.gif" width="1" height="5"></td>
</tr>
<tr>
  <td class="large-darkblue-text-bold" align="right">Person</td>
</tr>
<tr>
  <td><img src="/images/spacers/space.gif" width="1" height="2"></td>
</tr>
<tr>
  <td bgcolor="#000099"><img src="/images/spacers/space.gif" width="1" height="2"></td>
</tr>

<tr>
  <td><img src="/images/spacers/space.gif" width="1" height="5"></td>
</tr>

  <spring:hasBindErrors name="personForm">
  <tr><td>
    <span class="error-header1">Validation Errors<br></span><span class="error-header2">You must correct the following error(s) before proceeding:</span><ul>
  	<form:errors path="*" htmlEscape="false" cssClass="error-text"/>
  	</ul>
  </td></tr>
  </spring:hasBindErrors>
  
  <tr>
    <td>
      <table align="left" cellpadding="0" cellspacing="5" border="0">
      

        <tr>
          <td class="medium-black-text-bold" width="33%">Salutation:</td>
          <td>
            <form:select path="salutation">
              <form:options items="${salutationList}" itemValue="salutation" itemLabel="salutation"/>
            </form:select>
          </td>
        </tr>
        <tr>
          <td class="medium-black-text-bold" width="33%">First name:</td>
          <td><form:input path="firstName" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter first name">
          </td>
        </tr>
        <tr>
          <td class="medium-black-text-bold" width="33%">Middle name:</td>
          <td><form:input path="middleName" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter middle name">
          </td>
        </tr>
          <tr>
          <td class="medium-black-text-bold" width="33%">Last name:</td>
          <td><form:input path="lastName" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter last name">
          </td>
        </tr>
        <tr>
        <tr>
          <td>&nbsp;</td>
          <td class="courier-font">
            <table><TR>
              <td><input type="submit" value="Save"></td>
              <td>&nbsp;</td>
              <td><input type="reset" value="Cancel"></td>
            </TR></table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

</form:form>

</body>

</html>
