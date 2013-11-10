/**
 * 
 */
package yajhfc.file.tiff.jna;

import com.sun.jna.Platform;

/**
 * @author jonas
 *
 */
public class LibName {
	public static String getLibCName() {
		if (Platform.isWindows()) {
			return "msvcrt";
		} else {
			return "c";
		}
	}
	
	public static String getLibTIFFName() {
		if (Platform.isWindows()) {
			return "libtiff";
		} else {
			return "tiff";
		}
	}
}
