@echo off
if not exist bin mkdir bin

javac -cp "lib/*" -d bin src\**\*.java

if %errorlevel%==0 (
    echo ✅ Compilation terminee avec succes !
) else (
    echo ❌ Erreur de compilation.
)
