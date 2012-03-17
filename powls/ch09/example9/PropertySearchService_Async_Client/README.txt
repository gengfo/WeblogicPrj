== Example 9 - Asynchronous Invoker Web Service Client project ==

How To Build / Run
******************

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

   Ensure your local WebLogic Server is running and the target Web Service 
   from the Async service project in Example 9 has already been deployed. Run 
   the following command to execute the ClientGen Ant task which generates the
   Web Service stub classes using the WSDL from the WebLogic hosted Web Service.

   > ant generate

4. Build and Run Project

   Run the following command which compiles all classes and runs the client 
   application's main() method to test that the Web Service client correctly 
   calls the WebLogic hosted Web Service.

   > ant 

