set builddir=c:\users\jonas\build
set path=%path%;C:\Program Files (x86)\Microsoft Visual Studio 9.0\VC\bin;C:\Program Files (x86)\GnuWin32\bin

cd %builddir%\jbigkit
nmake /f Makefile.vc clean
if errorlevel 1 exit /b 1
cd ..\zlib-1.2.8
nmake /f Makefile.msc clean
if errorlevel 1 exit /b 2
cd ..\jpeg-6b
nmake /f Makefile.vc clean
if errorlevel 1 exit /b 3
cd ..\tiff-4.0.3
nmake /f Makefile.vc clean
if errorlevel 1 exit /b 4
cd ..

diff -rupN -x *.obj -x *.lib -x *.dll -x *.exe -x RCa03388 zlib-1.2.8 orig/zlib-1.2.8 > zlib-1.2.8.patch
diff -rupN -x *.obj -x *.lib -x *.dll -x *.exe jpeg-6b orig/jpeg-6b > jpeg-6b.patch
diff -rupN -x *.obj -x *.lib -x *.dll -x *.exe jbigkit orig/jbigkit > jbigkit.patch
diff -rupN -x *.obj -x *.lib -x *.dll -x *.exe -x *.ilk -x *.exp tiff-4.0.3 orig/tiff-4.0.3 > tiff-4.0.3.patch


