package image_char_matching;

import java.util.*;
/**
 * Matches characters with their corresponding image brightness values.
 * @author adan.ir1, hayanat2002
 */
public class SubImgCharMatcher {
    private static final int LENGTH_OF_BOOL_ARRAY = 16;
    private static final char ASCII_BIGGEST_NUMBER = 127;

    // hash map or tree map ? -> hashmap for better runtime
    private final HashMap<Character, Double> brightnessMap = new HashMap<>();
    private double maxBrightness = 0;
    private double minBrightness = 255;

    /**
     * Constructs a SubImgCharMatcher object with a given charset.
     * @param charset The array of characters for which brightness needs to be calculated.
     */
    public SubImgCharMatcher(char[] charset) {
        for(char c: charset){ // O(n)
            double charInitialBrightness = calculateInitialBrightness(c);  // O(1)
            minBrightness = Math.min(charInitialBrightness, minBrightness);
            maxBrightness = Math.max(charInitialBrightness, maxBrightness);
            brightnessMap.put(c,charInitialBrightness);
        }
    }
    /**
     * Gets the character that matches the closest brightness value to the specified brightness.
     * @param brightness The brightness value in order to find the closest character to match.
     * @return The character that matches the closest brightness value.
     */
    public char getCharByImageBrightness(double brightness){ // O(n) in average
        char nearestChar = ASCII_BIGGEST_NUMBER; // the biggest ascii number
        double minDifference = 2 ; // number bigger than 1

        for (char c : this.brightnessMap.keySet()) {
            double normalizedBrightness = normalizeCharBrightness(c);
            if(((Math.abs(normalizedBrightness - brightness) == minDifference) && (c < nearestChar))
                    ||  (Math.abs(normalizedBrightness - brightness) < minDifference) ){
                nearestChar = c;
                minDifference = Math.abs(normalizedBrightness - brightness);
            }
        }
        return nearestChar;
    }
    /**
     * Adds a new character.
     * @param c The character to add.
     */
    public void addChar(char c){ // O(1) in average
        if(brightnessMap.containsKey(c)){
            return;
        }
        double charInitialBrightness = calculateInitialBrightness(c);  // O(1)
        brightnessMap.put(c, charInitialBrightness);
        this.maxBrightness = Math.max(maxBrightness, charInitialBrightness);
        this.minBrightness = Math.min(minBrightness, charInitialBrightness);
    }
    /**
     * Removes a given character.
     * @param c The character to remove.
     */
    public void removeChar(char c){
        if(!brightnessMap.containsKey(c)){
            return;
        }

        double brightnessChar = brightnessMap.get(c);
        brightnessMap.remove(c);

        if(brightnessChar <= minBrightness || brightnessChar >= maxBrightness) {
            // reset the min and max
            minBrightness = LENGTH_OF_BOOL_ARRAY* LENGTH_OF_BOOL_ARRAY;
            maxBrightness = 0;

            for(char d: brightnessMap.keySet()){
                double charBrightness = brightnessMap.get(d);
                minBrightness = Math.min(charBrightness, minBrightness);
                maxBrightness = Math.max(charBrightness, maxBrightness);
            }
        }
    }

    /**
     * Gets a sorted list of the characters we hold according to their ascii value.
     * @return A sorted list of characters.
     */
    public List<Character> getChars(){  // O(n)
        return new ArrayList<>(brightnessMap.keySet());
    }

    private static double calculateInitialBrightness(char c) { // O(1)
        boolean[][] booleanArr = CharConverter.convertToBoolArray(c);
        double trueCounter = 0; // must be double
        for (int i = 0; i < LENGTH_OF_BOOL_ARRAY; i++) {
            for (int j = 0; j < LENGTH_OF_BOOL_ARRAY; j++) {
                if(booleanArr[i][j])
                    trueCounter ++;
            }
        }
        return trueCounter / Math.pow(LENGTH_OF_BOOL_ARRAY, 2);
    }

    private double normalizeCharBrightness(char c){
        return  ((brightnessMap.get(c) - minBrightness)/ (maxBrightness - minBrightness));
    }
}