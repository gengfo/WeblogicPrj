<h1>SAML Sender Vouches Client</h1>

This page makes a SOAP call a <A href="http://domainB.bizreg.com:9011/SAMLSPWebApp/ExampleWebServiceService?WSDL">webservice in DomainB</A> that is protected by a SAML 2.0 Sender Vouches policy.
<%


professional.weblogic.ch11.examples.client.webservice.saml.ExampleWebServiceService service
	= new professional.weblogic.ch11.examples.client.webservice.saml.ExampleWebServiceService();


professional.weblogic.ch11.examples.client.webservice.saml.ExampleWebService ews = service.getExampleWebServicePort();

response.getWriter().println(ews.hello("SAML Token Profile - Sender Vouches"));


%>