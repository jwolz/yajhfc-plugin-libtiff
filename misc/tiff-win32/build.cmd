set builddir=c:\users\jonas\build


if "%1"=="" (
  start /wait cmd /c build 32
  start /wait cmd /c build 64
  pause
  exit /b
)
if "%1"=="32" goto 32bit 
if "%1"=="64" goto 64bit 

echo Unknown parameter value %1
exit /b 1

:32bit
  echo "32-bit build"
  call c:\WinDDK\7600.16385.1\bin\setenv c:\WinDDK\7600.16385.1 fre x86
  set path=%path%;C:\Program Files (x86)\Microsoft Visual Studio 9.0\VC\bin
  set include=%include%;C:\WinDDK\7600.16385.1\inc\crt;C:\WinDDK\7600.16385.1\inc\api\crt\stl60
  set lib=%lib%;C:\WinDDK\7600.16385.1\lib\Crt\i386;C:\WinDDK\7600.16385.1\lib\wxp\i386
  set libtiff_suffix=
  goto build
:64bit
  call c:\WinDDK\7600.16385.1\bin\setenv c:\WinDDK\7600.16385.1 fre x64
  set path=%path%;C:\Program Files (x86)\Microsoft Visual Studio 9.0\VC\bin
  set include=%include%;C:\WinDDK\7600.16385.1\inc\crt;C:\WinDDK\7600.16385.1\inc\api\crt\stl60
  set lib=%lib%;C:\WinDDK\7600.16385.1\lib\Crt\amd64;C:\WinDDK\7600.16385.1\lib\wnet\amd64
  set libtiff_suffix=64
  goto build
:build
cd %builddir%\jbigkit
nmake /f Makefile.vc clean
if errorlevel 1 exit /b 1
nmake /f Makefile.vc
if errorlevel 1 exit /b 2
cd ..\zlib-1.2.8
nmake /f Makefile.msc clean
if errorlevel 1 exit /b 3
nmake /f Makefile.msc
if errorlevel 1 exit /b 4
cd ..\jpeg-6b
nmake /f Makefile.vc clean
if errorlevel 1 exit /b 5
nmake /f Makefile.vc
if errorlevel 1 exit /b 6
cd ..\tiff-4.0.3
nmake /f Makefile.vc clean
if errorlevel 1 exit /b 7
nmake /f Makefile.vc
if errorlevel 1 exit /b 8
cd ..
copy tiff-4.0.3\libtiff\libtiff.dll dist\libtiff%libtiff_suffix%.dll
copy tiff-4.0.3\tools\tiff2pdf.exe dist\tiff2pdf%libtiff_suffix%.exe
copy tiff-4.0.3\tools\tiff2ps.exe dist\tiff2ps%libtiff_suffix%.exe

