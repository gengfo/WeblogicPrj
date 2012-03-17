<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:webxml="http://java.sun.com/xml/ns/javaee">
	<xsl:template match="/webxml:web-app">
		<web-app xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="2.5">
			<xsl:apply-templates/>
    	
			<login-config>
				<auth-method>CLIENT-CERT</auth-method>
				<realm-name>WebLogic Administrators</realm-name>
			</login-config>
			
			<security-role>
				<role-name>Administrators</role-name>
			</security-role>	

		</web-app>
	</xsl:template>

	<!-- Copy all remaining elements -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
