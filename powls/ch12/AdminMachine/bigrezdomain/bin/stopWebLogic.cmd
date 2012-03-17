@ECHO OFF

@REM WARNING: This file is created by the Configuration Wizard.
@REM Any changes to this script may be lost when adding extensions to this configuration.

SETLOCAL

if NOT "%1"=="" (
	set userID=username='%1',
	shift
) else (
	if NOT "%userID%"=="" (
		set userID=username='%userID%',
	)
)

if NOT "%1"=="" (
	set password=password='%1',
	shift
) else (
	if NOT "%password%"=="" (
		set password=password='%password%',
	)
)

@REM set ADMIN_URL

if NOT "%1"=="" (
	set ADMIN_URL=%1
	shift
) else (
	if "%ADMIN_URL%"=="" (
		set ADMIN_URL=t3://diablo:7001
	)
)

@REM Call setDomainEnv here because we want to have shifted out the environment vars above

set DOMAIN_HOME=C:\powls\ch12\AdminMachine\bigrezdomain
for %%i in ("%DOMAIN_HOME%") do set DOMAIN_HOME=%%~fsi

@REM Read the environment variable from the console.

if "%doExit%"=="true" (
	set exitFlag=doExit
) else (
	set exitFlag=noExit
)

call "%DOMAIN_HOME%\bin\setDomainEnv.cmd" %exitFlag%

echo connect^(%userID% %password% url='%ADMIN_URL%',adminServerName='%SERVER_NAME%'^) >"shutdown.py" 
echo shutdown^('%SERVER_NAME%','Server'^) >>"shutdown.py" 
echo exit^(^) >>"shutdown.py" 

echo Stopping Weblogic Server...

%JAVA_HOME%\bin\java %JAVA_OPTIONS% weblogic.WLST shutdown.py  2>&1 

echo Done

@REM Exit this script only if we have been told to exit.

if "%doExitFlag%"=="true" (
	exit
)



ENDLOCAL