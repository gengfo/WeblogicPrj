== Example 9 - Callback Return Web Service project ==

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

3. Build and Deploy Project

   Ensure your local WebLogic Server is running and run the following command
   which re-compiles all classes, executes the JWSC Ant task to generate the
   Web Service, builds a deployable EAR and then deploys the EAR to the server:

   > ant 

Note: This test is best tested by building and deploying this and then the 
callback service and then running the callback client.
