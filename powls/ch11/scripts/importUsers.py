connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[5] == 'myrealm'):
	realmName = 'myrealm'
else:
	realmName = sys.argv[5]+'Realm'

print 'DomainServices/DomainRuntimeService/DomainConfiguration/'+sys.argv[4]+'/SecurityConfiguration/'+sys.argv[4]+'/Realms/'+realmName+'/'

domainRuntime()
cd('DomainServices/DomainRuntimeService/DomainConfiguration/'+sys.argv[4]+'/SecurityConfiguration/'+sys.argv[4]+'/Realms/'+realmName+'/')
cd('AuthenticationProviders/DefaultAuthenticator')

properties = util.Properties() 
cmo.importData('DefaultAtn','../../../scripts/DefaultAuthenticator.dat',properties)

disconnect();
