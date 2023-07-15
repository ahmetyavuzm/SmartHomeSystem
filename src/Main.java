import java.util.ArrayList;

/**
 * The {@code Main} class for executing the whole program.
 * @author Ahmet Yavuz Mutlu / 2210356014
 * @version 2.4
 */
public class Main {
    /**
     * Takes the inputs and execute the program then writes the outputs.
     * @param args console arguments.
     */
    public static void main(String[] args){
        InputIO inputFile = new InputIO(args[0]);
        OutputIO outputFile = new OutputIO(args[1]);
        ArrayList<Action> actions = inputFile.getLinesAsActions();
        try{
            Action lastAction = null;
            for(Action action : actions){
                Object response = SmartDeviceController.actionHandler(action);
                if (!Validations.isNull(response)){
                    outputFile.write((String) response);
                }
                lastAction = action;
            }
            if(Validations.isNull(lastAction)){
                throw new DeviceExceptions.InvalidCommandException("");
            }
            if (!"ZReport".equals(lastAction.actionName)){
                String report = "ZReport:\n";
                report += SmartDeviceController.zReport(new ArrayList<>());
                outputFile.write(report);
            }
        }catch (DeviceExceptions e){
            outputFile.write(e.getMessage());
        }
    }
}