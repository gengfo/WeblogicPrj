connect(sys.argv[1],sys.argv[2],sys.argv[3])

if (sys.argv[4] == 'myrealm'):
	realmName = 'myrealm'
else:
	realmName = sys.argv[4]+'Realm'

print realmName
domainRuntime()
cd('DomainServices/DomainRuntimeService/DomainConfiguration/'+domainName+'/SecurityConfiguration/'+domainName+'/Realms')

cd(realmName)
cd('CertPathBuilder')
cd('CertificateRegistry')


cmo.registerCertificate(sys.argv[5],'../../keys/'+sys.argv[5]+'_cert.der')