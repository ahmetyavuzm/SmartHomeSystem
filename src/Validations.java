import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.regex.Pattern;

/**
 * Contains all validations for necessary methods of the program.
 */
public class Validations {
    /**
     * Checks there is an item at the index that given.
     * @param list An {@code ArrayList}.
     * @param index An {@code integer}.
     * @return A {@code boolean}, If there is an item returns true.
     */
    public static boolean isIndex(ArrayList list, int index){
        try{
            list.get(index);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Checks the string that given is "On" or "Off"
     * @param str A {@code String}.
     * @return A {@code boolean}, If string is equal to "On" or "Off" returns true.
     */
    public static boolean isStatus(String str){
        return str.equals("On") || str.equals("Off");
    }
    /**
     * Checks the string that given is can be an integer.
     * @param str A {@code String}.
     * @return A {@code boolean}, If is can be an integer returns true.
     */
    public static boolean isCanInt(String str){
        Pattern pattern = Pattern.compile("[-+]?[0-9]+");
        return pattern.matcher(str).matches();
    }
    /**
     * Checks the string that given is can be a float.
     * @param str A {@code String}.
     * @return A {@code boolean}, If is can be an float returns true.
     */
    public static boolean isCanFloat(String str){
        Pattern pattern = Pattern.compile("^[-+]?[0-9]*\\.?[0-9]+$");
        return pattern.matcher(str).matches();
    }

    /**
     * Checks the string that given is a color code.
     * <br> Limits: 0x000000 - 0xFFFFFF
     * @param str A {@code String}.
     * @return A {@code boolean}, If is a color code returns true.
     *
     */
    public static boolean isColorCode(String str){
        Pattern pattern = Pattern.compile("^0x([A-F0-9]{6})$");
        return pattern.matcher(str).matches();
    }
    /**
     * Checks the string that given is hexadecimal.
     * <br> Limits: 0x[0-9A-F]+
     * @param str A {@code String}.
     * @return A {@code boolean}, If is hexadecimal returns true.
     */
    public static boolean isHexadecimal(String str){
        Pattern pattern = Pattern.compile("^0x([0-9A-F]+)$");
        return pattern.matcher(str).matches();
    }
    /**
     * Checks the string that given is in a true date format.
     * @param str A {@code String}.
     * @return A {@code boolean}, If is in a true date format returns true.
     */
    public static boolean isTrueDateFormat(String str){
        Pattern pattern = Pattern.compile("^\\d{4}-(\\d|0[1-9]|1[0-2])-([012]\\d|3[01]|\\d)_([01]\\d|2[0-3]|\\d):([0-5]\\d|\\d):([0-5]\\d|\\d)$");
        return pattern.matcher(str).matches();
    }

    /**
     * Checks the times are the same or not.
     * @param time1 A {@code Date} object that represents time that wantings to check
     * @param time2 A {@code Date} object for using as reference.
     * @return If times are the same according to currentTime returns true.
     */
    public static boolean isSameTime(Date time1, Date time2){
        return time1.getTime() == time2.getTime();
    }
    /**
     * Checks the time is passed or not.
     * @param time A {@code Date} object that represents time that wantings to check
     * @param currentTime A {@code Date} object for using as reference.
     * @return If time passed according to currentTime returns true.
     */
    public static boolean isPassedTime(Date time, Date currentTime){
        return time.getTime() < currentTime.getTime();
    }

    /**
     * Checks the dictionary object has a key same with key that given.
     * @param dict A {@code Dictionary<T,V>} object.
     * @param key A {@code <T>} object.
     * @return If there is a key same with the key that given in dict, returns true.
     */
    public static <T,V> boolean isLegalKey(Dictionary<T,V> dict, T key){
        return dict.get(key) != null;
    }

    /**
     * Checks the number is positive or not.
     * @param num A {@code float} number.
     * @return If number is positive, returns true.
     */
    public static boolean isPositive(float num){
        return num > 0;
    };
    /**
     * Checks the number is negative or not.
     * @param num A {@code float} number.
     * @return If number is negative, returns true.
     */
    public static boolean isNegative(float num){
        return num < 0;
    };

    /**
     * Checks the number is zero or not.
     * @param num A {@code float} number.
     * @return If number is zero, returns true.
     */
    public static boolean isZero(float num){
        return num == 0;
    }

    /**
     * Checks the object that given is null or not.
     * @param obj An {@code Object}.
     * @return If the object is null, returns true.
     */
    public static boolean isNull(Object obj){
        return obj == null;
    }

    /**
     * Checks the given integer is in the range.
     * @param min An {@code int} that represents the minimum value.
     * @param max An {@code int} that represents the maximum value.
     * @param num An {@code int}.
     * @return If the given integer is in the range, returns true.
     */
    private static boolean isInRange(int min, int max, int num){
        return num>= min & num <= max;
    }

    /**
     * Checks the given text is empty or not.
     * @param text A {@code String}.
     * @return if the string is just contains " "(Space) and "\t"(Tab), return true.
     */
    public static boolean isEmptyCommand(String text){
        Pattern pattern = Pattern.compile("^[ \t]+$");
        return pattern.matcher(text).matches() || text.isEmpty();
    }

    /**
     * Checks the number, is in range.
     * @param min An {@code Integer} that represents minimum number that num param can be.
     * @param max An {@code Integer} that represents maximum number that num param can be.
     * @param num An {@code Integer} that is going to check.
     * @throws DeviceExceptions.InvalidCommandException if is not in range, it gives an error.
     */
    public static void checkRange(int min ,int max, int num) throws DeviceExceptions.InvalidCommandException {
        if(!isInRange(min,max,num)){
            throw new DeviceExceptions.InvalidCommandException("ERROR: Erroneous command!");
        }
    }

    /**
     * Checks the there is missing arguments in a list.
     * @param values An {@code ArrayList}.
     * @param indexes {@code int...} varargs that represents the indexes that is wanted to check.
     * @throws DeviceExceptions.MissingArgumentException If there is a missing argument, gives an error.
     */
    public static void checkMissingArgs(ArrayList<?> values,int... indexes) throws DeviceExceptions.MissingArgumentException {
        try{
            for (int index : indexes){
                values.get(index);
            }
        }catch (Exception e){
            throw new DeviceExceptions.MissingArgumentException("ERROR: Erroneous command!");
        }
    }
    /**
     * Checks the there is a device with name that given.
     * @param deviceName A {@code String}, represents device name.
     * @throws DeviceExceptions.IllegalArgumentException If there is a device, gives an error.
     * @see SmartDevice
     * @see SmartDevice#deviceNames
     */
    public static void checkDeviceNonExist(String deviceName) throws DeviceExceptions.IllegalArgumentException{
        if (Validations.isLegalKey(SmartDevice.deviceNames,deviceName)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: There is already a smart device with same name!");
        }
    }
    /**
     * Checks the there is a device with name that given.
     * @param deviceName A {@code String}, represents device name.
     * @throws DeviceExceptions.IllegalArgumentException If there is not a device, gives an error.
     * @see SmartDevice
     * @see SmartDevice#deviceNames
     */
    public static void checkDeviceExist(String deviceName) throws DeviceExceptions.IllegalArgumentException {
        if (!Validations.isLegalKey(SmartDevice.deviceNames,deviceName)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: There is not such a device!");
        }
    }

    /**
     * Checks the device is a SmartPlug.
     * @param device A {@code String}, represents device.
     * @throws DeviceExceptions.IllegalArgumentException If there is not a SmartPlug, gives an error.
     * @see SmartPlug
     */
    public static void checkSmartPlug(SmartDevice device) throws DeviceExceptions{
        if (!device.cls.equals(SmartPlug.class)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: This device is not a smart plug!");
        }
    }
    /**
     * Checks the device is a SmartLamp.
     * @param device A {@code String}, represents device.
     * @throws DeviceExceptions.IllegalArgumentException If there is not a SmartLamp, gives an error.
     * @see SmartLamp
     */
    public static void checkSmartLamp(SmartDevice device) throws DeviceExceptions{
        if (!(device.cls.equals(SmartLamp.class) || device.cls.equals(SmartColorLamp.class))){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: This device is not a smart lamp!");
        }

    }
    /**
     * Checks the device is a SmartColorLamp.
     * @param device A {@code String}, represents device.
     * @throws DeviceExceptions.IllegalArgumentException If there is not a SmartColorLamp, gives an error.
     * @see SmartColorLamp
     */
    public static void checkSmartColorLamp(SmartDevice device) throws DeviceExceptions{
        if (!device.cls.equals(SmartColorLamp.class)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: This device is not a smart color lamp!");
        }
    }

    /**
     * Checks the name of the {@code SmartDevice}.
     * @param nameStr A {@code String} : name of the device.
     * @throws DeviceExceptions.IllegalArgumentException If the name is null, it gives an error.
     */
    public static void validateName(String nameStr) throws DeviceExceptions.IllegalArgumentException {
        if (Validations.isNull(nameStr)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
        }
    }
    /**
     * Checks the status of the {@code SmartDevice}.
     * @param statusStr A {@code String} : status of the device.
     * @throws DeviceExceptions.IllegalArgumentException If the status exist and different
     * from "On" or "Off", it gives an error.
     */
    public static void validateStatus(String statusStr) throws DeviceExceptions.IllegalArgumentException {
        if (!Validations.isNull(statusStr)){
            if (!Validations.isStatus(statusStr)){
                throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
            }
        }
    }
    /**
     * Checks the ampere of the {@code SmartPlug}.
     * @param ampereStr A {@code String} : ampere of the plugged device.
     * @throws DeviceExceptions.IllegalArgumentException If the ampere exist and not can be
     * a float number or not positive, it gives an error.
     */
    public static void validateAmpere(String ampereStr) throws DeviceExceptions.IllegalArgumentException {
        if (!Validations.isNull(ampereStr)){
            if (Validations.isCanFloat(ampereStr)){
                float ampere = Float.parseFloat(ampereStr);
                if (!Validations.isPositive(ampere))
                    throw new DeviceExceptions.IllegalArgumentException("ERROR: Ampere value must be a positive number!");
            }else{
                throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
            }
        }
    }
    /**
     * Checks the kelvin of the {@code SmartLamp}.
     * @param kelvinStr A {@code String} : kelvin value of the device.
     * @throws DeviceExceptions.IllegalArgumentException If the kelvin exist and not can be
     * an integer or not range in 2000-6000, it gives an error.
     */
    public static void validateKelvin(String kelvinStr) throws DeviceExceptions.IllegalArgumentException {
        if (!Validations.isNull(kelvinStr)){
            if (Validations.isCanInt(kelvinStr)){
                int kelvin = Integer.parseInt(kelvinStr);
                if (!Validations.isInRange(2000,6500,kelvin))
                    throw new DeviceExceptions.IllegalArgumentException("ERROR: Kelvin value must be in range of 2000K-6500K!");
            }else{
                throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
            }
        }
    }
    /**
     * Checks the brightness of the {@code SmartLamp}.
     * @param brightnessStr A {@code String} : brightness value of the device.
     * @throws DeviceExceptions.IllegalArgumentException If the brightness exist and not can be
     * an integer or not range in 0-100, it gives an error.
     */
    public static void validateBrightness(String brightnessStr) throws DeviceExceptions.IllegalArgumentException {
        if (!Validations.isNull(brightnessStr)){
            if (Validations.isCanInt(brightnessStr)){
                int brightness = Integer.parseInt(brightnessStr);
                if (!Validations.isInRange(0,100,brightness))
                    throw new DeviceExceptions.IllegalArgumentException("ERROR: Brightness must be in range of 0%-100%!");
            }else{
                throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
            }
        }
    }
    /**
     * Checks the color code of the {@code SmartColorLamp}.
     * @param colorCodeStr A {@code String} : color code of the device.
     * @throws DeviceExceptions.IllegalArgumentException If the color code exist and not in
     * true color code format, it gives an error.
     */
    public static void validateColorCode(String colorCodeStr) throws DeviceExceptions.IllegalArgumentException {
        if (!Validations.isNull(colorCodeStr)){
            if (!Validations.isHexadecimal(colorCodeStr)){
                throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
            }
            if (!Validations.isColorCode(colorCodeStr)){
                throw new DeviceExceptions.IllegalArgumentException("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
            }
        }
    }
    /**
     * Checks the megabyte of the {@code SmartPlug}.
     * @param megabyteStr A {@code String} : megabyte of the plugged device.
     * @throws DeviceExceptions.IllegalArgumentException If the megabyte not exist or not can be
     * a float number or not positive, it gives an error.
     */
    public static void validateMegabyte(String megabyteStr) throws DeviceExceptions.IllegalArgumentException {
        if (Validations.isNull(megabyteStr)){
           throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
        }
        if (Validations.isCanFloat(megabyteStr)){
            float megabyte = Float.parseFloat(megabyteStr);
            if (!Validations.isPositive(megabyte))
                throw new DeviceExceptions.IllegalArgumentException("ERROR: Megabyte value must be a positive number!");
        }else{
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
        }

    }

    /**
     * Checks the {@link Action#actionName} is a valid action in this program.
     * @param action An {@code Action} from user
     * @throws DeviceExceptions.InvalidCommandException If the action name is not in
     * {@link SmartDeviceController#methodMapper}, gives an error.
     */
    public static void validateAction(Action action) throws DeviceExceptions.InvalidCommandException {
        if (Validations.isNull(SmartDeviceController.methodMapper.get(action.actionName))){
            throw new DeviceExceptions.InvalidCommandException("ERROR: Erroneous command!");
        }
    }
}
