/**
 * 
 */
package yajhfc.file.tiff;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

import yajhfc.Utils;
import yajhfc.file.tiff.TIFFImageReader;
import yajhfc.file.tiff.TIFFImageReaderFactory;
import yajhfc.file.tiff.jna.LibName;
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
        TIFFImageReaderFactory.DEFAULT = new TIFFImageReaderFactory() {
          @Override
            public TIFFImageReader createReader(File tiff) throws IOException {
        	  	if (!Launcher2.isPropertyTrue(USE_NATIVE_TIFF_PROPERTY, true)) {
        	  		log.fine("Using iText TIFF support...");
        	  		return super.createReader(tiff);
        	  	} else {
        	  		log.fine("Using native (libtiff) TIFF support...");
        	  		return new LibTIFFImageReader(tiff);
        	  	}
            }  
        };
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
