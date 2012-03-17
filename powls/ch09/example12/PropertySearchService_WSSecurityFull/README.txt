== Example 12 - WS-Security SSL/Certificate Secured Web Service project ==

How To Build / Deploy
*********************

1. Pre-requisites

  - Ensure you have fulfilled the pre-requisites outlined in the main Web 
    Service chapter examples readme file at:

    ../../README.txt

2. Generate the new server-side Key, Certificate and Keystore

   In a new sub-directory (called 'serverkeystore') hanging off the domain 
   directory, from the command line, run WebLogic's CertGen tool to generate
   the public/private key and certificate for the Web Service, for the user
   'weblogic'. Then run WebLogic's ImportPrivateKey to import the generated
   private key and certificate into a new server-side private keystore:
   
   > . setWLSEnv.sh       (or on Windows run: setWLSEnv.cmd)
   > java utils.CertGen -keyfilepass ServerKeyPass -certfile ServerCert -keyfile ServerKey -strength 1024 -cn weblogic -ou weblogic -o Oracle -l Mytown -s Mystate -c US
   > java utils.ImportPrivateKey -keystore ServerIdentity.jks -storepass ServerStorePass -storetype JKS -alias servercert -keypass ServerKeyPass -keyfilepass ServerKeyPass -certfile ServerCert.pem -keyfile ServerKey.pem

   The new Server Key Store will be called 'ServerIdentity.jks'.
   
3. Configure the Domain for SSL

   For each server in the domain, configure it to support SSL (enable the "SSL
   Listen Port Enabled" field and set the "SSL Listen Port").

   For the domain's Security Realm, re-configure the 'DefaultIdentityAsserter'
   provider to support certificate based identity assertion. Add 'X.509' as an 
   'Active Type' and set the 'Default User Name Mapper Attribute Type' field
   to the value 'CN'.

4. Configure the Domain for a new Identity Keystore
     (Note: see OPTIONAL alternative to this step at base of this file)

   In the Admin Console's Keystore tab for each server, specify a new Custom 
   Identity Store and Custom Trust Store, using the new keystore for identity 
   and the existing demo keystore for trust, using the following settings:

     Custom Identity Keystore: <domainpath>/serverkeystore/ServerIdentity.jks
     Custom Identity Keystore Type: JKS
     Custom Identity Keystore Passphrase: ServerStorePass
	 Custom Trust Keystore: <wlhome>/server/lib/DemoTrust.jks
     Custom Trust Keystore Type: JKS
     Custom Trust Keystore Passphrase: DemoTrustKeyStorePassPhrase

   In the SSL admin console tab, specify a new identity for the server, 
   referencing the newly generated keys using the following settings:

     Private Key Alias: servercert
     Private Key Passphrase: ServerKeyPass

	Then re-start the WebLogic Server for all the changes to take effect.
   
5. Clean Project

   In a command shell, change directory to the root directory containing this 
   sample project. Run the following Ant command, to clean out any old 
   compiled classes and packages:
  
   > ant clean

6. Build and Deploy Project

   Ensure your local WebLogic Server is running. Run the following command 
   which re-compiles all classes, executes the JWSC Ant task to generate the
   Web Service, builds a deployable EAR and then deploys the EAR to the server:

   > ant 

Note: This test requires the injection of WS-Security specific SOAP headers, 
Therefore it is best to test the Web Service by building and deploying the
corresponding client project in this example 12.


------------------------------------------------
OPTIONAL: ALTERNATIVE FOR STEP 4

   Instead of changing the server's default identity. A separate server 
   identity (key and certificate), can be configured for use by one or more
   deployed Web Services, which is different that the server's default 
   identity key and certificate. Instead of performing step 4, above, perform
   the following actions...

   Create a new Domain Web Services Security Configuration to be configured to
   use the new server private key and keystore that	we created in the step 2.
   This can easily achieved by running the following script which is contained
   in the WebLogic Server examples directory: 
	
    > . setWLSEnv.sh       (or on Windows run: setWLSEnv.cmd)
	> java weblogic.WLST <wls-home>/samples/server/examples/src/examples/webservices/wss1.1/configWss.py weblogic weblogic localhost 7001 <domain-home>/ServerIdentity.jks ServerStorePass servercert ServerKeyPass   

	(replace <wls-home> with the path the WebLogic Server installation and 
	 replace <domain-home> with the path of the WebLogic Server Domain)

  Example:	
	java weblogic.WLST /opt/oracle/wls103/wlserver_10.3/samples/server/examples/src/examples/webservices/wss1.1/configWss.py weblogic weblogic localhost 7001 /var/oracle/WSTest103Domain/ServerIdentity.jks ServerStorePass servercert ServerKeyPass

	Then re-start the WebLogic Server for all the changes to take effect.

