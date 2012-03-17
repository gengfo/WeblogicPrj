<HTML>
  <BODY>
    <H1>PropertySearchService Web Service test links</H1>

    <BR>

    <UL>
      <LI><A HREF='PropertySearchService?WSDL'>PropertySearchService WSDL</A></LI>
      <%
      final StringBuffer requestURL = request.getRequestURL();
      final String wsdlURL =
        requestURL.substring(0, requestURL.lastIndexOf("/")) +
        "/PropertySearchService?WSDL";
      %>
      <LI><A HREF='/wls_utc?wsdlUrl=<%=wsdlURL%>'>PropertySearchService Test Client</A></LI>
    </UL>

    <BR>

    <P>Sample data which can be used for the property search service request:</P>
    
    <TABLE BORDER="1">
      <TR>
        <TH>Id</TH><TH>City</TH><TH>State</TH>
      </TR>
      <TR>
        <TD>Property-51</TD><TD>Minneapolis</TD><TD>MN</TD>
      </TR>
      <TR>
        <TD>Property-52</TD><TD>Duluth</TD><TD>MN</TD>
      </TR>
      <TR>
        <TD>Property-61</TD><TD>Minneapolis</TD><TD>MN</TD>
      </TR>
    </TABLE>
  </BODY>
</HTML>
