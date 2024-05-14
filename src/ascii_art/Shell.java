package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImageManager;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Represents a shell interface for generating ASCII art from images.
 * Commands include setting characters, adjusting resolution, changing input images, selecting output methods,
 * and generating ASCII art.
 * @author adan.ir1, hayanat2002
 */
public class Shell {
    /**
     * The Default image file name.
     */
    public static final String DEFAULT_IMAGE = "cat.jpeg";
    /**
     * The Default characters to do ascii art with.
     */
    public static final char[] DEFAULT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /**
     * The Default resolution to do ascii art with.
     */
    public static final int DEFAULT_RESOLUTION = 128;

    // commands.
    private static final String EXIT = "exit";
    private static final String CHARS = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String IMAGE = "image";
    private static final String OUTPUT = "output";
    private static final String ASCII_ART = "asciiArt";

    // messages to print.
    private static final String RESOLUTION_INCORRECT_FORMAT = "Did not change resolution" +
            " due to incorrect format.";
    private static final String RESOLUTION_OUT_BOUNDARIES = "Did not change resolution due to exceeding" +
            " boundaries.";
    private static final String RESOLUTION_SET = "Resolution set to ";
    private static final String EMPTY_CHARSET = "Did not execute. Charset is empty.";
    private static final String IMAGE_ERROR = "Did not execute due to problem with image file.";
    private static final String ADD_ERROR = "Did not add due to incorrect format.";
    private static final String REMOVE_ERROR = "Did not remove due to incorrect format.";
    private static final String OUTPUT_INCORRECT_FORMAT = "Did not change output method" +
            " due to incorrect format.";
    private static final String INCORRECT_COMMAND = "Did not execute due to incorrect command.";

    // for codding style.
    private static final String CONSOLE = "console";
    private static final String HTML = "html";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String FONT = "Courier New";
    private static final String OUTPUT_FILE_NAME = "out.html";
    private static final String CONSOLE_ARROWS = ">>> ";
    private static final String REGEX = " ";
    private static final int ONE_LETTER = 1;
    private static final int START_CHAR_NO_SPACE = 33;
    private static final int END_CHAR = 126;
    private static final String ALL = "all";
    private static final String SPACE = "space";
    private static final int RANGE = 3;
    private static final char SPLITTER_RANGE = '-';
    private static final char SPACE_CHAR = ' ';
    private static final int FIRST_CHAR_IN_RANGE = 0;
    private static final int SECOND_CHAR_IN_RANGE = 2;
    private static final int START_CHAR_IN_RANGE = 0;
    private static final int END_CHAR_IN_RANGE = 1;
    private static final int LETTER = 0;
    private static final int SPLITTER = 1;
    private static final int COMMAND_WITH_TWO_ARGS = 2;
    private static final int ARG = 1;
    private static final int COMMAND = 0;

    private int minCharsInRow;
    private int maxCharsInRow;
    private int currResolution;
    private AsciiOutput asciiOutput;
    private final SubImgCharMatcher subImgCharMatcher;
    private ImageManager imageManager;
    private Image originalImage;

