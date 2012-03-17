connect(userConfigFile='C:\powls\ch12\server-WebLogicConfig.properties',
        userKeyFile='C:\powls\ch12\server-WebLogicKey.properties',
        url='t3://192.168.1.40:7001')
targetDataSourceName='BigRezJTADataSource'
servers=domainRuntimeService.getServerRuntimes()
if (len(servers) > 0):
	for server in servers:
		jdbcServiceRT = server.getJDBCServiceRuntime()
		dataSources = jdbcServiceRT.getJDBCDataSourceRuntimeMBeans()
		if (len(dataSources) > 0):
			for dataSource in dataSources:
				if(dataSource.getName() == targetDataSourceName):
					dataSource.reset()


