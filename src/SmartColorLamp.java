import java.util.ArrayList;


/**
 * <p>
 *     The {@code SmartColorLamp} class is represents a smart color lamp. It is a subclass of {@code SmartLamp} class.
 *     It's contains necessary methods and properties for a smart color lamp.
 * </p>
 * @see SmartLamp
 * @see SmartDevice
 */
public class SmartColorLamp extends SmartLamp {

    /**
     * A {@code String} represents Hexadecimal color value of the {@code SmartColorLamp}.
     * <br>Example: Ox0AB094
     */
    public String colorCode = null;
    /**
     * Sets the {@link SmartColorLamp#colorCode} value of {@code SmartColorLamp}.
     * @param colorCode A {@code String} represents a Hexadecimal color. It takes values between 0x0 and 0xFFFFFF.
     */
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
        this.kelvin = null;
    }

    /**
     * Sets the {@link SmartColorLamp#kelvin} value of {@code SmartColorLamp}.
     * @param kelvin An {@code Integer} represents the kelvin value of the color lamp.
     *               It takes values between 2000 and 6500.
     */
    @Override
    public void setKelvin(Integer kelvin){
        this.kelvin = kelvin;
        this.colorCode = null;
    }

    /**
     * Sets color and brightness of the {@code SmartColorLamp}.
     * @param colorCode A {@code String} represents a Hexadecimal color. It takes values between 0x0 and 0xFFFFFF.
     * @param brightness An {@code int} represents the brightness of the color lamp.
     *                   It takes values between 0%-100%.
     */
    public void setColor(String colorCode, int brightness) {
        setColorCode(colorCode);
        setBrightness(brightness);

    }

    /**
     * Creates An {@code SmartColorLamp} with values that given.
     * @param name A {@code String}represents Name of the smart color lamp.
     * @param initial_status A {@code Boolean} represents On/Off status of the smart color lamp.
     * @param kelvin An {@code Integer} represents Kelvin value of the smart color lamp.
     *               It takes values between 2000K-6500K.
     * @param brightness An {@code Integer} represents the brightness of the color lamp.
     *                   It takes values between 0%-100%.
     */
    public SmartColorLamp(String name,Boolean initial_status, Integer kelvin, Integer brightness) {
        super(name,SmartColorLamp.class,initial_status, kelvin, brightness);
    }
    /**
     * Creates An {@code SmartColorLamp} with values that given.
     * @param name A {@code String} represents Name of the smart color lamp.
     * @param initial_status A {@code Boolean} represents On/Off status of the smart color lamp.
     * @param colorCode A {@code String} represents a Hexadecimal color. It takes values between 0x0 and 0xFFFFFF.
     * @param brightness An {@code Integer} represents the brightness of the color lamp.
     *                   It takes values between 0%-100%.
     */
    public SmartColorLamp(String name,Boolean initial_status, String colorCode , Integer brightness) {
        super(name, SmartColorLamp.class, initial_status);
        setColor(colorCode, brightness);
    }

    /**
     * Gets {@code SmartColorLamp} information.
     * @return A {@code String} that contains information about {@code SmartColorLamp}.
     */
    @Override
    public String getDeviceInfo(){
        String colorText = this.kelvin == null? this.colorCode: String.valueOf(this.kelvin) + "K";
        return "Smart Color Lamp "+ this.name+ " is " + getStatusText() + " and its color value is " + colorText +
                " with "+ this.brightness+"% brightness, and its time to switch its status is " +getSwitchTimeText()+".";
    }

    /**
     * Creates a {@code SmartColorLamp} with values.
     * @param values An {@code ArrayList<String>} of the parameters for create a {@code SmartColorLamp}.
     * @return A {@code SmartColorLamp>} object creates with parameters given.
     * @throws DeviceExceptions When there is an invalid parameter.
     */
    public static SmartColorLamp create(ArrayList<String> values) throws DeviceExceptions{
        if (!Validations.isIndex(values,2) || Validations.isCanInt(values.get(2))){
            SmartLamp.validate(values);
            String name = values.get(0);
            Boolean status = Validations.isIndex(values,1) ? Utilities.getStatusFromText(values.get(1)):false;
            int kelvin = Validations.isIndex(values,2) ? Integer.parseInt(values.get(2)):4000;
            int brightness = Validations.isIndex(values,3) ? Integer.parseInt(values.get(3)):100;
            return new SmartColorLamp(name,status, kelvin, brightness);
        }else{
            String name = values.get(0);
            Boolean status = Validations.isIndex(values,1) ? Utilities.getStatusFromText(values.get(1)):false;
            String colorCode = values.get(2);
            int brightness = Validations.isIndex(values,3) ? Integer.parseInt(values.get(3)):100;
            SmartColorLamp.validate(values);
            return new SmartColorLamp(name,status, colorCode, brightness);
        }

    }
    /**
     * <p>
     *     Validates the parameters that given for {@code SmartColorLamp}.
     * </p>
     *
     * @param values An {@code ArrayList<String>} represents the parameters for create a {@code SmartColorLamp}.
     * @throws DeviceExceptions If there is a wrong parameter, throws a {@code DeviceException}.
     * @see Validations#validateName(String)
     * @see Validations#validateStatus(String) 
     * @see Validations#validateColorCode(String) 
     * @see Validations#validateBrightness(String)  
     * @see Validations#checkDeviceNonExist(String)
     */
    protected static void validate(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(1,4,values.size());
        String nameStr = Validations.isIndex(values, 0)? values.get(0): null;
        String statusStr = Validations.isIndex(values,1) ? values.get(1): null;
        String colorCodeStr = Validations.isIndex(values,2)? values.get(2):null;
        String brightness = Validations.isIndex(values,3)? values.get(3):null;
        Validations.validateName(nameStr);
        Validations.validateStatus(statusStr);
        Validations.validateColorCode(colorCodeStr);
        Validations.validateBrightness(brightness);
        Validations.checkDeviceNonExist(nameStr);
    }
}
