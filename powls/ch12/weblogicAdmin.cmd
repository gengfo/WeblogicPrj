@SETLOCAL
@set WL_HOME=c:\Oracle\Middleware\wlserver_10.3
@call %WL_HOME%\server\bin\setWLSEnv.cmd
java -Dweblogic.security.TrustKeyStore=CustomTrust -Dweblogic.security.CustomTrustKeyStoreFileName=C:\powls\ch12\cacerts -Dweblogic.security.CustomTrustKeyStorePassPhrase=changeit weblogic.Admin -url %URL% -username weblogic -password weblogic1 %*
@ENDLOCAL
