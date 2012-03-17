
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/xml" prefix="x" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Person List</title>
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
    <c:forEach var="person" items="${personList.results}">
      <tr>
        <TD class="table-data"><c:out value="${person.salutation}"/></TD>
        <TD class="table-data"><c:out value="${person.firstName}"/></TD>
        <TD class="table-data"><c:out value="${person.middleName}"/></TD>
        <TD class="table-data"><c:out value="${person.lastName}"/></TD>
        <TD class="table-data"><a href="editperson.do?id=<c:out value="${person.id}"/>">[Edit]</a></TD>
      </tr>
    </c:forEach>
    </table>
  </td>
</tr>

<tr>
  <td><img src="/images/spacers/space.gif" width="1" height="10"></td>
</tr>

<tr>
  <td class="table-header">
    <a href="editperson.do?id=0">Create Person</a>
  </td>
</tr>

</table>

</body>

</html>
