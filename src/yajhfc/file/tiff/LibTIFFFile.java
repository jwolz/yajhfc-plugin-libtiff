package yajhfc.file.tiff;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import yajhfc.file.tiff.jna.LibTIFF;
import yajhfc.file.tiff.jna.TIFFPointer;
import yajhfc.tiff.TIFFConstants;

import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;

/**
 * Read TIFF files using libtiff and JNA
 * 
 * @author jonas
 *
 */
public class LibTIFFFile implements TIFFConstants {
    static final Logger log = Logger.getLogger(LibTIFFFile.class.getName());
    
    protected static int[] ABGR_bandmasks = new int[] { 0x000000ff , 0x0000ff00, 0x00ff0000, 0xff000000};
    protected static ColorModel ColorModel_AGBR = new DirectColorModel(32,
            0x000000ff,   // Red
            0x0000ff00,   // Green
            0x00ff0000,   // Blue
            0xff000000    // Alpha
            );
    private static byte[] bw = {(byte) 0xff, (byte) 0};  
    protected static IndexColorModel ColorModel_BlackAndWhite = new IndexColorModel(1, 2, bw, bw, bw);
    
    protected TIFFPointer tiffPointer;
    protected int page=-1;
    
    public LibTIFFFile() {
        super();
        try {  
        	LibTIFFErrorHandler.initialize();
        } catch (UnsatisfiedLinkError e) {
        	throw new RuntimeException(e);
        } catch (NoClassDefFoundError e) {
        	throw new RuntimeException(e);
        } 
    }

    public LibTIFFFile(File f) throws IOException {
        this();
        open(f);
    }
    
    /**
     * Opens a TIFF file read only
     * @param f
     * @throws IOException
     */
    public void open(File f) throws IOException {
        open(f, "r");
    }
    
    /**
     * Opens a TIFF file with the specified mode
     * @param f
     * @param mode
     * @throws IOException
     */
    public void open(File f, String mode) throws IOException {
        if (tiffPointer != null) 
            throw new IOException("Close the open file first");
        
        tiffPointer = LibTIFF.INSTANCE.TIFFOpen(f.getPath(), mode);
        if (tiffPointer.isNull()) {
            tiffPointer = null;
            throw LibTIFFErrorHandler.createIOException("Could not open TIFF file " + f);
        }
        page = 1;
    }
    
    /**
     * Closes the TIFF file
     */
    public void close() {
        if (tiffPointer != null) {
            LibTIFF.INSTANCE.TIFFClose(tiffPointer);
            tiffPointer = null;
            page = -1;
        }
    }
    
    /**
     * Returns the currently read page
     * @return
     */
    public int getPage() {
        return page;
    }
    
