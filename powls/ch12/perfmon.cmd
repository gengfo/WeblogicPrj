@SETLOCAL
@set POWLS_HOME=c:\powls
@set WL_HOME=C:\oracle\middleware\wlserver_10.3
@call %WL_HOME%\server\bin\setWLSenv.cmd
@set CLASSPATH=%POWLS_HOME%\ch12\classes;%CLASSPATH%
java -Dweblogic.security.TrustKeyStore=CustomTrust -Dweblogic.security.CustomTrustKeyStoreFileName=%POWLS_HOME%\ch12\cacerts -Dweblogic.security.CustomTrustKeyStorePassPhrase=changeit professional.weblogic.ch12.example1.WebLogicPerformanceMonitor %POWLS_HOME%\ch12\perfmon.properties
@ENDLOCAL