@echo off
setlocal
set cp=.
if "!cp!" NEQ "." (
  cmd /e:on /v:on /c "%0 %*"
) else ( 
  for /r %~dps0 %%i in (*.jar) do (
    set cp=!cp!;%%i;  
  )
  java -DWF_JNI_MODE=false -cp "!cp!" eu.w4.contrib.genactors.Main %*
  if errorlevel 1 (
    endlocal
    exit 1
  )
  endlocal
  exit 0  
)
