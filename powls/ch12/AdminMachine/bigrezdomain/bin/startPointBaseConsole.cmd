@ECHO OFF

@REM WARNING: This file is created by the Configuration Wizard.
@REM Any changes to this script may be lost when adding extensions to this configuration.

SETLOCAL

@REM Call setDomainEnv here to get the correct pointbase port

set DOMAIN_HOME=C:\powls\ch12\AdminMachine\bigrezdomain
for %%i in ("%DOMAIN_HOME%") do set DOMAIN_HOME=%%~fsi

call "%DOMAIN_HOME%\bin\setDomainEnv.cmd"

call "%WL_HOME%\common\bin\startPointBaseConsole.cmd" -port=%POINTBASE_PORT%



ENDLOCAL