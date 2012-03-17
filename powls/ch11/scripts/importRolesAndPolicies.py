

if (sys.argv[5] == 'myrealm'):
	realmName = 'myrealm'
	exit()
else:
	realmName = sys.argv[5]+'Realm'


connect(sys.argv[1],sys.argv[2],sys.argv[3])

print 'DomainServices/DomainRuntimeService/DomainConfiguration/'+sys.argv[4]+'/SecurityConfiguration/'+sys.argv[4]+'/Realms/'+realmName+'/'

domainRuntime()
cd('DomainServices/DomainRuntimeService/DomainConfiguration/'+sys.argv[4]+'/SecurityConfiguration/'+sys.argv[4]+'/Realms/'+realmName+'/')
cd('Authorizers/XACMLAuthorizer')

properties = util.Properties() 

importFilePath = '../../../scripts/'+sys.argv[5]+'/'+'XACMLAuthorizer.dat'
cmo.importData('XACML',importFilePath,properties)

cd('DomainServices/DomainRuntimeService/DomainConfiguration/'+sys.argv[4]+'/SecurityConfiguration/'+sys.argv[4]+'/Realms/'+realmName+'/')
cd('RoleMappers/XACMLRoleMapper')

properties = util.Properties() 
importFilePath = '../../../scripts/'+sys.argv[5]+'/XACMLRoleMapper.dat'
cmo.importData('XACML',importFilePath,properties)


disconnect();