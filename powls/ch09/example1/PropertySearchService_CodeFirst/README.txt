== Example 1 - Code-First Web Service project ==

How To Build / Deploy / Run
***************************

1. Pre-requisites

  - Ensure you have fulfilled the pre-requisites outlined in the main Web 
    Service chapter examples readme file at:

    ../../README.txt

2. Clean Project

   In a command shell, change directory to the root directory containing this 
   sample project. Run the following Ant command, to clean out any old compiled
   classes and packages:
  
   > ant clean

3. Build and Deploy Project

   Ensure your local WebLogic Server is running. Run the following command 
   which re-compiles all classes, executes the JWSC Ant task to generate the
   Web Service, builds a deployable EAR and then deploys the EAR to the server:

   > ant 

4. Run WebLogic Web Service Test Client

   Using the WebLogic Admin Console, navigate to the 'Deployments' section and 
   for this deployed project EAR shown in the list, select the Web Service sub-
   element. For the displayed Web Service configuration, select the 'Testing' 
   tab for the Web Service and click the link to launch the test client for 
   this Web Service.

