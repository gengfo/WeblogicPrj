Professsional Oracle WebLogic Server - Chapter 11, Security
***********************************************************

Please read the top level README.txt file for important information
about these samples.


Description
***********

All of the source code for the samples is in the ch11/src directory.
The code for this samples is very straightforward. In almost every
case, all that code does is return who the user is via the
weblogic.security.Security.getCurrentSubject() method.

What is complicated is the configuration, so with the exception of
setting the initial environmnent, everything for this chapter is
scripted with WLST. The scripts and any data (like exported users and
policies) can be found in the scripts directory. The scripts directory
is roughly organized by project. The sample applications (exampleEJB,
httpsSampleWebApp, SAMLSPWebApp, SAMLSSOWebApp and
WebAppSecurityExample) do have some interesting information in their
various deployment descriptors.


Setup
*****

1. Modify hosts file

  The samples create two Weblogic Server domains - DomainA and
  DomainB, and refer to them using two network names
  (domainA.bigrez.com and domainB.bigrez.com). So that you can run the
  samples on a single machine and the generated certificates are
  valid, you should modify your hosts file (
  c:\windows\system32\drivers\etc\hosts on Windows or etc/hosts on
  Unix) so that domainA.bigrez.com and domainb.bigrez.com
  resolve to localhost by adding the folowing line:

    127.0.0.1       localhost domainB.bigrez.com domainA.bigrez.com
 

2. Generate certificates and build samples

  Once the hosts file has been modified is done, you should build the
  samples using Ant. First ensure that the shell is configured with
  the WebLogic Server environment as described in the top level
  README.txt, then run Ant. E.g.

    c:\powls\ch11> ant 
 
  The Ant build will create the keys and the domains used by the
  samples, compile the sample code, and create the WebLogic Server
  domains. This process will take several minutes.

    Domain name   Path                                  URLs

    ch11_DomainA  c:\powls\ch11\build\domains\DomainA   http://domainA.bigrez.com:9001/console
                                                        https://domainA.bigrez.com:9002/console

    ch11_DomainB  c:\powls\ch11\build\domains\DomainB   http://domainA.bigrez.com:9011/console
                                                        https://domainA.bigrez.com:9012/console

  The Ant build also creates the following users:

    User           Password  Groups                               Private Key (PKCS12)
    weblogic       welcome1  Admin(Role)                          -
    someuser       password  Ch11ProtectedUsers                   someuser.p12
    someotheruser  password  Ch11ProtectedUsers,Ch11SpecialUsers  someotheruser.p12

  Note: The password for the private keys is "password".


3. Deploy the samples

  The next step is to deploy the samples

    c:\powls\ch11> ant deploy-samples     
 

4. Deploy the RDBMS plugin

  The connection information that the RDBMS Plugin uses to connect to
  the database is defined in the local.properties file

    rdbms.driver = oracle.jdbc.driver.OracleDriver
    rdbms.url = jdbc:oracle:thin:@localhost:1521:orcl
    rdbms.user = system
    rdbms.password = password
 

  Modify the settings to match your environment and run

    c:\powls\ch11> ant build-rdbms-plugin
 

  This Ant task connects to the database, creates the table and the
  sample users. It then configures DomainA with a JDBC data source for
  the database, and then configures each of the realms with a
  CustomSQLAuthenticator that uses the plugin. In order for the
  changes to take affect, DomainA needs to be restarted.

  NOTE: The samples have been tested with Oracle Database 11g.


5. Deploy the SAML Samples

  The settings for the SAML 2.0 metadata is contained in the
  local.properties file. You can modify it if you see fit.

    saml.OrganizationName = CH11_Security_Samples
    saml.ContactPersonType = technical
    saml.OrganizationURL = http://www.bigrez.com
    saml.ContactPersonGivenName = Josh
    saml.ContactPersonCompany = bigrez
    saml.ContactPersonEmailAddress = josh@bigrez.com
    saml.ContactPersonSurName = Bregman
    saml.ContactPersonTelephoneNumber = 123-456-7890
 

  To deploy the SAML samples:

    c:\powls\ch11> ant build-saml
 

How to Run the Samples
**********************