    /**
     * Read the next page
     * @return true if there is a next page, false if this was the last page
     */
    public boolean nextPage() {
        if (LibTIFF.INSTANCE.TIFFReadDirectory(tiffPointer)!=0) {
            page++;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns an integer value of a TIFF tag
     * @param tag
     * @return
     * @throws IOException 
     */
    public int getIntField(int tag) throws IOException {
        IntByReference res = new IntByReference();
        int rv = LibTIFF.INSTANCE.TIFFGetField(tiffPointer, tag, res);
        if (rv == 0) {
            throw new IOException("Tag " + tag + " not found.");
        }
        return res.getValue();
    }
    
    /**
     * Returns the height of the current page in pixels
     * @return
     * @throws IOException
     */
    public int getHeight() throws IOException {
        return getIntField(TIFFTAG_IMAGELENGTH);
    }

    /**
     * Returns the width of the current page in pixels
     * @return
     * @throws IOException
     */
    public int getWidth() throws IOException {
        return getIntField(TIFFTAG_IMAGEWIDTH);
    }
    
    /**
     * Returns an integer value of a TIFF tag
     * @param tag
     * @return
     * @throws IOException 
     */
    public short getShortField(int tag) throws IOException {
        ShortByReference res = new ShortByReference();
        int rv = LibTIFF.INSTANCE.TIFFGetField(tiffPointer, tag, res);
        if (rv == 0) {
            throw new IOException("Tag " + tag + " not found.");
        }
        return res.getValue();
    }
    
    /**
     * Returns an float value of a TIFF tag
     * @param tag
     * @return
     * @throws IOException 
     */
    public float getFloatField(int tag) throws IOException {
        FloatByReference res = new FloatByReference();
        int rv = LibTIFF.INSTANCE.TIFFGetField(tiffPointer, tag, res);
        if (rv == 0) {
            throw new IOException("Tag " + tag + " not found.");
        }
        return res.getValue();
    }
   
    /**
     * Returns the horizontal resolution in dpi
     * @return
     * @throws IOException 
     */
    public float getResolutionX() throws IOException {
        return getFloatField(TIFFTAG_XRESOLUTION);
    }
   
    /**
     * Returns the vertical resolution in dpi
     * @return
     * @throws IOException 
     */
    public float getResolutionY() throws IOException {
        return getFloatField(TIFFTAG_YRESOLUTION);
    }
    
    /**
     * Returns an String value of a TIFF tag
     * @param tag
     * @return
     * @throws IOException 
     */
    public String getStringField(int tag) throws IOException {
        PointerByReference res = new PointerByReference();
        int rv = LibTIFF.INSTANCE.TIFFGetField(tiffPointer, tag, res);
        if (rv == 0) {
            throw new IOException("Tag " + tag + " not found.");
        }
        
        return res.getValue().getString(0);
    }
    
    /**
     * Reads a 1 bpp image
     * @return
     * @throws IOException
     */
    public BufferedImage readBWImage() throws IOException {
        // Check that it is of a type that we support
        short bps = getShortField(TIFFTAG_BITSPERSAMPLE);
        if (bps != 1)
            throw new IOException("Not a black&white image (Either undefined or unsupported number of bits per sample: " + bps + ")");
        short spp = getShortField(TIFFTAG_SAMPLESPERPIXEL);
        if (spp != 1)
            throw new IOException("Not a black&white image (Either undefined or unsupported number of samples per pixel: " + spp + ")");
        
        // Read in the possibly multiple strips
        int stripSize = LibTIFF.INSTANCE.TIFFStripSize (tiffPointer);
        int stripMax = LibTIFF.INSTANCE.TIFFNumberOfStrips (tiffPointer);
        int imageOffset = 0;
        int bufferSize = stripMax * stripSize;
        log.fine("Page="+page+";stripSize="+stripSize+";stripMax="+stripMax);
        
        byte[] buffer = new byte[bufferSize];
        ByteBuffer bBuf = ByteBuffer.wrap(buffer);

        for (int stripCount = 0; stripCount < stripMax; stripCount++){
            bBuf.position(imageOffset);
            int result = LibTIFF.INSTANCE.TIFFReadEncodedStrip(tiffPointer, stripCount, bBuf, stripSize);
            if (result == -1) {
                throw LibTIFFErrorHandler.createIOException("Read error on input strip number " + stripCount);
            }
            imageOffset += result;
        }

        short photo = getShortField(TIFFTAG_PHOTOMETRIC);
        if(photo != PHOTOMETRIC_MINISWHITE){
            // Flip bits
            log.info("Fixing the photometric interpretation (photo=" + photo + ")");
            for(int count = 0; count < bufferSize; count++)
                buffer[count] = (byte)~buffer[count];
        }
        
        // ReadEncodedStrip already takes care of this...
//        short fillorder = getShortField(TIFFTAG_FILLORDER);
//        if(fillorder != FILLORDER_MSB2LSB){
//            // We need to swap bits -- ABCDEFGH becomes HGFEDCBA
//            System.out.println("Fixing the fillorder");
//            for(int count = 0; count < bufferSize; count++){
//                int tempbyte = 0;
//                byte b = buffer[count];
//                if ((b & 128) != 0) tempbyte += 1;
//                if ((b & 64)  != 0) tempbyte += 2;
//                if ((b & 32)  != 0) tempbyte += 4;
//                if ((b & 16)  != 0) tempbyte += 8;
//                if ((b & 8)   != 0) tempbyte += 16;
//                if ((b & 4)  != 0) tempbyte += 32;
//                if ((b & 2)   != 0) tempbyte += 64;
//                if ((b & 1)   != 0) tempbyte += 128;
//                buffer[count] = (byte)tempbyte;
//            }
//        }
        
        final int width = getWidth();
        final int height = getHeight();
        log.fine("Page=" + page + ";width=" + width + ";height=" + height);
        DataBuffer imgBuf = new DataBufferByte(buffer, bufferSize);
        WritableRaster wr = Raster.createPackedRaster(imgBuf, width, height, 1, null);
        return new BufferedImage(ColorModel_BlackAndWhite, wr, false, null);
    }
    
    /**
     * Reads the current page as a AGBR color image
     * @return
     * @throws IOException 
     */
    public BufferedImage readAGBRImage() throws IOException {
        final int width = getWidth();
        final int height = getHeight();
        log.fine("Page=" + page + ";width=" + width + ";height=" + height);

        int[] imageData = new int[width * height];
        int rv = LibTIFF.INSTANCE.TIFFReadRGBAImageOriented(tiffPointer, width, height, imageData, ORIENTATION_TOPLEFT, 0);
        if (rv == 0)
            throw LibTIFFErrorHandler.createIOException("Could not successfully read the RGBA image");

        DataBuffer imgBuf = new DataBufferInt(imageData, imageData.length);
        WritableRaster wr = Raster.createPackedRaster(imgBuf, width, height, width, ABGR_bandmasks, null);
        return new BufferedImage(ColorModel_AGBR, wr, false, null);
    }
    
    /**
     * Reads the current page as a Java image.
     * Uses readBWImage if 1 bit per pixel, else readAGBRImage
     * @return
     * @throws IOException
     */
    public BufferedImage readImage() throws IOException {
        short bps = getShortField(TIFFTAG_BITSPERSAMPLE);
        short spp = getShortField(TIFFTAG_SAMPLESPERPIXEL);
        log.fine("Page=" + page + ";BitsPerSample=" + bps + ";SamplesPerPixel=" + spp);
        
        if (bps==1 && spp==1) {
            log.fine("Reading image as Black&White");
            return readBWImage();
        } else { 
            log.fine("Reading image as AGBR");
            return readAGBRImage();
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    
    
    /**
     * Test code for this class
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        File f;
        if (args.length == 0)
         f = new File("/home/jonas/java/yajhfc/tiffbug/test.tif"); 
        else
         f = new File(args[0]);

        LibTIFFFile tif = new LibTIFFFile(f);
        do {
            final String page = "Page " + tif.getPage() + " (" + tif.getWidth() + "x" + tif.getHeight() + "; resX=" + tif.getResolutionX() + "; resY=" + tif.getResolutionY() +  ")";
            final BufferedImage bi = tif.readImage();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new JFrame(page);
                    JPanel contentPane = new JPanel(new BorderLayout());
                    JLabel image = new JLabel(new ImageIcon(bi));
                    contentPane.add(new JScrollPane(image), BorderLayout.CENTER);
                    frame.setContentPane(contentPane);

                    frame.setSize(1000, 800);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                } 
            });
        } while (tif.nextPage());
        tif.close();
        
//        int[] bandmasks = new int[] { 0x000000ff , 0x0000ff00, 0x00ff0000, 0xff000000};
//        ColorModel ABGR = new DirectColorModel(32,
//                0x000000ff,   // Red
//                0x0000ff00,   // Green
//                0x00ff0000,   // Blue
//                0xff000000    // Alpha
//                );
//        
//        TIFFPointer t = LibTIFF.INSTANCE.TIFFOpen(f.getAbsolutePath(), "r");
//        if (!t.isNull()) {
//            int dircount = 0;
//            IntByReference pWidth = new IntByReference();
//            IntByReference pHeight = new IntByReference();
//            do {
//                LibTIFF.INSTANCE.TIFFGetField(t, TIFFConstants.TIFFTAG_IMAGELENGTH, pHeight);
//                LibTIFF.INSTANCE.TIFFGetField(t, TIFFConstants.TIFFTAG_IMAGEWIDTH, pWidth);
//                final int width = pWidth.getValue();
//                final int height = pHeight.getValue();
//                System.out.println("Page " + (dircount+1) + "; width=" + width + "; height=" + height);
//                
//                int[] imageData = new int[width * height];
//                LibTIFF.INSTANCE.TIFFReadRGBAImageOriented(t, width, height, imageData, LibTIFF.ORIENTATION_TOPLEFT, 0);
//                
//                DataBuffer imgBuf = new DataBufferInt(imageData, imageData.length);
//                WritableRaster wr = Raster.createPackedRaster(imgBuf, width, height, width, bandmasks, null);
//                final BufferedImage bi = new BufferedImage(ABGR, wr, false, null);
//                
//                final String page = "Page " + (dircount+1) + "; width=" + width + "; height=" + height;
//                SwingUtilities.invokeLater(new Runnable() {
//                   @Override
//                    public void run() {
//                       JFrame frame = new JFrame(page);
//                       JPanel contentPane = new JPanel(new BorderLayout());
//                       JLabel image = new JLabel(new ImageIcon(bi));
//                       contentPane.add(new JScrollPane(image), BorderLayout.CENTER);
//                       frame.setContentPane(contentPane);
//                       
//                       frame.setSize(1000, 800);
//                       frame.setVisible(true);
//                       frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                    } 
//                });
//                
//                
//                dircount++;
//            } while (LibTIFF.INSTANCE.TIFFReadDirectory(t)!=0);
//            System.out.println(dircount);
//            LibTIFF.INSTANCE.TIFFClose(t);
//        }
    }
}
