/**
 * 
 */
package yajhfc.file.tiff;

import java.io.File;
import java.io.IOException;

import yajhfc.file.tiff.TIFFImageReader;
import yajhfc.file.tiff.TIFFImageReaderFactory;
import yajhfc.plugin.PluginManager;

/**
 * @author jonas
 *
 */
public class EntryPoint {
    /**
     * Plugin initialization method.
     * The name and signature of this method must be exactly as follows 
     * (i.e. it must always be "public static boolean init(int)" )
     * @param startupMode the mode YajHFC is starting up in. The possible
     *    values are one of the STARTUP_MODE_* constants defined in yajhfc.plugin.PluginManager
     * @return true if the initialization was successful, false otherwise.
     */
    public static boolean init(int startupMode) {
        TIFFImageReaderFactory.DEFAULT = new TIFFImageReaderFactory() {
          @Override
            public TIFFImageReader createReader(File tiff) throws IOException {
                return new LibTIFFImageReader(tiff);
            }  
        };
        return true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        PluginManager.internalPlugins.add(EntryPoint.class);
        yajhfc.pdf.EntryPoint.main(args);

    }

}
