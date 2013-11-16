This directory contains a build of libtiff.dll (32 and 64bit), tiff2pdf.exe and tiff2ps.exe (both 32bit).

The libtiff has support for JPEG, ZLIB and (most importantly) JBIG

The versions used for this build are:
tiff 4.0.6  (ftp://ftp.remotesensing.org/pub/libtiff/tiff-4.0.3.zip)
jpeg 6b     (http://sourceforge.net/projects/libjpeg/files/libjpeg/6b/jpegsr6.zip/download) 
zlib 1.2.8  (http://zlib.net/zlib128.zip)
jbigkit 2.0 (http://www.cl.cam.ac.uk/~mgk25/download/jbigkit-2.0.tar.gz)

The patches used for the build (mostly for the make files) are in the misc directory of the source distribution.
The compiler used was from the WinDDK to link the files against msvcrt.dll .


