connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[4]=='myrealm'):
	realmName='myrealm'
else:
	realmName=sys.argv[4]+'Realm'

print 'RealmName='+realmName

domainRuntime()
cd('DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/Realms/'+realmName)
cd('CredentialMappers/SAML2CredMapper')

partner = cmo.consumeSPPartnerMetadata(sys.argv[5])
partner.setName(sys.argv[6]+'-SSO')

cmo.addSPPartner(partner)

disconnect()