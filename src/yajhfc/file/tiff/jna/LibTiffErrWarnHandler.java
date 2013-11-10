/**
 * 
 */
package yajhfc.file.tiff.jna;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * @author jonas
 *
 */
public interface LibTiffErrWarnHandler extends Callback {

	// typedef void (*TIFFErrorHandler)(const char *module, const char *fmt, va_list ap);
	public void handle(String module, Pointer fmt, Pointer ap);
}
