connect(sys.argv[1],sys.argv[2],sys.argv[3])

realmName = sys.argv[4]

print 'RealmName '+realmName

edit()
startEdit()



cd('/SecurityConfiguration/'+domainName)
cmo.createRealm(realmName+'Realm')

print 'Created Realm called '+realmName+'Realm'

cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName+'Realm')
cmo.setDeployCredentialMappingIgnored(false)


cmo.setSecurityDDModel(realmName)
cmo.setCombinedRoleMappingEnabled(true)
cmo.setDelegateMBeanAuthorization(false)
cmo.setDeployRoleIgnored(false)
cmo.setDeployPolicyIgnored(false)

cmo.createAuthenticationProvider('DefaultAuthenticator', 'weblogic.security.providers.authentication.DefaultAuthenticator')
cd('/SecurityConfiguration/'+domainName+'/Realms/CustomRolesRealm/AuthenticationProviders/DefaultAuthenticator')
cmo.setControlFlag('REQUIRED')


cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName+'Realm')
cmo.createAuthenticationProvider('DefaultIdentityAsserter', 'weblogic.security.providers.authentication.DefaultIdentityAsserter')
cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName+'Realm/AuthenticationProviders/DefaultIdentityAsserter')
set('ActiveTypes',jarray.array([String('AuthenticatedUser'), String('X.509')], String))
cmo.setUseDefaultUserNameMapper(true)


cd('/SecurityConfiguration/'+domainName+'/Realms/'+realmName+'Realm')
cmo.createAuthorizer('XACMLAuthorizer', 'weblogic.security.providers.xacml.authorization.XACMLAuthorizer')

cmo.destroyAdjudicator()
cmo.createAdjudicator('DefaultAdjudicator', 'weblogic.security.providers.authorization.DefaultAdjudicator')

cmo.createRoleMapper('XACMLRoleMapper', 'weblogic.security.providers.xacml.authorization.XACMLRoleMapper')

cmo.createAuditor('DefaultAuditor', 'weblogic.security.providers.audit.DefaultAuditor')

cmo.createCredentialMapper('DefaultCredentialMapper', 'weblogic.security.providers.credentials.DefaultCredentialMapper')

cmo.createCertPathProvider('WebLogicCertPathProvider', 'weblogic.security.providers.pk.WebLogicCertPathProvider')
cmo.createCertPathProvider('CertificateRegistry', 'weblogic.security.providers.pk.CertificateRegistry')
cmo.setCertPathBuilder(getMBean('/SecurityConfiguration/'+domainName+'/Realms/'+realmName+'Realm/CertPathProviders/CertificateRegistry'))

activate()
disconnect()


