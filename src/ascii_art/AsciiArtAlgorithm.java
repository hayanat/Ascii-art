package ascii_art;

import image.ImageManager;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;
/**
 * Represents an algorithm for generating ASCII art from an image.
 * @author adan.ir1, hayanat2002
 * @see ImageManager
 * @see SubImgCharMatcher
 */
public class AsciiArtAlgorithm {
    private final int resolution;
    private final ImageManager imageManager;
    private final SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructs an AsciiArtAlgorithm object with the given ImageManager, SubImgCharMatcher, and resolution.
     * @param imageManager The ImageManager object to manage the original image and its sub-images.
     * @param subImgCharMatcher The SubImgCharMatcher object to match sub-image brightness with
     * ASCII characters.
     * @param resolution The resolution of the ASCII art.
     */
    public AsciiArtAlgorithm(ImageManager imageManager,SubImgCharMatcher subImgCharMatcher, int resolution) {
        this.imageManager = imageManager;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
    }
    /**
     * Runs the ASCII art algorithm on the provided image and returns the generated ASCII art.
     * @return The generated ASCII art as a 2D character array.
     */
    public char [][] run(){
        SubImage[][] subImagesArr = imageManager.getSubImages();
        int numRows = subImagesArr.length;

        char [][] result = new char[numRows][this.resolution];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < resolution; j++) {
                SubImage subImage = subImagesArr[i][j];
                double grayValue = subImage.getGrayValue();
                result[i][j] = subImgCharMatcher.getCharByImageBrightness(grayValue);
            }
        }
        return result;
    }

}