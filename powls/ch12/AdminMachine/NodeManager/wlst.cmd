@ECHO OFF
SETLOCAL

SET WL_HOME=C:\Oracle\Middleware\wlserver_10.3
CALL "%WL_HOME%\server\bin\setWLSEnv.cmd"

if NOT "%WLST_HOME%"=="" (
	set WLST_PROPERTIES=-Dweblogic.wlstHome=%WLST_HOME% %WLST_PROPERTIES%
)

SET CLASSPATH=%CLASSPATH%;%POINTBASE_CLASSPATH%;%POINTBASE_TOOLS%

@echo.
@echo CLASSPATH=%CLASSPATH%

SET JVM_ARGS=-Dprod.props.file="%WL_HOME%\.product.properties" %WLST_PROPERTIES% %MEM_ARGS% %CONFIG_JVM_ARGS% -Dweblogic.security.TrustKeyStore=CustomTrust -Dweblogic.security.CustomTrustKeyStoreFileName=c:\\powls\\ch12\\cacerts -Dweblogic.security.CustomTrustKeyStorePassPhrase=changeit

"%JAVA_HOME%\bin\java" %JVM_ARGS% weblogic.WLST %*

ENDLOCAL
