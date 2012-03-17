connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[4]=='myrealm'):
	realmName='myrealm'
else:
	realmName=sys.argv[4]+'Realm'

print 'RealmName='+realmName

domainRuntime()
cd('DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/Realms/'+realmName)
cd('AuthenticationProviders/SAML2IdentityAsserter')
partner = cmo.newWSSIdPPartner()
partner.setName(sys.argv[5]+"-WSS")
partner.setConfirmationMethod("urn:oasis:names:tc:SAML:2.0:cm:sender-vouches")
partner.setEnabled(true)
target=["target:-:/SAMLSPWebApp/ExampleWebServiceService"]
targetArray=array(target,java.lang.String)
partner.setAudienceURIs(targetArray)
partner.setIssuerURI("http://"+sys.argv[5])
cmo.addIdPPartner(partner)

disconnect()
