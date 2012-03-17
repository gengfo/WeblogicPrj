Professsional Oracle WebLogic Server - BigRez Sample Application
****************************************************************

Please read the top level README.txt file for important information
about these samples.


How to build BigRez
*******************

1. Obtain and install the following pre-requisites

   - Oracle WebLogic Server 10.3.1
     (http://www.oracle.com/technology/software/products/ias/index.html)

   - JUnit 4 or later.
     (http://www.junit.org/)

   - Mockito 1.6 or later.
     (http://code.google.com/p/mockito/)

   - A database. We developed BigRez with Oracle Database 10g.
     The 10g Express Edition (also known as Oracle XE) will work fine:
     (http://www.oracle.com/technology/software/products/database/xe/index.html)
     If you are using a non-Oracle database, some minor porting effort
     will be required.


2. Update properties file for your environment

   Edit etc/local.properties for local installation directories of
   JUnit and Mockito; and database connection information. BigRez
   has only been tested with Oracle databases, but should be
   straightforward to port to other databases.

   Some of the properties refer to file paths. If you are running on
   Windows, either quote back slashes ("\\") or use forward slashes.


3. Set the environment

   To run the Ant scripts succesfully, open a cmd or terminal window
   and set your environment variables as follows:

   - JAVA_HOME should point to the location of a Java 6 JSE
     environment such as that included with WebLogic Server.

   - Set ANT_HOME to an installation of Apache Ant 1.7.0 or later.

   - Add the Java and Ant bin directories to your PATH.

   - WL_HOME should point to the WebLogic Server installation
     directory below your Middleware Home. (Usually called
     wlserver_10.3).

   All of these settings can be set appropriately by running the
   wlserver_10.3.1/server/bin/setWLSEnv.cmd script from the WebLogic
   Server installation. (Unix users should source the setWLSEnv.sh
   script). Note, WebLogic Server 10.3.1 includes Apache Ant 1.7.0.


4. Initialize the database

   Ensure your database is running.

   Create a new database user using the name and password you
   specified in etc/local.properties (default: bigrez/password).
   Alternatively, run the script etc/recreate_dbuser_bigrez.sql to
   create the user 'bigrez' with the correct DB permissions.

   Change directory to the top level of the bigrez distribution and
   type "ant initialize-database". Alternatively, apply the
   initialisation script (etc/bigrez.sql) by hand.


5. Run Ant

   To build everything, type "ant" in the top level directory of the
   bigrez distribution. The EAR file will be produced in ear/output.

   Individual modules can be built using the Ant files in their
   respective directories. Type "ant -projecthelp" for a summary of
   the targets.


6. Configure WebLogic Server

   Use the WebLogic configuration wizard to create a new WebLogic
   Server 10.3.1 domain. On Windows, the wizard is available from your
   start menu. On Unix, run the wlserver_10.3.1/common/bin/config.sh
   script from the WebLogic Server installation. You can accept the
   defaults, but we suggest changing the domain name to "bigrez" and
   using "welcome1" as the password for the weblogic account.

   Start the domain's administration server.

   In a terminal window, change to the the domain's bin directory
   (e.g. c:\Oracle\Middleware\user_projects\domains\bigrez\bin) and
   run the setDomainEnv.cmd file, or on Unix, source the
   setDomainEnv.sh file, to set your local environment.

   Change to the etc directory in the bigrez distribution, and run
   "java weblogic.WLST setUpDomain.py". This will prompt you for the
   administration server connection details, then modify the domain to
   create the appropriate JDBC, JMS, Email, and security resources.
   The script uses the local.properties file you edited in step 2.


7. Deploy to WebLogic Server

   Change directory to the top level of the bigrez distribution.
   Deploy the ear from ear/output/bigrez.ear. For example:

    java weblogic.Deployer -deploy ear/output/bigrez.ear

   Alternatively, you can also use the deploy target provided in the
   top level Ant build file:

    ant deploy


8. Test BigRez

   You can access bigrez.com at one of the following URLs.

     http://localhost:7001/user
     http://localhost:7001/admin
     http://localhost:7001/webservices/

