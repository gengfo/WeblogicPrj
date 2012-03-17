Professsional Oracle WebLogic Server - Sample Code
**************************************************

License
*******

Please refer to the file LICENSE.txt for copyright and license
information.


Description
***********

This distribution contains the source code for standalone samples from
the following chapters of the book:

  Chapter 2  - Choosing a Web Application Architecture
  Chapter 9  - Developing and Deploying Web Services
  Chapter 11 - Using WebLogic Security
  Chapter 12 - Administering and Deploying Applications in WebLogic
               Server

It also includes the BigRez sample application, which is referred to
throughout the book.

The samples have been tested on Windows XP and on Ubuntu Linux, but
should run on all platforms supported by WebLogic Server 10.3.1.


Common pre-requisites
*********************

1. Obtain and install the following pre-requisites

   - Oracle WebLogic Server 10.3.1
     (http://www.oracle.com/technology/software/products/ias/index.html)


2. Update properties file for your environment

   Each chapter directory contains an local.properties file. You may
   need to edit this to match your environment.


3. Create a WebLogic Server Domain and start the administration server

   Use the WebLogic Server configuration wizard to create a new
   WebLogic Server 10.3.1 domain. On Windows, the wizard is available
   from your start menu. On Unix, run the
   wlserver_10.3.1/common/bin/config.sh script from the WebLogic
   Server installation.


4. Set the command prompt/shell/terminal environment

   To set up the command line environment to be suitable for building
   the samples, run the bin\setDomainEnv.cmd script from the domain
   directory. Unix users should source the bin/setDomainEnv.sh script
   instead.

   This will set the following environment variables upon which the
   build scripts depend:

     JAVA_HOME   - location of a Java SE 6 Java installation.	
     ANT_HOME    - an ANT installation.
     PATH        - must include the Java and Ant bin directories.
     DOMAIN_HOME - location of your domain directory.
     WL_HOME     - location of the WebLogic Server product, below
                   your Middleware Home.


5. Create a WebLogic Server Domain and start the administration server

   [ NB This step is unnecessary to run the Chapter 11 samples. The
   [    Chapter 11 build files include the steps to create sample
   [    domains due to the involved nature of the security
   [    configuration.

   In a new terminal window, start the domain's administration server
   using the domain'startWebLogic.cmd or startWebLogic.sh script.

   If you have used a non-defalt host name and port (something other
   than localhost:7001), be sure to update ch02/local.properties and
   bigrez/etc/local.properties.
