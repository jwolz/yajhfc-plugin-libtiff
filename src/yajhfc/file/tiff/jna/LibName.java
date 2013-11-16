/**
 * 
 */
package yajhfc.file.tiff.jna;

import java.io.File;

import yajhfc.Utils;

import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

/**
 * @author jonas
 *
 */
public class LibName {
	static {
    	if (Platform.isWindows()) {
    		NativeLibrary.addSearchPath(LibName.getLibTIFFName(), Utils.getApplicationDir().getAbsolutePath() + File.separatorChar + "tiff-win32");
    	}
	}
	
	public static String getLibCName() {
		if (Platform.isWindows()) {
			return "msvcrt";
		} else {
			return "c";
		}
	}
	
	public static String getLibTIFFName() {
		if (Platform.isWindows()) {
			if (Platform.is64Bit()) 
				return "libtiff64";
			else
				return "libtiff";
		} else {
			return "tiff";
		}
	}
}
