package image;

import java.awt.*;
/**
 *  Manages the processing of an image by padding it and dividing it into sub-images and performing
 * operations on them, according to the original image and given resolution.
 * @author adan.ir1, hayanat2002
 * @see Image
 * @see SubImage
 * @see ImagePadding
 */
public class ImageManager {
    private final Image paddedImage;
    private final int resolution;
    private final SubImage[][] subImages;
    private final int subImageSize;
    private final int numRows;

    /**
     * Constructs an ImageManager object with the original image and resolution for sub-images.
     * @param originalImage The original image to be processed.
     * @param resolution The resolution for creating sub-images.
     */
    public ImageManager(Image originalImage,int resolution){
        ImagePadding imagePadding = new ImagePadding(originalImage);
        this.paddedImage = imagePadding.getPaddedImage() ;
        this.resolution = resolution;
        this.subImageSize = paddedImage.getWidth() / resolution;
        this.numRows = paddedImage.getHeight() / subImageSize;
        this.subImages = new SubImage[numRows][resolution];
        buildSubImages();
    }
    /**
     * Gets the array of sub-images.
     * @return The array of sub-images.
     */
    public SubImage[][] getSubImages(){
        return this.subImages;
    }
    /**
     * Gets the width of the padded image.
     * @return The width of the padded image.
     */
    public int getWidth(){
        return paddedImage.getWidth();
    }
    /**
     * Gets the height of the padded image.
     * @return The height of the padded image.
     */
    public int getHeight(){
        return paddedImage.getHeight();
    }

    private void buildSubImages(){
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < resolution; j++) {
                Color[][] pixelArray = makeArraySubImg(i * subImageSize, j * subImageSize);
                subImages[i][j] = new SubImage( pixelArray, subImageSize );
            }
        }
    }

    private Color[][] makeArraySubImg(int startX, int startY) {
        Color[][] pixelArray = new Color[subImageSize][subImageSize];
        for (int i = 0; i < subImageSize; i++) {
            for (int j = 0 ; j < subImageSize; j++) {
                pixelArray[i][j] = paddedImage.getPixel(startX + i , startY + j);
            }
        }
        return pixelArray;
    }
}