@SETLOCAL
@set WL_HOME=c:\Oracle\Middleware\wlserver_10.3
@call %WL_HOME%\server\bin\setWLSEnv.cmd
java  -Dweblogic.security.TrustKeyStore=CustomTrust -Dweblogic.security.CustomTrustKeyStoreFileName=c:/powls/ch12/cacerts -Dweblogic.security.CustomTrustKeyStorePassPhrase=changeit weblogic.Deployer -adminurl t3s://192.168.1.40:9002 -username weblogic -password weblogic1 %*
@ENDLOCAL