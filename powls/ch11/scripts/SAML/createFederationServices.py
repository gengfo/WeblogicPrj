connect(sys.argv[1],sys.argv[2],sys.argv[3])

edit()
startEdit()

cd('/Servers/myserver/SingleSignOnServices/myserver')

ls()

cmo.setOrganizationName(sys.argv[4])
cmo.setContactPersonType(sys.argv[5])
cmo.setWantTransportLayerSecurityClientAuthentication(false)
cmo.setOrganizationURL(sys.argv[6])
cmo.setContactPersonGivenName(sys.argv[7])
cmo.setContactPersonCompany(sys.argv[8])
cmo.setReplicatedCacheEnabled(false)
cmo.setRecipientCheckEnabled(true)
cmo.setPublishedSiteURL(sys.argv[9])
cmo.setContactPersonEmailAddress(sys.argv[10])
cmo.setContactPersonSurName(sys.argv[11])
cmo.setWantBasicAuthClientAuthentication(false)
cmo.setContactPersonTelephoneNumber(sys.argv[12])
cmo.setEntityID(sys.argv[13])

activate()
disconnect()
