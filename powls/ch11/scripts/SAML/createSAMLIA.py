connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[4]=='myrealm'):
	realmName='myrealm'
else:
	realmName=sys.argv[4]+"Realm"
	

edit()
startEdit()

cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName)
cmo.createAuthenticationProvider('SAML2IdentityAsserter', 'com.bea.security.saml2.providers.SAML2IdentityAsserter')

activate()

startEdit()
