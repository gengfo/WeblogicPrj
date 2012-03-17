
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="profile" value="${viewProfileForm.profile}"/>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Guest Profile</td>
  </tr>
  <tr>
    <td class="page-text">Here is your current guest profile information:</td>
  </tr>

  <tr>
  <td>
  <table width="100%" cellspacing="5" cellpadding="0" border="0">

    <tr>
      <td class="table-header">Name:</td>
      <td class="table-data"><c:out value='${profile.firstName}'/>&nbsp;<c:out value='${profile.lastName}'/></td>
    </tr>

    <tr>
      <td class="table-header">Phone:</td>
      <td class="table-data"><c:out value='${profile.phone}'/></td>
    </tr>

    <tr>
      <td class="table-header">Email:</td>
      <td class="table-data"><c:out value='${profile.email}'/></td>
    </tr>

    <tr>
      <td class="table-header">Saved Card Info:</td>
      <td class="table-data"><c:out value='${profile.card.type}'/></td>
    </tr>

    <tr>
      <td class="table-header">&nbsp;</td>
      <td class="table-data"><c:out value='${profile.card.number}'/>&nbsp;
			Exp:&nbsp;<c:out value='${profile.card.expiry}'/></td>
    </tr>

  </table>
  </td>
  </tr>

  <%@ include file="/WEB-INF/jsp/common/RecentReservationsBlock.jspf" %>

</table>

