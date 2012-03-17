<%@ include file="/WEB-INF/jsp/common/Include.jspf" %>

<c:set var="property" value="${sessionScope.currentProperty}"/>

<table width="100%" cellspacing="5" cellpadding="0">
  <tr>
    <td class="page-header" align="right"><c:out value="${property.description}"/>&nbsp;-&nbsp;Availability Summary Information</td>
  </tr>
  <tr>
    <td>
      <table width="100%" cellspacing="0" cellpadding="3" border="0">
        <c:forEach var="availsummary" items="${propertyAvailsForm.availabilitySummaries}">
          <c:set var="roomtype" value="${availsummary.roomType}"/>
          <tr>
            <td>
              <c:url var="propertyroom" value="/propertyroom.do">
                <c:param name="id" value="${roomtype.externalIdentity}"/>
              </c:url>
              <a class="table-link" href="<c:out value='${propertyroom}'/>">
                <c:out value="${roomtype.description}"/>
              </a>
            </td>
          </tr>
          <tr>
            <td class="table-data">
              <c:out value="${roomtype.features}"/>
            </td>
          </tr>
          <tr>
            <td>
              <table width="100%" cellspacing="0" cellpadding="3" border="0">
                <tr>
                  <td width="10%" class="table-data">&nbsp;</td> 
                  <c:forEach var="datelabel" items="${propertyAvailsForm.dateList}">
                    <c:url var="propertyavail" value="/propertyavail.do">
                      <c:param name="roomId" value="${roomtype.externalIdentity}"/>
                      <c:param name="editDate" value="${datelabel}"/>
                      <c:param name="startDate" value="${propertyAvailsForm.startDate}"/>
                    </c:url>
                    <td><a class="table-link" href="<c:out value='${propertyavail}'/>"><c:out value="${datelabel}"/></a></td> 
                  </c:forEach>
                </tr>
                <tr>
                  <td class="table-data">Controls:</td> 
                  <c:forEach var="avail" items="${availsummary.availabilityByMonth}">
                    <td class="table-data" align="center"><c:out value="${avail.controls}"/></td> 
                  </c:forEach>
                </tr>
                <tr>
                  <td class="table-data">Closeouts:</td> 
                  <c:forEach var="avail" items="${availsummary.availabilityByMonth}">
                    <td class="table-data" align="center"><c:out value="${avail.closeOuts}"/></td> 
                  </c:forEach>
                </tr>
              </table>
            </td>
          </tr>
          <tr><td>&nbsp;</td></tr>
        </c:forEach>
      </table>
    </td>
  </tr>

  <tr>
    <td>
      <table width="100%" cellspacing="0" cellpadding="3" border="0">
        <tr>
          <td width="50%" align="left">
            <c:url var="earlierpropertyavails" value="/propertyavails.do">
              <c:param name="startDate" value="${propertyAvailsForm.earlierStartDate}"/>
            </c:url>
            <a class="table-link" href="<c:out value='${earlierpropertyavails}'/>">Earlier Dates</A>
          </td>
          <td width="50%" align="right">
            <c:url var="laterpropertyavails" value="/propertyavails.do">
              <c:param name="startDate" value="${propertyAvailsForm.laterStartDate}"/>
            </c:url>
            <A class="table-link" href="<c:out value='${laterpropertyavails}'/>">Later Dates</A>
          </td>
        </tr>
      </table>
    </td>
  </tr>

</table>

