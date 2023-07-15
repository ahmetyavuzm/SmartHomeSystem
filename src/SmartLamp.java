import java.util.ArrayList;

/**
 * <p>
 *     The {@code SmartLamp} class is a subclass of {@code SmartDevice} class.
 *     It's contains necessary methods and properties for a smart lamp.
 * </p>
 * @see SmartDevice
 */
public class SmartLamp extends SmartDevice {
    /**
     * Kelvin value of the smart lamp.
     * <br>Determines the tone of white.
     * <br>Limits: 2000K - 6500K
     */
    public Integer kelvin = null;
    /**
     * Brightness of the smart lamp.
     * <br> Determines the brightness of light.
     * <br> Limits: 0% - 100%
     */
    public Integer brightness = null;

    /**
     * Creates a {@code SmartLamp}.
     * @param name A {@code String} that is name of the device.
     * @param cls A {@code Class} that is the class of the device.
     * @param status A {@code boolean} that is On/Off status of the device.
     */
    protected SmartLamp(String name, Class cls,boolean status) {
        super(name, cls);
        setStatus(status);
    }
    /**
     * Creates a {@code SmartLamp}.
     * @param name A {@code String} that is name of the device.
     * @param cls A {@code Class} that is the class of the device.
     * @param status A {@code boolean} that is On/Off status of the device.
     * @param kelvin An {@code Integer} that is the kelvin value between 2000K - 6500K.
     * @param brightness An {@code Integer} that is the brightness value between 0% - 100%.
     */
    public SmartLamp(String name, Class cls,Boolean status, Integer kelvin, Integer brightness) {
        super(name,cls);
        setStatus(status);
        setWhite(kelvin,brightness);
    }

    /**
     * Sets the {@link #kelvin} value of {@code SmartLamp}
     * @param kelvin An {@code Integer} that is the kelvin value between 2000K - 6500K.
     */
    public void setKelvin(Integer kelvin) {
        this.kelvin = kelvin;
    }
    /**
     * Sets the {@link #brightness} value of {@code SmartLamp}
     * @param brightness An {@code Integer} that is the brightness value between 0% - 100%.
     */
    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
    /**
     * Sets the {@link #kelvin} and {@link #brightness} value of {@code SmartLamp}
     * @param kelvin An {@code Integer} that is the kelvin value between 2000K - 6500K.
     * @param brightness An {@code Integer} that is the brightness value between 0% - 100%.
     */
    public void setWhite(int kelvin,int brightness){
        setKelvin(kelvin);
        setBrightness(brightness);
    }

    /**
     * Gets information about the {@code SmartLamp}.
     * @return A {@code String} that keeps information about the device.
     */
    public String getDeviceInfo(){
        return "Smart Lamp "+ this.name+ " is " + getStatusText() + " and its kelvin value is " + this.kelvin +"K "+
                "with "+ this.brightness+"% brightness, and its time to switch its status is " +getSwitchTimeText()+".";
    }

    /**
     * Creates a {@code SmartLamp} with values.
     * @param values An {@code ArrayList<String>} of the parameters for create a {@code SmartLamp}.
     * @return A {@code SmartLamp} object creates with parameters given.
     * @throws DeviceExceptions When there is an invalid value.
     */
    public static SmartLamp create(ArrayList<String> values) throws DeviceExceptions {
        validate(values);
        String name =values.get(0);
        Boolean status = Validations.isIndex(values,1) ? Utilities.getStatusFromText(values.get(1)):false;
        int kelvin = Validations.isIndex(values,2) ? Integer.parseInt(values.get(2)):4000;
        int brightness = Validations.isIndex(values,3) ? Integer.parseInt(values.get(3)):100;
        return new SmartLamp(name, SmartLamp.class, status, kelvin, brightness);
    }
    /**
     * <p>
     *     Validates the parameters that given for {@code SmartLamp}.
     * </p>
     *
     * @param values An {@code ArrayList<String>} represents the parameters for create a {@code SmartLamp}.
     * @throws DeviceExceptions If there is a wrong parameter, throws a {@code DeviceException}.
     * @see Validations#validateName(String)
     * @see Validations#validateStatus(String)
     * @see Validations#validateKelvin(String) 
     * @see Validations#validateBrightness(String)
     * @see Validations#checkDeviceNonExist(String)
     */
    protected static void validate(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(1,4,values.size());
        String nameStr = Validations.isIndex(values, 0)? values.get(0): null;
        String statusStr = Validations.isIndex(values,1) ? values.get(1): null;
        String kelvinStr = Validations.isIndex(values,2)? values.get(2): null;
        String brightnessStr = Validations.isIndex(values,3)? values.get(3): null;
        Validations.validateName(nameStr);
        Validations.validateStatus(statusStr);
        Validations.validateKelvin(kelvinStr);
        Validations.validateBrightness(brightnessStr);
        Validations.checkDeviceNonExist(nameStr);
    }
}
