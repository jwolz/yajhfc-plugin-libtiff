README for YajHFC native TIFF plugin
====================================

The YajHFC native TIFF plugin is used in conjunction with the YajHFC PDF plugin.

It uses the native libtiff to read TIFF files.
This offers a much wider format support compared to the TIFF reading support included in iText

CONFIGURATION
-------------

You can set the following Java system properties:
yajhfc.file.tiff.use_native_tiff=true/false   - Enables or disables the native TIFF support (default true)



INSTALLATION
-------------

If you use the Windows setup:
Install the newest version using the setup program. This plugin is included 
in the default installation for YajHFC 0.5.5 and up.

If you use deb packages:
Install the yajhfc-pdfplugin package.

Else:
1. Unpack this ZIP file into the same folder as the yajhfc.jar.
2. Start YajHFC, go to Options->Plugins&JDBC and click "Add plugin".
3. Select yajhfc-plugin-libtiff.jar 
4. Restart YajHFC

   