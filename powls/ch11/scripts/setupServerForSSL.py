
connect(sys.argv[1],sys.argv[2],sys.argv[3]);

edit()
startEdit()

cd('/Servers/myserver')
cmo.setKeyStores('CustomIdentityAndCustomTrust')
cmo.setCustomIdentityKeyStoreFileName(sys.argv[4]+'.jks')
cmo.setCustomIdentityKeyStoreType('JKS')
set('CustomIdentityKeyStorePassPhrase', 'password')
cmo.setCustomTrustKeyStoreFileName('ch11CustomTrust.jks')
cmo.setCustomTrustKeyStoreType('JKS')
set('CustomTrustKeyStorePassPhrase', 'password')

cd('/Servers/myserver/SSL/myserver')
cmo.setServerPrivateKeyAlias(sys.argv[4])
set('ServerPrivateKeyPassPhrase', 'password')
cmo.setExportKeyLifespan(500)
cmo.setUseServerCerts(true)
cmo.setSSLRejectionLoggingEnabled(true)
cmo.setAllowUnencryptedNullCipher(false)
cmo.setInboundCertificateValidation('BuiltinSSLValidationOnly')
cmo.setOutboundCertificateValidation('BuiltinSSLValidationAndCertPathValidators')
cmo.setHostnameVerificationIgnored(false)
cmo.setHostnameVerifier(None)
cmo.setTwoWaySSLEnabled(false)
cmo.setClientCertificateEnforced(false)


cd('/Servers/myserver')
cmo.setListenPortEnabled(true)
cmo.setJavaCompiler('javac')
cmo.setClientCertProxyEnabled(false)

cd('/Servers/myserver/SSL/myserver')
cmo.setEnabled(true)

sslport = int(sys.argv[5])
cmo.setListenPort(sslport)

activate()
disconnect()