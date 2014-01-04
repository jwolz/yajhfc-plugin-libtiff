package yajhfc.file.tiff.jna;

import java.nio.Buffer;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;


public interface LibTIFF extends Library {
    LibTIFF INSTANCE = (LibTIFF)Native.loadLibrary(LibName.getLibTIFFName(), LibTIFF.class);
    
    //    typedef uint32 ttag_t;          /* directory tag */
    //    typedef uint16 tdir_t;          /* directory index */
    //    typedef uint16 tsample_t;       /* sample number */
    //    typedef uint32 tstrile_t;       /* strip or tile number */
    //    typedef tstrile_t tstrip_t;     /* strip number */
    //    typedef tstrile_t ttile_t;      /* tile number */
    //    typedef int32 tsize_t;          /* i/o size in bytes */
    //    typedef void* tdata_t;          /* image data ref */
    //    typedef uint32 toff_t;          /* file offset */
    
    //TIFF* TIFFOpen(const char*, const char*);
    TIFFPointer TIFFOpen(String filename, String mode);
    
    //TIFFClose(TIFF*);
    void TIFFClose(TIFFPointer handle);
    
    //int TIFFReadDirectory(TIFF*);
    int TIFFReadDirectory(TIFFPointer handle);
    
    //int TIFFGetField(TIFF*, ttag_t, ...);
    int TIFFGetField(TIFFPointer handle, int tag_t, ByReference... args);
    
    //int TIFFReadRGBAImage(TIFF*, uint32, uint32, uint32*, int);
    int TIFFReadRGBAImage(TIFFPointer handle, int width, int height, int[] raster, int stopOnError);
    
    //int TIFFReadRGBAImageOriented(TIFF *tif, uint32 width, uint32 height, uint32 *raster, int orientation, int stopOnError)
    int TIFFReadRGBAImageOriented(TIFFPointer tif, int width, int height, int[] raster, int orientation, int stopOnError);
    
    
    //  tsize_t TIFFStripSize(TIFF*);
    int TIFFStripSize(TIFFPointer tif);
    //  tstrip_t TIFFNumberOfStrips(TIFF*);
    int TIFFNumberOfStrips(TIFFPointer tif);
    //  tsize_t TIFFReadEncodedStrip(TIFF*, tstrip_t, tdata_t, tsize_t);
    int TIFFReadEncodedStrip(TIFFPointer tif, int strip, Buffer buf, int size);
    
    //TIFFErrorHandler TIFFSetErrorHandler(TIFFErrorHandler handler);
    Pointer TIFFSetErrorHandler(LibTiffErrWarnHandler handler);
    //TIFFWarningHandler TIFFSetWarningHandler(TIFFWarningHandler handler);
    Pointer TIFFSetWarningHandler(LibTiffErrWarnHandler handler);
    
    String TIFFGetVersion();
    
    int TIFFIsTiled(TIFFPointer handle);
}
