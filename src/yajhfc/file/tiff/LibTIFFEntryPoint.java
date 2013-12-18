/**
 * 
 */
package yajhfc.file.tiff;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import yajhfc.file.tiff.jna.LibTIFF;
import yajhfc.launch.Launcher2;
import yajhfc.plugin.PluginManager;

/**
 * @author jonas
 *
 */
public class LibTIFFEntryPoint {
	static final Logger log = Logger.getLogger(LibTIFFEntryPoint.class.getName());
    public static final String USE_NATIVE_TIFF_PROPERTY = "yajhfc.file.tiff.use_native_tiff";

	/**
     * Plugin initialization method.
     * The name and signature of this method must be exactly as follows 
     * (i.e. it must always be "public static boolean init(int)" )
     * @param startupMode the mode YajHFC is starting up in. The possible
     *    values are one of the STARTUP_MODE_* constants defined in yajhfc.plugin.PluginManager
     * @return true if the initialization was successful, false otherwise.
     */
    public static boolean init(int startupMode) {
        log.fine("Initializing libtiff plugin...");
        yajhfc.pdf.EntryPoint.haveNativeLibTIFF = true;
        try {
            String tiffVersion = LibTIFF.INSTANCE.TIFFGetVersion();
            log.info("libtiff version: " + tiffVersion);
            yajhfc.pdf.EntryPoint.nativeTIFFVersion = tiffVersion;

            TIFFImageReaderFactory.DEFAULT = new TIFFImageReaderFactory() {
                @Override
                public TIFFImageReader createReader(File tiff) throws IOException {
                    if (Launcher2.isPropertyTrue(USE_NATIVE_TIFF_PROPERTY, true) &&
                            yajhfc.pdf.EntryPoint.getOptions().enableNativeLibTIFF) {
                        log.fine("Using native (libtiff) TIFF support...");
                        return new LibTIFFImageReader(tiff);
                    } else {
                        log.fine("Using iText TIFF support...");
                        return super.createReader(tiff);
                    }
                }  
            };
        } catch (Throwable e) {
            log.log(Level.SEVERE, "Can not load libtiff, plugin disabled", e);
            yajhfc.pdf.EntryPoint.nativeTIFFVersion = "libtiff load error: " + e.toString();
        }
        return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        PluginManager.internalPlugins.add(LibTIFFEntryPoint.class);
        yajhfc.pdf.EntryPoint.main(args);

    }

}
