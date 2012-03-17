connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[4]=='myrealm'):
	realmName='myrealm'
else:
	realmName=sys.argv[4]+"Realm"
	

edit()


edit()
startEdit()

cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName)
cmo.createCredentialMapper('SAML2CredMapper', 'com.bea.security.saml2.providers.SAML2CredentialMapper')

cd('CredentialMappers/SAML2CredMapper')
cmo.setIssuerURI('http://'+sys.argv[5])
cmo.setNameQualifier('users')
activate()
disconnect()