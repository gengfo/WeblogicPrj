connect(sys.argv[1],sys.argv[2],sys.argv[3])
if (sys.argv[4]=='myrealm'):
	realmName='myrealm'
else:
	realmName=sys.argv[4]+'Realm'

print 'RealmName='+realmName

domainRuntime()
cd('DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/Realms/'+realmName)
cd ("CredentialMappers")
cd ("PKICredMapper")

resource = 'type=<remote>, protocol='+sys.argv[8]+', remoteHost='+sys.argv[9]+', remotePort='+sys.argv[10]+', path=/SAMLSPWebApp/ExampleWebServiceService'
cmo.setKeypairCredential(resource,'Ch11ProtectedUsers',Boolean('false'),None,sys.argv[5],sys.argv[6])
cmo.setCertificateCredential(resource,'Ch11ProtectedUsers',Boolean('false'),None,sys.argv[7])

cd ("../..")
cd("CredentialMappers")
cd ("SAML2CredMapper")
partner=cmo.newWSSSPPartner()
partner.setName(sys.argv[7]+"-WSS")
partner.setEnabled(true)
target=["target:-:"+sys.argv[8]+"://"+sys.argv[9]+":"+sys.argv[10]+"/SAMLSPWebApp/ExampleWebServiceService"]
targetArray=array(target,java.lang.String)
partner.setAudienceURIs(targetArray)
partner.setConfirmationMethod("urn:oasis:names:tc:SAML:2.0:cm:sender-vouches")
cmo.addSPPartner(partner)


