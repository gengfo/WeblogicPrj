
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${viewPropertyForm.property}"/>

<table width="100%" cellspacing="5" cellpadding="0">

  <tr>
    <td class="page-header" align="right">Property Information</td>
  </tr>
  <tr>
    <td class="page-text">Here is detailed information on <c:out value='${property.description}'/>:</td>
  </tr>

  <tr>
  <td>
  <table width="100%" cellspacing="5" cellpadding="0" border="0">
    <tr>
      <td width="80" align="center">
        <img src="<c:url value='/images/${property.imageFile}'/>" alt="" width="70" height="70"/>
      </td>
      <td width="100%" align="left">
        <table cellspacing="0" cellpadding="0" border="0">
          <tr>
            <td align="left">
              <span class="table-header">
                <c:out value='${property.description}'/>
              </span>
            </td>
          </tr>
          <tr>
            <td align="left">
              <span class="table-data">
                <c:out value='${property.address.address1}'/>
              </span>
            </td>
          </tr>
          <c:if test='${not empty property.address.address2}'>
          <tr>
            <td align="left">
              <span class="table-data">
                <c:out value='${property.address.address2}'/>
              </span>
            </td>
          </tr>
          </c:if>
          <tr>
            <td align="left">
              <span class="table-data">
                <c:out value='${property.address.city}'/>&nbsp;<c:out value='${property.address.stateCode}'/>,&nbsp;<c:out value='${property.address.postalCode}'/>
              </span>
            </td>
          </tr>
        </table>
      </td>
      <td align="right">  
        <form:form method="post" commandName="viewPropertyForm"> 
          <input type="image" src="<c:url value='/images/selectbutton.gif'/>" border="0"/>
        </form:form>
      </td>
    </tr>

    <tr>
      <td colspan="3" class="table-data">
        <c:out value='${property.features}' escapeXml='false'/>
      </td>
    </tr>

  </table>
  </td>
  </tr>

</table>

