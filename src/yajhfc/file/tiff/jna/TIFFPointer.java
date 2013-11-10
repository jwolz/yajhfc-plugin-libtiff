package yajhfc.file.tiff.jna;

import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class TIFFPointer extends PointerType {

    public boolean isNull() {
        return getPointer().equals(Pointer.NULL);
    }
}
