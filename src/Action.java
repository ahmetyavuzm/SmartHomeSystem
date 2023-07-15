import java.util.ArrayList;

/**
 * This {@code Action} class keeps actions that users want to do.
 */
public class Action {
    /**
     * The action name as a {@code String}
     */
    String actionName;
    /**
     * Parameters list of the action.
     */
    ArrayList<String> values;

    /**
     * Users command as a raw String.
     */
    String commandText;

    /**
     * Creates an {@code Action} with action name , action values and command text.
     * @param actionName: Action's name.
     * @param values: An {@code ArrayList<String>} contains the parameters of {@code Action}.
     * @param commandText: Raw command text.
     */
    public Action(String actionName, ArrayList<String> values, String commandText){
        this.actionName = actionName;
        this.values = values;
        this.commandText = commandText;
    }
}
