@echo off

if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK

echo ERROR: JAVA_HOME is not set.
echo ERROR: Set JAVA_HOME to the directory of your local JDK.
exit /B

:JAVA_HOME_OK
@"%JAVA_HOME%"\bin\java -jar libs/xsdgen-app-1.3.1.jar %1 %2 %3 %4 %5 %6 %7 %8 %9
