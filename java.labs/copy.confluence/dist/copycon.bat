@echo off

set JAVA_EXE=java
set COPY_CON=copy.confluence-0.1-jar-with-dependencies.jar

for /F "tokens=*" %%A in (%1) do %JAVA_EXE% -jar %COPY_CON% %%A

