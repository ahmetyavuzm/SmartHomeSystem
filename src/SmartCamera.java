import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 *     The {@code SmartCamera} class is a subclass of {@code SmartDevice} class.
 *     It's contains necessary methods and properties for a smart camera.
 * </p>
 * @see SmartDevice
 */
public class SmartCamera extends SmartDevice{
    /**
     * Represents the filled storage of the smart camera.
     */
    public float filledStorage = (float) 0;
    /**
     * Represents how much storage filled per minute as megabyte during video recording.
     * <br>Its must be a positive float number.
     */
    public float megabyte;

    /**
     * Sets the {@link SmartCamera#megabyte} property of the {@code SmartCamera}.
     * @param megabyte Filled storage per minute as megabyte during the video recording. ({@code float})
     */
    public void setMegabyte(float megabyte) {
        this.megabyte = megabyte;
    }

    /**
     * Sets the {@link SmartCamera#filledStorage} property of the {@code SmartCamera}.
     * @param filledStorage Total filled storage of the smart camera. ({@code float})
     */
    public void setFilledStorage(float filledStorage) {
        this.filledStorage = filledStorage;
    }

    /**
     * Creates a {@code SmartCamera}.
     * @param name Name of the smart camera. ({@code String})
     * @param megabyte Filled storage per minute as megabyte during the video recording. ({@code float})
     * @param status On/Off status of the smart camera. ({@code boolean})
     */
    public SmartCamera(String name ,float megabyte ,boolean status) {
        super(name,SmartCamera.class);
        setMegabyte(megabyte);
        setStatus(status);
    }

    /**
     * Gets {@code SmartCamera} information.
     * @return A {@code String} that contains information about {@code SmartCamera}.
     */
    public String getDeviceInfo(){
        return "Smart Camera "+ this.name+ " is " + getStatusText() + " and used " + String.format("%.2f",this.filledStorage) +" MB "+
                "of storage so far (excluding current status), and its time to switch its status is " +getSwitchTimeText()+".";
    }

    /**
     * Turns on the {@code SmartCamera}.
     * @param startTime A {@code Date} object that represents the time of turned on.
     * @see Date
     */
    @Override
    public void on(Date startTime){
        setStatus(true);
        setStartTime(startTime);
    }

    /**
     * Turns off the {@code SmartCamera} and updates filled storage.
     * @param stopTime A {@code Date} represents the time of turned off.
     */
    @Override
    public void off(Date stopTime){
        setStatus(false);
        setStopTime(stopTime);
        updateStorage();

        setStopTime(null);
        setStartTime(null);
    }

    /**
     * Updates the filled storage of {@code SmartCamera}
     */
    private void updateStorage(){
        long diff = this.stopTime.getTime()-this.startTime.getTime();
        float minute = Utilities.millisecondToMinute(diff);
        setFilledStorage(this.filledStorage + minute *this.megabyte);
    }

    /**
     * Creates a {@code SmartCamera} with values.
     * @param values An {@code ArrayList<String>} represents the parameters for create a {@code SmartCamera}.
     * @return A {@code SmartCamera}  object creates with parameters given.
     * @throws DeviceExceptions When there is an invalid parameter.
     */
    public static SmartCamera create(ArrayList<String> values) throws DeviceExceptions {
        validate(values);
        String name = values.get(0);
        float  megabyte = Float.parseFloat(values.get(1));
        boolean status = Validations.isIndex(values,2) ? Utilities.getStatusFromText(values.get(2)):false;
        return new SmartCamera(name,megabyte, status);
    }

    /**
     * <p>
     *     Validates the parameters that given for {@code SmartCamera}.
     * </p>
     *
     * @param values An {@code ArrayList<String>} represents the parameters for create a {@code SmartCamera} ()
     * @throws DeviceExceptions If there is a wrong parameter, throw a {@code DeviceException}
     * @see Validations#validateName(String)
     * @see Validations#validateMegabyte(String)
     * @see Validations#validateStatus(String)
     * @see Validations#checkDeviceNonExist(String)
     */
    private static void validate(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(1,3,values.size());
        String nameStr = Validations.isIndex(values, 0)? values.get(0): null;
        String megabyteStr = Validations.isIndex(values,1) ? values.get(1): null;
        String statusStr = Validations.isIndex(values,2) ? values.get(2): null;

        Validations.validateName(nameStr);
        Validations.validateMegabyte(megabyteStr);
        Validations.validateStatus(statusStr);
        Validations.checkDeviceNonExist(nameStr);

    }
}