The examples are divided into several areas:


1. Stand-Alone Clients

  Examples of how to use stand alone clients to communicate securely
  with WebLogic Server.

    * JAAS Client used to authenticate to WLS. Includes standard and
       custom login modules.

    * RMI Client using SSL for transport security as well as Identity.

    * HTTP Client using both WLS and JSSE SSL stacks for transport
      security as well as Identity.

    * Custom HostnameVerifier and TrustManager implementations for WLS
      and JSSE SSL stacks

  These samples can be run from the command line

   c:\powls\ch11> ant run-client
 
     [echo] Client Examples:
     [echo] ====================================================
     [echo]     01 - JAAS Client - Standard Login
     [echo]     02 - JAAS Client - Custom Login
     [echo]     03 - RMI Client  - 1 way SSL - Thin Client (JSSE)
     [echo]     04 - RMI Client  - 2 way SSL - Thin Client (JSSE)
     [echo]     05 - RMI Client  - 1 way SSL - WLS SSL
     [echo]     06 - RMI Client  - 2 way SSL - WLS SSL
     [echo]     07 - RMI Client  - 2 way SSL - WLS SSL - Identity Propagation
     [echo]     08 - HTTP Client - 1 way SSL - WLS SSL
     [echo]     09 - HTTP Client - 2 way SSL - WLS SSL
     [echo]     10 - HTTP Client - 2 way SSL - WLS SSL - Custom Hostname Verifie
r and Trust Manager
     [echo]     11 - HTTP Client - 2 way SSL - Thin Client (JSSE)
     [echo]     12 - HTTP Client - 2 way SSL - Thin Client (JSSE) - Custom Hostn
ame Verifier and Trust Manager
     [echo]     13 - HTTP Client - 2 way SSL - Thin Client (JSSE) - Identity Pro
pagation
    [input] Enter the number of the client example you want to run:
 

  The Ant task will start the server (DomainB), and make sure its in
  the correct SSL state, and the run the client with the correct
  configuration.


2. WebApplication and EJB Sample Applications

  Once you've deployed the samples, start-up Domain A.

   c:\powls\ch11> build\domains\domainA\startWebLogic.cmd
 

  The jumping off point for the examples is
  http://domaina.bigrez.com:9001/WebAppSecurityExample/index.jsp.

  The basic idea of the samples is to show the behavior of EJBs and
  WebApps in 3 different security modes - DDOnly, CustomRoles, and
  CustomRolesAndPolicies. You can change the active realm from the
  console, but to simplify it, I created some WLST scripts and Ant
  tasks that you can use to change the mode and re-deploy the
  application with the correct canned policies. The Ant tasks are
  listed below:

  DDOnly Realm

    c:\powls\ch11> ant default-realm  
 
  CustomRoles Realm

    c:\powls\ch11> ant customRoles-realm
 
  CustomRolesAndPolicies Realm
 
    c:\powls\ch11> ant customRolesAndPolicies-realm
 

3. SAML Samples

  The SAML samples demonstrated both Web SSO and SAML Token Profile
  (Web-Services). Both scenarios require that both domains are up and
  running. Also, make sure that DomainA is running with the DDOnly
  (myrealm). SAML is only configured in the default realm. In both
  scenarios, try logging in as someotheruser

  To show the WebSSO scenario, go to
  http://domainb.bigrez.com:9011/SAMLSPWebApp/

  To show the SAML Token Profile (Web Services), go to
  http://domaina.bigrez.com:9001/SAMLSSOWebApp/protected/index.jsp


4. RDBMS Plugin

  The RDBMS plugin creates a single table in the database called
  CH11_RDBMS_PLUGIN. This table has three columns USERNAME, PASSWORD,
  and GROUPS. The SQL for creating the two sample users is

    INSERT INTO CH11_RDBMS_PLUGIN VALUES ('someuser','password','CH11ProtectedGroup');
    INSERT INTO CH11_RDBMS_PLUGIN VALUES ('someuserother','password','CH11ProtectedGroup:CH11SpecialGroup');
 

  With the plugin configured, try creating additional users or
  changing the groups of the existing sample users.
