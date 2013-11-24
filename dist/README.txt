README for YajHFC native TIFF plugin
====================================

The YajHFC native TIFF plugin is used in conjunction with the YajHFC PDF plugin.

It uses the native libtiff (http://www.libtiff.org/) to read TIFF files.
This offers a much wider and more robust format support compared to the TIFF reading support included in iText.


CONFIGURATION
-------------

You can set the following Java system properties:
yajhfc.file.tiff.use_native_tiff=true/false   - Enables or disables the native TIFF support (default is true)
jna.library.path                              - Can be used to specify an additional search path for the libtiff library


INSTALLATION
-------------

If you use the Windows setup:
Install the newest version using the setup program. This plugin is included 
in the default installation for YajHFC 0.5.5 and up.

If you use deb packages:
Install the yajhfc-pdfplugin package.

Manual installation/other platforms:
This ZIP file includes 32bit and 64bit Windows builds of libtiff (with JPEG, JBIG and zlib support) in the tiff-win32 directory.
On other platforms, you will need to install a native libtiff to use this plugin. 
(On Linux the respective package usually will be called something like "libtiff", for example "libtiff5" on Debian).


After you have installed a suitable libtiff, please perform the following steps:
1. Unpack this ZIP file into the same folder as the yajhfc.jar.
2. Start YajHFC, go to Options->Plugins&JDBC and click "Add plugin".
3. Select yajhfc-plugin-libtiff.jar 
4. Restart YajHFC

   