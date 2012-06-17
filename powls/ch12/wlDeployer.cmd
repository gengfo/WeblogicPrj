@SETLOCAL
@set WL_HOME=c:\Oracle\Middleware\wlserver_10.3
@call %WL_HOME%\server\bin\setWLSEnv.cmd
java weblogic.Deployer -adminurl t3://192.168.1.40:7001 -username weblogic -password weblogic1 %*
@ENDLOCAL