import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The {@code InputIO} class includes methods for handle the all inputs from a text file.
 */
public class InputIO {
    /**
     * It keeps the filepath of the input file.
     */
    private final String filePath;
    /**
     * Gets the input file's {@link InputIO#filePath}.
     * @return A {@code String} represents the input file's filepath.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     *
     * @param filePath The input file's filepath.
     */
    public InputIO(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets lines of the input file.
     * @return <p>
     *      An {@code ArrayList<String>} that contains the lines in the input file.
     * Each line is an element of the list.
     * </p>
     */
    public ArrayList<String> getLines() {
        try {
            return new ArrayList<String>(Files.readAllLines(Paths.get(this.filePath)));

        } catch (IOException e) {
            System.exit(1);
            return null;
        }
    }

    /**
     * Gets lines of the input files as Actions
     * @return <p>
     *     An {@code ArrayList<Action>} that contains the actions.
     *     Each Action comes from a line form input file.
     * </p>
     * @see Action
     * @see InputIO#getLines()
     * @see InputIO#convertToAction(String)
     */
    public ArrayList<Action> getLinesAsActions(){
        ArrayList<String> lines = getLines();
        ArrayList<Action> actions = new ArrayList<>();
        for (String line: lines){
            actions.add(convertToAction(line));
        }
        return actions;
    }

    /**
     *Converts a string to an {@code Action} object.
     * @param line A string.
     * @return An Action object.
     * @see InputIO#splitLine(String)
     */
    private static Action convertToAction(String line){
        ArrayList<String> splittedLine = splitLine(line);
        String action = splittedLine.get(0);
        ArrayList<String> values = new ArrayList<>(splittedLine.subList(1,splittedLine.size()));
        return new Action(action,values,line);
    }

    /**
     * Splits the line form {@code \t} character.
     * @param line A {@code String} object.
     * @return An {@code ArrayList<String>} that contains substrings in the line.
     */
    private static ArrayList<String> splitLine(String line){
        return new ArrayList<>(Arrays.asList(line.split("\t")));
    }
}
