
<%@ page language="java"%>
<%@ include file="Include.jspf" %>

<?xml version="1.0" encoding="ISO-8859-15"?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Welcome to BigRez.com!</title>
  <link rel=stylesheet type="text/css" href="<c:url value='/css/StyleMaster.css'/>">
</head>

<body bgcolor="#FFFFFF">

<table align="center" width="725" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td colspan="2">
      <table align="center" cellpadding="0" cellspacing="10">
        <tr>
          <td align="center">
            <tiles:insertAttribute name="header"/>
          </td>
        </tr>
        <tr>
          <td>
            <tiles:insertAttribute name="topnav"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<table align="center" width="725" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td colspan="5" bgcolor="#000066"><img src="../images/space.gif" width="1" height="3"></td>
  </tr>
  <tr>
    <td width="3" bgcolor="#000066"><img src="../images/space.gif" width="3" height="1"></td>
    <td colspan="3" bgcolor="#CCCCCC"><img src="../images/space.gif" width="1" height="2"></td>
    <td width="3" bgcolor="#000066"><img src="../images/space.gif" width="3" height="1"></td>
  </tr>
  <tr>
    <td width="3" bgcolor="#000066"><img src="../images/space.gif" width="3" height="1"></td>
    <td width="2" bgcolor="#CCCCCC"><img src="../images/space.gif" width="2" height="1"></td>
    <td valign="top">
      <table width="725" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="175" valign="top" bgcolor="#EEEEEE">
            <table width="175" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td><tiles:insertAttribute name="rezinfo"/></td>
            </tr>
            <tr>
              <td><tiles:insertAttribute name="offers"/></td>
            </tr>
            </table>
          </td>
          <td width="1" ><img src="../images/space.gif" width="1" height="1"></td>
          <td width="550" valign="top">
            <tiles:insertAttribute name="body"/>
          </td>
        </tr>
      </table>
    </td>
    <td width="2" bgcolor="#CCCCCC"><img src="../images/space.gif" width="2" height="1"></td>
    <td width="3" bgcolor="#000066"><img src="../images/space.gif" width="3" height="1"></td>
  </tr>
  <tr>
    <td width="3" bgcolor="#000066"><img src="../images/space.gif" width="3" height="1"></td>
    <td colspan="3" bgcolor="#CCCCCC"><img src="../images/space.gif" width="1" height="2"></td>
    <td width="3" bgcolor="#000066"><img src="../images/space.gif" width="3" height="1"></td>
  </tr>
  <tr>
    <td colspan="5" bgcolor="#000066"><img src="../images/space.gif" width="1" height="3"></td>
  </tr>
  <tr>
    <td colspan="5"><img src="../images/space.gif" width="1" height="5"></td>
  </tr>
</table>

<table align="center" width="725" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <tiles:insertAttribute name="footer"/>
  </tr>
</table>

</body>
</html>
