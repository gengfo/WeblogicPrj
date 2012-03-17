
<%@ page import="java.util.List,
                 professional.weblogic.ch02.example1.objects.Person,
                 professional.weblogic.ch02.example1.services.PersonService" %>
<%@ page import="professional.weblogic.ch02.example1.*" %>
<%@ page extends="professional.weblogic.ch02.example1.MyJspBase" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
  <title>Edit Person</title>
</head>
<link rel="stylesheet" type="text/css" href="common/style/StyleMaster.css">

<body bgcolor="white">

<% 
  List<String> salutationList = PersonService.getSalutationList();
  String action = getr(request,"action");
  int id = atoi(getr(request,"id"));
  // preserve the original submit token if we are handling error display
  String submittoken = getr(request,"submittoken");
%>

<%-- Retrieve person object from request (if redisplay of form) or from service --%>
<jsp:useBean id="person" class="professional.weblogic.ch02.example1.objects.Person" scope="request">
  <% 
    // code in here is executed only if person is absent from request
    if (action.equals("update")) {
      person = PersonService.findPersonById(id);
    } else {
      person = new Person();
    }
    submittoken = getSubmitToken(session); // only get new one if fresh display
  %>
</jsp:useBean>

<jsp:useBean id="errors" class="java.util.ArrayList" scope="request" />

<form method="POST" action="EditPerson_action.jsp">

  <INPUT TYPE="hidden" name="action" value="<%= action %>">
  <INPUT TYPE="hidden" name="id" value="<%= id %>">
  <INPUT TYPE="hidden" name="submittoken" value="<%= submittoken %>">

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

  <tr><td><%= makeErrorDisplay(errors) %></td></tr>

  <tr>
    <td>
      <table align="left" cellpadding="0" cellspacing="5" border="0">
      

        <tr>
          <td class="medium-black-text-bold" width="33%">Salutation:</td>
          <td>
            <%= makeSelectList("salutation",salutationList,salutationList,person.getSalutation()) %>
          </td>
        </tr>
        <tr>
          <td class="medium-black-text-bold" width="33%">First name:</td>
          <td><INPUT TYPE="text" NAME="firstName" value="<jsp:getProperty name="person" property="firstName"/>" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter first name">
          </td>
        </tr>
        <tr>
          <td class="medium-black-text-bold" width="33%">Middle name:</td>
          <td><INPUT TYPE="text" NAME="middleName" value="<jsp:getProperty name="person" property="middleName"/>" size="50"/>
            <IMG SRC="images/icons/help.gif" align="absbottom" alt="Enter middle name">
          </td>
        </tr>
          <tr>
          <td class="medium-black-text-bold" width="33%">Last name:</td>
          <td><INPUT TYPE="text" NAME="lastName" value="<jsp:getProperty name="person" property="lastName"/>" size="50"/>
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

</form>

</body>

</html>
