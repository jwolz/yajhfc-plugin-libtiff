/**
 * 
 */
package yajhfc.file.tiff.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * @author jonas
 *
 */
public interface LibC extends Library {
	LibC INSTANCE = (LibC)Native.loadLibrary(LibName.getLibCName(), LibC.class);
	
	//int vsnprintf(char *str, size_t size, const char *format, va_list ap);
	public int vsnprintf(byte[] buf, int size, Pointer format, Pointer ap);
}
