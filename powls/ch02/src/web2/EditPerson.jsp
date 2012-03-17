
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Edit Person</title>
  <html:base/>
</head>
<link rel="stylesheet" type="text/css" href="common/style/StyleMaster.css">

<body bgcolor="white">

<html:form action="/EditPersonAction">
<html:hidden property="action"/>
<html:hidden property="id"/>

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

  <tr><td><html:errors/></td></tr>

  <tr>
    <td>
      <table align="left" cellpadding="0" cellspacing="5" border="0">
      

        <tr>
          <td class="medium-black-text-bold" width="33%">Salutation:</td>
          <td>
            <html:select property="salutation">
              <html:options collection="salutationList" property="salutation" labelProperty="salutation"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="medium-black-text-bold" width="33%">First name:</td>
          <td><html:text property="firstName" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter first name">
          </td>
        </tr>
        <tr>
          <td class="medium-black-text-bold" width="33%">Middle name:</td>
          <td><html:text property="middleName" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter middle name">
          </td>
        </tr>
          <tr>
          <td class="medium-black-text-bold" width="33%">Last name:</td>
          <td><html:text property="lastName" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter last name">
          </td>
        </tr>
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

</html:form>

</body>

</html>
