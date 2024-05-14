package image;

import java.awt.*;
/**
 *  Pads an image with white pixels to make its dimensions a power of two.
 * @author adan.ir1, hayanat2002
 */
public class ImagePadding {
    private static final int WHITE = 255;
    private final Image originalImage;
    private final Image paddedImage;

    /**
     * Constructs an ImagePadding object with the given original image.
     * @param originalImage The original image to be padded.
     */
    public ImagePadding(Image originalImage) {
        this.originalImage = originalImage;
        this.paddedImage = makePaddedImage();
    }
    /**
     * Gets the padded image.
     * @return The padded image.
     */
    public Image getPaddedImage(){
        return this.paddedImage;
    }
    private Image makePaddedImage(){
        int originalHeight = originalImage.getHeight();
        int originalWidth = originalImage.getWidth();
        int height = closestPowerOfTwo(originalHeight);
        int width = closestPowerOfTwo(originalWidth);
        Color[][] pixelArray = new Color[height][width];
        int widthPadding = (width - originalWidth)/2;
        int heightPadding = (height - originalHeight)/2;
        for (int row = 0; row<height;row++){
            for (int col = 0; col < width;col++){
                if(col < widthPadding|| col >= widthPadding + originalWidth ||
                        row < heightPadding || row >= heightPadding + originalHeight){
                    pixelArray[row][col] = new Color(WHITE,WHITE,WHITE);
                }
                else {
                    pixelArray[row][col] = originalImage.getPixel(row -heightPadding,col-widthPadding);
                }
            }
        }
        return new Image(pixelArray,width,height);
    }

    private static int closestPowerOfTwo(int n){
        int closestNumber = 1;
        while (closestNumber < n){
            closestNumber = closestNumber * 2;
        }
        return closestNumber;
    }
}
