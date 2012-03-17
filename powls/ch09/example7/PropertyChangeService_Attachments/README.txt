== Example 7 - Using SOAP Attachments in a Web Service project ==

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

3. Generate Web Service Skeleton Code

   Run the following command to execute the WSDLC Ant task which generates the
   Web Service skeleton code including SEI interface and the Data POJOs.

   > ant generate

4. Build and Deploy Project

   Ensure your local WebLogic Server is running. Run the following command 
   which re-compiles all classes, executes the JWSC Ant task to generate the
   Web Service, builds a deployable EAR and then deploys the EAR to the server:

   > ant 

Note: This test requires the attaching of an image first to the SOAP request.
Therefore it is best to test the Web Service by building and deploying the
corresponding client project in his example 7.

