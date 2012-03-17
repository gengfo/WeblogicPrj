== Example 10 - SSL with 2-way Certificates Web Service Client project ==

How To Build / Run
******************

1. Pre-requisites

  - Ensure you have fulfilled the pre-requisites outlined in the main Web 
    Service chapter examples readme file at:

    ../../README.txt

2. Generate the Keys, Certificates and Keystores

   Ensure your local WebLogic Server is running, run WebLogic's CertGen tool 
   to generate the public/private key and certificate for our service client
   for the user 'weblogic' and run WebLogic's ImportPrivateKey to import the 
   generated private key and certificate into a new client-side private 
   keystore:
  
   > . setWLSEnv.sh       (or on Windows run: setWLSEnv.cmd)
   > java utils.CertGen -keyfilepass mypassword -certfile ClientCert -keyfile ClientKey -strength 1024 -cn weblogic -ou weblogic -o Oracle -l Mytown -s Mystate -c US
   > java utils.ImportPrivateKey -keystore ClientIdentity.jks -storepass mypassword -storetype JKS -alias clientcert -keypass mypassword -keyfilepass mypassword -certfile ClientCert.pem -keyfile ClientKey.pem

3. Clean Project

   In a command shell, change directory to the root directory containing this 
   sample project. Run the following Ant command, to clean out any old 
   compiled classes and packages:
  
   > ant clean

4. Generate Service Client Stubs

   Ensure the target Web Server from the SSL Cert project in Example 10 has 
   already been deployed. Run the following command to execute the ClientGen 
   Ant task which generates the Web Service stub classes using the WSDL from 
   the WebLogic hosted Web Service.

   > ant generate

5. Build and Run Project

   Run the following command which compiles all classes and runs the client 
   application's main() method to test that the Web Service client correctly 
   calls the WebLogic hosted Web Service.

   > ant 

