
<%@ page import="java.util.List,
                 professional.weblogic.ch02.example1.services.PersonService" %>
<%@ page import="professional.weblogic.ch02.example1.*" %>
<%@ page extends="professional.weblogic.ch02.example1.MyJspBase" %>

<%-- Create a "Person" object, load it with data from request params, and store it on http request --%>
<jsp:useBean id="person" class="professional.weblogic.ch02.example1.objects.Person" scope="request" />
<jsp:setProperty name="person" property="*" />

<jsp:useBean id="errors" class="java.util.ArrayList" scope="request" />

<%
// Check for proper submit token before validating or processing this form
String submittoken = getr(request,"submittoken");
if (checkSubmitToken(session,submittoken)==false) {
  errors.add("This form has expired and may not be resubmitted!&nbsp;<A HREF=\"ShowPeople.jsp\">Click here to continue.</A>");
  %>
    <jsp:forward page="EditPerson.jsp" />
  <%
}

// Perform validation against the Person object loaded with form data
if (person.getLastName().equals("")) {
  errors.add("Last name is a required field");
}

// Return to form page if errors are encountered, leaving Person on request
if (errors.size()>0) { 
%>
  <jsp:forward page="EditPerson.jsp"/>
<%
} else {
  // Clear the token from the session so it will no longer be valid
  clearSubmitToken(session);
  // Process the request using the PersonService
  String action = getr(request,"action");
  int id = Integer.parseInt(getr(request,"id"));
  if (action.equals("update")) {
    LOG.debug("Updating person bean in database");
    PersonService.updatePerson(person);
  } else if (action.equals("create")) {
    LOG.debug("Creating person bean in database");
    PersonService.createPerson(person);
  }
  // Send user to main page. Use redirect instead of forward..
  redirect(response,"./ShowPeople.jsp"); 
}
%>

