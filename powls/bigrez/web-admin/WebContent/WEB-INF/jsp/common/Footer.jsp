<%@ include file="Include.jspf" %>

<td>
  <table width="100%" cellspacing="0" cellpadding="0">
    <tr>
      <c:if test="${not empty username}">
        <td align="right" class="sidebar-data">
          Logged in as <c:out value="${username}"/>
        </td>
      </c:if>
    </tr>
  </table>
</td>
