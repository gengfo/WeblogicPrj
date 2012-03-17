#
# WLST Script to set up a WebLogic Server domain for BigRez.
#

from java.io import FileInputStream
from java.util import Properties

#
# Load the properties file.
#
def loadProperties(fileName):
    properties = Properties()
    input = FileInputStream(fileName)
    properties.load(input)
    input.close()

    # Dictionaries behave better that java.util.Properties.
    result= {}
    for entry in properties.entrySet(): result[entry.key] = entry.value

    return result

properties = loadProperties("local.properties")

#
# WLST online script to create the BigRez.com resources.
#

connect()
adminServerName = cmo.adminServerName

#
# Obtain the edit lock.
#
edit()
startEdit()

try:
    domainName = cmo.name
    cd("Servers/%s" % adminServerName)
    adminServer = cmo

    #
    # Delete old resources, if they exist.
    #
    def deleteIgnoringExceptions(mbean):
        try: delete(mbean)
        except: pass

    cd("/JDBCSystemResources")
    deleteIgnoringExceptions("BigRezJTADataSource")
    deleteIgnoringExceptions("BigRezNonJTADataSource")

    cd("/JMSSystemResources")
    deleteIgnoringExceptions("BigRezJMSModule")
    cd("/JMSServers")
    deleteIgnoringExceptions("BigRezJMSServer")

    cd("/MailSessions")
    deleteIgnoringExceptions("BigRezMailSession")

    #
    # JDBC data sources
    #

    cd("/JDBCSystemResources")

    jtaDataSource = create("BigRezJTADataSource", "JDBCSystemResource")
    cd("%s/JDBCResource/%s" % (jtaDataSource.name, jtaDataSource.name))
    cmo.name = jtaDataSource.name

    cd("JDBCConnectionPoolParams/%s" % jtaDataSource.name)
    cmo.testConnectionsOnReserve = 1
    cmo.testTableName = "SQL SELECT 1 FROM dual"
    cmo.connectionCreationRetryFrequencySeconds=30

    cd("../../JDBCDataSourceParams/%s" % jtaDataSource.name)
    cmo.JNDINames = ("bigrez.datasource.jta",)

    cd("../../JDBCDriverParams/%s" % jtaDataSource.name)
    cmo.driverName = properties["database.xaDriver"]
    cmo.passwordEncrypted = properties["database.password"]
    cmo.url = properties["database.url"]

    cd("Properties/%s" % jtaDataSource.name)
    userProperty = create("user", "Property")
    userProperty.value = properties["database.user"]

    jtaDataSource.targets = (adminServer,)

    cd("/JDBCSystemResources")

    nonJTADataSource = create("BigRezNonJTADataSource", "JDBCSystemResource")
    cd("%s/JDBCResource/%s" % (nonJTADataSource.name, nonJTADataSource.name))
    cmo.name = nonJTADataSource.name

    cd("JDBCConnectionPoolParams/%s" % nonJTADataSource.name)
    cmo.testConnectionsOnReserve = 1
    cmo.testTableName = "SQL SELECT 1 FROM dual"

    cd("../../JDBCDataSourceParams/%s" % nonJTADataSource.name)
    cmo.JNDINames = ("bigrez.datasource.nonjta",)
    cmo.globalTransactionsProtocol = "None"

    cd("../../JDBCDriverParams/%s" % nonJTADataSource.name)
    cmo.driverName = properties["database.driver"]
    cmo.passwordEncrypted = properties["database.password"]
    cmo.url = properties["database.url"]

    cd("Properties/%s" % nonJTADataSource.name)
    userProperty = create("user", "Property")
    userProperty.value = properties["database.user"]

    nonJTADataSource.targets = (adminServer,)

    #
    # JMS
    #

    cd("/JMSServers")
    jmsServer = create("BigRezJMSServer", "JMSServer")
    jmsServer.targets = (adminServer,)

    cd("/JMSSystemResources")

    jmsModule = create("BigRezJMSModule", "JMSSystemResource")
    jmsModule.targets = (adminServer,)

    cd(jmsModule.name)
    sd = create("JMSServerSubDeployment", "SubDeployment")
    sd.targets = (jmsServer,)

    cd("JMSResource/%s" % jmsModule.name)

    cf = create("BigRezConnectionFactory", "ConnectionFactory")
    cf.JNDIName="bigrez.jms.connectionfactory"
    cf.defaultTargetingEnabled = 1
    cf.transactionParams.setXAConnectionFactoryEnabled(1)

    errorQueue = create("BigRezEmailErrorQueue", "Queue")
    errorQueue.subDeploymentName = sd.name

    q = create("BigRezEmailQueue", "Queue")
    q.JNDIName="bigrez.jms.emailQueue"
    q.subDeploymentName = sd.name
    cd("Queues/%s/DeliveryFailureParams/%s" % (q.name, q.name))
    cmo.errorDestination = errorQueue
    cmo.redeliveryLimit = 5
    cd("../../DeliveryParamsOverrides/%s" % q.name)
    cmo.redeliveryDelay = 60000

    #
    # Email
    #

    cd("/MailSessions")
    mail = create("BigRezMailSession", "MailSession")
    mail.JNDIName="bigrez.mail.session"

    mail.properties = Properties()

    for k,v in properties.items():
        if k.startswith("mail."): mail.properties[k] = v

    mail.targets = (adminServer,)

    activate()
except:
    cancelEdit("y")
    raise

#
# Hotel administration users
#

# Online security changes are made via the the runtime mbeans, not an edit session.
serverConfig()

groupName = "BigRezAdministrators"

cd("/SecurityConfiguration/%s/DefaultRealm/myrealm/AuthenticationProviders/DefaultAuthenticator"
   % domainName)

if not cmo.groupExists(groupName): cmo.createGroup(groupName, "Hotel Administrators")

for name in ("peter", "bob", "mark", "anna"):
    if not cmo.userExists(name):
        u = cmo.createUser(name, "password1", "Administrator")
        cmo.addMemberToGroup(groupName, name)



