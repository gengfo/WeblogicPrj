@SETLOCAL
@set WL_HOME=C:\Oracle\Middleware\wlserver_10.3
@set WLST_PROPERTIES=-Dweblogic.security.TrustKeyStore=CustomTrust -Dweblogic.security.CustomTrustKeyStoreFileName=c:/powls/ch12/cacerts -Dweblogic.security.CustomTrustKeyStorePassPhrase=changeit
@
@call %WL_HOME%\common\bin\wlst.cmd %*
@ENDLOCAL
