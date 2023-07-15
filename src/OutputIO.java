import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@code OutputIO} class includes methods for outputs.
 */
public class OutputIO {
    /**
     * Name of the output file.
     */
    public String filename;

    /**
     * Creates a OutputIO object and output file.
     * @param filename Name of the output file. ({@code String})
     * @see OutputIO#createOutFile(String)
     */
    public OutputIO(String filename){
        this.filename = filename;
        createOutFile(this.filename);
    }

    /**
     * Creates an output file with given name.
     * @param filename Name of the output file. ({@code String})
     */
    private static void createOutFile(String filename){
        FileWriter file;
        try {
            file = new FileWriter(filename);
            file.close();
        } catch (IOException e) {
        }
    }

    /**
     * Appends a text to the output file with a newline character last of the text.
     * @param obj An Object.
     * @param <T> Generic type operator.
     * @see OutputIO#write(Object)
     */
    public <T> void writeln(T obj){
        write(obj +"\n");
    }

    /**
     * Appends a text to the output file.
     * @param obj An Object.
     * @param <T> Generic type operator.
     */
    public <T> void write(T obj){
        try {
            FileWriter outFile = new FileWriter(this.filename, true);
            outFile.write(String.valueOf(obj));
            outFile.close();
        } catch (IOException e) {
        }
    }
}
