/**
 * 
 */
package yajhfc.file.tiff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

/**
 * @author jonas
 *
 */
public class LibTIFFImageReader implements TIFFImageReader {
    protected LibTIFFFile tifFile;
    
    /**
     * @throws IOException 
     * 
     */
    public LibTIFFImageReader(File tif) throws IOException {
        tifFile = new LibTIFFFile(tif);
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Image> iterator() {
        return new TiffIterator();
    }

    /* (non-Javadoc)
     * @see yajhfc.file.tiff.TIFFImageReader#close()
     */
    @Override
    public void close() {
        tifFile.close();
    }

    protected class TiffIterator implements Iterator<Image> {
        boolean nextValid = true;
        
        @Override
        public boolean hasNext() {
            return nextValid;
        }

        @Override
        public Image next() {
            Image img;
            try {
                BufferedImage bi = tifFile.readImage();
                img = Image.getInstance(bi, null);
                img.setDpi((int)tifFile.getResolutionX(), (int)tifFile.getResolutionY());
                nextValid = tifFile.nextPage();
                return img;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BadElementException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
}
