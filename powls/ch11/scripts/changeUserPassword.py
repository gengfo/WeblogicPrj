connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[7] == 'myrealm'):
	realmName = 'myrealm'
else:
	realmName = sys.argv[7]+'Realm'

cd('SecurityConfiguration/'+sys.argv[4]+'/Realms/'+realmName+'/')
cd('AuthenticationProviders/DefaultAuthenticator')
cmo.resetUserPassword(sys.argv[5],sys.argv[6])
disconnect()



