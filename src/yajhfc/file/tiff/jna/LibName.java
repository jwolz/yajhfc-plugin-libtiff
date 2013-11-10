/**
 * 
 */
package yajhfc.file.tiff.jna;

import com.sun.jna.Platform;

/**
 * Library names for different platforms
 * 
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
