connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[4]=='myrealm'):
	realmName='myrealm'
else:
	realmName=sys.argv[4]+"Realm"
	

edit()


edit()
startEdit()

cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName)

cmo.createCredentialMapper('PKICredMapper', 'weblogic.security.providers.credentials.PKICredentialMapper')
cd('CredentialMappers/PKICredMapper')
set('KeyStorePassPhrase',sys.argv[6] )
cmo.setKeyStoreFileName(sys.argv[5])

activate()
disconnect()