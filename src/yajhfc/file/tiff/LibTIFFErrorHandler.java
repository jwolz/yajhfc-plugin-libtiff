package yajhfc.file.tiff;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import yajhfc.file.tiff.jna.LibC;
import yajhfc.file.tiff.jna.LibTIFF;
import yajhfc.file.tiff.jna.LibTiffErrWarnHandler;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public final class LibTIFFErrorHandler implements LibTiffErrWarnHandler {
    static final Logger log = Logger.getLogger(LibTIFFErrorHandler.class.getName());
    
	private static boolean initialized = false;
	public static final LibTIFFErrorHandler ERR_HANDLER = new LibTIFFErrorHandler("error", Level.WARNING);
	public static final LibTIFFErrorHandler WARN_HANDLER = new LibTIFFErrorHandler("warning", Level.INFO);
	
	public synchronized static void initialize() {
		if (!initialized) {
		    // Initialize LibC
		    LibC.INSTANCE.hashCode();
			LibTIFF.INSTANCE.TIFFSetErrorHandler(ERR_HANDLER);
			LibTIFF.INSTANCE.TIFFSetWarningHandler(WARN_HANDLER);
			initialized = true;
		}
	}
	
	/**
	 * Throws an IOException with an description of the last error
	 * @param baseMessage
	 * @return
	 */
	public static IOException createIOException(String baseMessage) {
		String lastMsg = ERR_HANDLER.getLastMessage();
		if (baseMessage != null) {
			return new IOException(baseMessage + ": " + lastMsg);
		} else {
			return new IOException(lastMsg);
		}
	}

	/////////////////////////////////////////
	private final String kind;
	private final Level logLevel;
	private String lastMessage;

	private LibTIFFErrorHandler(String kind, Level logLevel) {
		super();
		this.kind = kind;
		this.logLevel = logLevel;
	}

	@Override
	public void handle(String module, Pointer fmt, Pointer ap) {
		byte[] buf = new byte[512];
		int rv = LibC.INSTANCE.vsnprintf(buf, buf.length, fmt, ap);
		
		if (rv != 0) {
			lastMessage = module + ": " + Native.toString(buf);
			log.log(logLevel, "libtiff " + kind + ": " + lastMessage);
		} else {
			log.severe("vsnprintf return code: " + rv);
		}
	}
	
	public String getLastMessage() {
		return lastMessage;
	}
}
