== Example 9 - Callback Web Service project ==

How To Build / Deploy
*********************

1. Pre-requisites

  - Ensure you have fulfilled the pre-requisites outlined in the main Web 
    Service chapter examples readme file at:

    ../../README.txt

2. Clean Project

   In a command shell, change directory to the root directory containing this 
   sample project. Run the following Ant command, to clean out any old
   compiled classes and packages:
  
   > ant clean

3. Generate Service Client Stubs

   Ensure your local WebLogic Server is running and the target callback return 
   web service has already been deployed. Run the following command to execute
   the ClientGen Ant task which generates the callback Web Service stub 
   classes using the WSDL from the WebLogic hosted Web Service.

   > ant generate


4. Build and Deploy Project

   Run the following command which re-compiles all classes, executes the JWSC 
   Ant task to generate the Web Service, builds a deployable EAR and then 
   deploys the EAR to the server:

   > ant 

Note: This test is best tested by first building the callback return service, 
before building and deploying this service and then building and running the 
callback client.
