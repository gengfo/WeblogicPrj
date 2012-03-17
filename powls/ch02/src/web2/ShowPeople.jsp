
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ page import="java.util.*, professional.weblogic.ch02.example2.objects.Person, professional.weblogic.ch02.example2.services.PersonService" %>

<% Collection<Person> personList = PersonService.getPersonList(); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Person List</title>
  <html:base/>
</head>
<link rel="stylesheet" type="text/css" href="common/style/StyleMaster.css">

<body bgcolor="white">

<table align="left" width="100%" cellpadding="0" cellspacing="0" border="0">
    
<tr>
  <td><img src="/images/spacers/space.gif" width="1" height="5"></td>
</tr>
<tr>
  <td class="large-darkblue-text-bold" align="right">Person List</td>
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

<jsp:useBean id="person" class="professional.weblogic.ch02.example2.objects.Person"/>

<tr>
  <td>
    <table width="90%">
      <tr>
        <TD class="table-header" width="15%">Salutation</TD>
        <TD class="table-header" width="20%">First Name</TD>
        <TD class="table-header" width="15%">Middle Name</TD>
        <TD class="table-header" width="20%">Last Name</TD>
        <TD class="table-header" width="10%">&nbsp;</TD>
      </tr>
    <% Iterator i = personList.iterator(); 
       while (i.hasNext()) {
          person = (Person)(i.next());
    %>
      <tr>
        <TD class="table-data"><jsp:getProperty name="person" property="salutation"/></TD>
        <TD class="table-data"><jsp:getProperty name="person" property="firstName"/></TD>
        <TD class="table-data"><jsp:getProperty name="person" property="middleName"/></TD>
        <TD class="table-data"><jsp:getProperty name="person" property="lastName"/></TD>
        <TD class="table-data"><a href="ShowPeopleAction.do?action=update&id=<jsp:getProperty name="person" property="id"/>">[Edit]</a></TD>
      </tr>
    <% } // end while %>

    </table>
  </td>
</tr>

<tr>
  <td><img src="/images/spacers/space.gif" width="1" height="10"></td>
</tr>

<tr>
  <td class="table-header">
    <a href="ShowPeopleAction.do?action=create">Create Person</a>
  </td>
</tr>

</table>

</body>

</html>
