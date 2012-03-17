connect(sys.argv[1],sys.argv[2],sys.argv[3])


if (sys.argv[4] == 'myrealm'):
	realmName = 'myrealm'
else:
	realmName = sys.argv[4]+'Realm'

edit()
startEdit()

cd('/SecurityConfiguration/'+domainName)
cmo.setCrossDomainSecurityEnabled(false)
cmo.setAnonymousAdminLookupEnabled(false)
cmo.setDefaultRealm(getMBean('/SecurityConfiguration/'+domainName+'/Realms/'+realmName))

activate()
disconnect()