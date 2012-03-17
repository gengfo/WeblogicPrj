connect(sys.argv[1],sys.argv[2],sys.argv[3])

edit()
startEdit()

cd('/Servers/myserver/SSL/myserver')
cmo.setHostnameVerificationIgnored(false)
cmo.setHostnameVerifier(None)

sslstate = (sys.argv[4] == "true")

cmo.setTwoWaySSLEnabled(sslstate)
cmo.setClientCertificateEnforced(sslstate)

activate()
disconnect()

