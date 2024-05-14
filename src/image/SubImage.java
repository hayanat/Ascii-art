package image;

import java.awt.*;
/**
 *  Represents a sub-image a subclass of the provided Image class.
 * Provides additional functionality in order to calculate gray value for each sub-image.
 * @author adan.ir1, hayanat2002
 * @see Image
 */
public class SubImage extends Image{
    private static final double RED_COEFFICIENT = 0.2126;
    private static final double GREEN_COEFFICIENT = 0.7152;
    private static final double BLUE_COEFFICIENT = 0.0722;
    private static final int RGB_MAX = 255;
    private final int size;
    /**
     * Constructs a SubImage with the given pixel array and size, such that the sub-image is square.
     * @param pixelArray The 2D array representing the pixels of the sub-image.
     * @param size The size of the sub-image.
     */
    public SubImage(Color[][] pixelArray, int size) {
        super(pixelArray, size, size);
        this.size = size;
    }
    /**
     * Calculates the average grayscale value for the sub-image.
     * @return The average grayscale value for the sub-image.
     */
    public double getGrayValue(){
        double brightness = 0;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                brightness += calculateGrayPixel(getPixel(i,j));
            }
        }
        return brightness/ (RGB_MAX * size * size);
    }
    private double calculateGrayPixel(Color color){
        return ((color.getRed() * RED_COEFFICIENT) + (color.getGreen() * GREEN_COEFFICIENT) +
                (color.getBlue() * BLUE_COEFFICIENT));
    }

}