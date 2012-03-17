== Example 10 - SSL with 2-way Certificates Web Service project ==

How To Build / Deploy
*********************

1. Pre-requisites

  - Ensure you have fulfilled the pre-requisites outlined in the main Web 
    Service chapter examples readme file at:

    ../../README.txt

2. Configure the Domain for SSL and Certificate Identity Assertion

   Ensure your local WebLogic Server is running. For each server in the 
   domain, configure it to support SSL (enable the "SSL Listen Port Enabled"
   field and set the "SSL Listen Port").

   In server's SSL admin console tab (advanced), set the "Two Way Client Cert 
   Behaviour" field to "Client Certs Requested And Enforced".

   For the domain's Security Realm, re-configure the 'DefaultIdentityAsserter'
   provider to support certificate based identity assertion. Add 'X.509' as an 
   'Active Type' and set the 'Default User Name Mapper Attribute Type' field
   to the value 'CN'.

   Restart the WebLogic Server for changes to take effect.

3. Clean Project

   In a command shell, change directory to the root directory containing this 
   sample project. Run the following Ant command, to clean out any old 
   compiled classes and packages:
  
   > ant clean

4. Build and Deploy Project

   Run the following command which re-compiles all classes, executes the JWSC 
   Ant task to generate the Web Service, builds a deployable EAR and then 
   deploys the EAR to the server:

   > ant 

Note: This test requires the processing of certificates using SSL. Therefore 
it is best to test the web service by building and deploying the corresponding 
client project in this example 10.

