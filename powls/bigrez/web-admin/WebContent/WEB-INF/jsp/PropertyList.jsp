
<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right">Properties</td>
  </tr>
  <tr>
    <td class="page-label">Welcome to the BigRez.com administration site! Please select a property.</td>
  </tr>
  <tr>
    <td>
      <table width="100%" cellspacing="0" cellpadding="3" border="0">
        <c:forEach var="property" items="${propertyListForm.propertyList}">
          <tr>
            <td width="80" align="center">
              <img src="<c:url value='/images/${property.imageFile}'/>" alt="" width="70" height="70">
            </td>
            <td width="100%" align="left">
              <table cellspacing="0" cellpadding="0" border="0">
                <tr>
                  <td align="left">
                    <c:url var="propertymain" value="/propertymain.do">
                      <c:param name="id" value="${property.externalIdentity}"/>
                    </c:url>
            		<a class="table-link" href="<c:out value='${propertymain}'/>">
                      <c:out value='${property.description}'/>
                    </a>
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
            <td>
        		<a href="<c:out value='${propertymain}'/>"><img src="<c:url value='/images/selectbutton.gif'/>" border="0"/></a>
        	</td>
          </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
</table>
