
connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[4]=='myrealm'):
	realmName='myrealm'
else:
	realmName=sys.argv[4]+'Realm'

print realmName

edit()
startEdit()

cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName)
cmo.createAuthenticationProvider('CH11RDBMSAuthenticator', 'weblogic.security.providers.authentication.CustomDBMSAuthenticator')

cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName+'/AuthenticationProviders/CH11RDBMSAuthenticator')
cmo.setControlFlag('OPTIONAL')
cmo.setPlaintextPasswordsEnabled(true)
cmo.setDataSourceName('CH11DataSource')
cmo.setPluginClassName('professional.weblogic.ch11.examples.server.rdbmsplugin.ExampleRDBMSPlugin')

props = java.util.Properties()
props.put("Ch11RDBMSProp1","RDBMSValue1")


cmo.setPluginProperties(props)


cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName+'/AuthenticationProviders/DefaultAuthenticator')
cmo.setControlFlag('OPTIONAL')

activate()

disconnect()
