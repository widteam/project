@echo off

echo Removing previous builds...
rd /s /q build
rd /s /q dist

echo Creating new directories...
md build
md build\classes
md build\classes\images
md dist

echo Updating the PATH...
PATH=C:\Program Files\Java\jdk1.6.0_23\bin;

echo Copy the images...
copy src\images\*.png build\classes\images
copy src\images\*.gif build\classes\images

echo Creating the class files...
javac -d build\classes src\*.java

echo Creating the executable...
cd build\classes
echo Main-Class: Main>manifest.mf
jar mcf manifest.mf ..\..\dist\grafikus.jar *.class images\*.png images\*.gif
del /q manifest.mf
cd ..
cd ..
echo Build finished
pause