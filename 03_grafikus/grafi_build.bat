@echo off

echo Removing previous builds...
rd /s /q build
rd /s /q dist

echo Creating new directories...
md build
md build\classes
md dist
md dist\images

echo Updating the PATH...
PATH=D:\Program Files\Java\jdk1.6.0_16\bin;

echo Copy the images...
copy src\images\*.png dist\images

echo Creating the class files...
javac -d build\classes src\*.java

echo Creating the executable...
cd build\classes
echo Main-Class: main>manifest.mf
jar mcf manifest.mf ..\..\dist\grafikus.jar *.class
del /q manifest.mf
cd ..
cd ..
echo Build finished
pause