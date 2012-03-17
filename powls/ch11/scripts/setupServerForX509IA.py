connect(sys.argv[1],sys.argv[2],sys.argv[3])

edit()
startEdit()

cd('/SecurityConfiguration/ch11_'+sys.argv[4]+'/Realms/myrealm/AuthenticationProviders/DefaultIdentityAsserter')
set('ActiveTypes',jarray.array([String('AuthenticatedUser'), String('X.509')], String))
cmo.setUseDefaultUserNameMapper(true)
activate()
disconnect()