    /**
     * Constructs a Shell object with default settings.
     */
    public Shell(){
        this.subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARS);
        this.currResolution = DEFAULT_RESOLUTION;
        try {
            this.originalImage = new Image(DEFAULT_IMAGE);
            this.imageManager = new ImageManager(originalImage, currResolution);
            this.maxCharsInRow = imageManager.getWidth();
            this.minCharsInRow = Math.max(1, imageManager.getWidth()/ imageManager.getHeight());
            this.asciiOutput = new ConsoleAsciiOutput();
        }
        catch (IOException ignored){} // this is okay >> read forum
    }

    /**
     * The main method that starts the shell.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }

    /**
     * Runs the shell interface, according to the users input.
     */
    public void run(){
        System.out.print(CONSOLE_ARROWS);
        String line = KeyboardInput.readLine();
        while (!(line.equals(EXIT))){
            String[] commandAndArgs = line.split(REGEX);
            String secondArg = " ";
            if(commandAndArgs.length == COMMAND_WITH_TWO_ARGS) {
                secondArg = commandAndArgs[ARG];
            }
            try {
                processCommand(commandAndArgs[COMMAND], secondArg);
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
            System.out.print(CONSOLE_ARROWS);
            line = KeyboardInput.readLine();
        }
    }

    private void processCommand(String command,String commandArgs) throws IOException{
        switch (command){
            case CHARS:
                dealWithChars(); // doesn't throw exceptions
                break;
            case ADD:
                removeAddCmd(commandArgs,true); // throw  IO exceptions
                break;
            case REMOVE:
                removeAddCmd(commandArgs,false); // throw  IO exceptions
                break;
            case RES:
                dealWithResolution(commandArgs);  // throw  IO exceptions
                break;
            case IMAGE:
                dealWithImage(commandArgs); // throw  IO exceptions
                break;
            case OUTPUT:
                dealWithOutput(commandArgs); // throw  IO exceptions
                break;
            case ASCII_ART:
                dealWithAsciiArt(); // throw  IO exceptions
                break;
            default:
                throw new IOException(INCORRECT_COMMAND);
        }
    }

    private void dealWithChars() {
        List<Character> chars =  subImgCharMatcher.getChars();
        Collections.sort(chars);
        for (char c : chars){
            System.out.print(c + " ");
        }
        System.out.println();
    }


    private void dealWithAsciiArt() throws IOException{
        if(subImgCharMatcher.getChars().isEmpty()){
            throw new IOException(EMPTY_CHARSET);
        }
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(imageManager,
                subImgCharMatcher,currResolution);
        char[][] result = asciiArtAlgorithm.run();
        asciiOutput.out(result);
    }

    private void dealWithOutput(String commandArgs) throws IOException {
        if (!commandArgs.equals(HTML) && !commandArgs.equals(CONSOLE)) {
            throw new IOException(OUTPUT_INCORRECT_FORMAT);
        }
        if(commandArgs.equals(HTML)){
            asciiOutput = new HtmlAsciiOutput(OUTPUT_FILE_NAME,FONT);
        }else{
            asciiOutput = new ConsoleAsciiOutput();
        }
    }

    private void dealWithImage(String commandArgs) throws IOException {
        try {
            Image image = new Image(commandArgs);
            this.originalImage = image;
            this.imageManager = new ImageManager(originalImage, currResolution);
            this.maxCharsInRow = imageManager.getWidth();
            this.minCharsInRow = Math.max(1, imageManager.getWidth()/ imageManager.getHeight());
        }
        catch (IOException e) {
            throw new IOException(IMAGE_ERROR);
        }
    }

    private void dealWithResolution(String commandArgs) throws IOException {
        if (!commandArgs.equals(UP) && !commandArgs.equals(DOWN)) {
            throw new IOException(RESOLUTION_INCORRECT_FORMAT);
        }
        //now:  commandArgs.equals("up") or commandArgs.equals("down")
        if (updateResolution(commandArgs)) {
            this.imageManager = new ImageManager(originalImage, currResolution);
            System.out.println(RESOLUTION_SET + currResolution + ".");
        } else {
            throw new IOException(RESOLUTION_OUT_BOUNDARIES);
        }
    }

    private boolean updateResolution(String commandArgs){
        boolean hasChanged = false;
        if (commandArgs.equals(UP)) {
            if ((2 * this.currResolution) <= maxCharsInRow) { // can up
                currResolution *= 2;
                hasChanged = true;
            }

        }else { // is down
            if ((this.currResolution * 0.5) >= minCharsInRow) { // can down
                currResolution /= 2;
                hasChanged = true;
            }
        }
        return hasChanged;
    }
    private void removeAddCmd(String commandArgs,boolean isAdd) throws IOException{
        if(commandArgs.length() == ONE_LETTER){
            removeAddCmdOneCharNonSpace(commandArgs.charAt(LETTER),isAdd);
        }
        else if(commandArgs.equals(ALL)){
            removeAddSpace(isAdd);
            removeAddInRange((char) START_CHAR_NO_SPACE,(char) END_CHAR,isAdd);
        }
        else if (commandArgs.equals(SPACE)){
            removeAddSpace(isAdd);
        }
        else if (commandArgs.length() == RANGE && commandArgs.charAt(SPLITTER) == SPLITTER_RANGE) {
            char startChar = getCharsResult(commandArgs)[START_CHAR_IN_RANGE];
            char endChar = getCharsResult(commandArgs)[END_CHAR_IN_RANGE];
            removeAddInRange(startChar,endChar,isAdd);
        }
        else {
            AddRemoveThrowErr(isAdd);
        }
    }

    private void AddRemoveThrowErr(boolean isAdd) throws IOException{
        if (isAdd){
            throw new IOException(ADD_ERROR);
        }
        else {
            throw new IOException(REMOVE_ERROR);
        }
    }

    private char[] getCharsResult(String commandArgs){
        char startChar = commandArgs.charAt(FIRST_CHAR_IN_RANGE);
        char endChar = commandArgs.charAt(SECOND_CHAR_IN_RANGE);
        char temp = startChar;
        startChar = (char) Math.min(startChar,endChar);
        endChar = (char) Math.max(temp,endChar);
        return new char[]{startChar,endChar};
    }
    private void removeAddInRange(char start,char end,boolean isAdd) throws IOException{
        for (char c = start; c<= end;c++){
            removeAddCmdOneCharNonSpace(c,isAdd);
        }
    }

    private void removeAddSpace(boolean isAdd) {
        if (isAdd){
            subImgCharMatcher.addChar(SPACE_CHAR);
        }
        else {
            subImgCharMatcher.removeChar(SPACE_CHAR);
        }
    }

    private void removeAddCmdOneCharNonSpace(char c,boolean isAdd) throws IOException{
        if(isValidChar(c)){
            if (isAdd){
                subImgCharMatcher.addChar(c);
            }
            else {
                subImgCharMatcher.removeChar(c);
            }
        }
        else {
            AddRemoveThrowErr(isAdd);
        }
    }

    private boolean isValidChar(char c) {
        return c >= START_CHAR_NO_SPACE && c <= END_CHAR;
    }

}