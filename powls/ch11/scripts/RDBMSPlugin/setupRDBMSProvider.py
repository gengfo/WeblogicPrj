connect(sys.argv[1],sys.argv[2],sys.argv[3])
edit()
startEdit()

cd('/')
cmo.createJDBCSystemResource('CH11DataSource')

cd('/JDBCSystemResources/CH11DataSource/JDBCResource/CH11DataSource')
cmo.setName('CH11DataSource')

cd('/JDBCSystemResources/CH11DataSource/JDBCResource/CH11DataSource/JDBCDataSourceParams/CH11DataSource')
set('JNDINames',jarray.array([], String))

cd('/JDBCSystemResources/CH11DataSource/JDBCResource/CH11DataSource/JDBCDriverParams/CH11DataSource')
cmo.setUrl(sys.argv[4])
cmo.setDriverName(sys.argv[5])
set('Password', sys.argv[6])

cd('/JDBCSystemResources/CH11DataSource/JDBCResource/CH11DataSource/JDBCConnectionPoolParams/CH11DataSource')
cmo.setTestTableName('SQL SELECT 1 FROM DUAL')

cd('/JDBCSystemResources/CH11DataSource/JDBCResource/CH11DataSource/JDBCDriverParams/CH11DataSource/Properties/CH11DataSource')
cmo.createProperty('user')

cd('/JDBCSystemResources/CH11DataSource/JDBCResource/CH11DataSource/JDBCDriverParams/CH11DataSource/Properties/CH11DataSource/Properties/user')
cmo.setValue(sys.argv[7])

cd('/JDBCSystemResources/CH11DataSource/JDBCResource/CH11DataSource/JDBCDataSourceParams/CH11DataSource')
cmo.setGlobalTransactionsProtocol('TwoPhaseCommit')

cd('/SystemResources/CH11DataSource')
set('Targets',jarray.array([ObjectName('com.bea:Name=myserver,Type=Server')], ObjectName))

activate()
disconnect()
