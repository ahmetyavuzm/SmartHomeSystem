import java.util.ArrayList;
import java.util.Date;

/**
 * <p>
 *     The {@code SmartPlug} class is a subclass of {@code SmartDevice} class.
 *     It's contains necessary methods and properties for a smart plug.
 * </p>
 * @see SmartDevice
 */
public class SmartPlug extends SmartDevice{
    /**
     * Constant voltage value
      */
    private final int voltage = 220;
    /**
     * Total energy that consumed.
     */
    public float totalEnergy = 0;
    /**
     * Ampere value of the device that plugged in.
     */
    public float ampere = 0;
    /**
     * Shows there is a device plugged in this plug.
     */
    public boolean plug = false;
    /**
     * Shows the time when a device plugs in.
     */
    private Date plugInTime = null;

    /**
     * Sets the {@link #ampere} value
     * @param ampere A {@code float} number that is positive.
     */
    public void setAmpere(float ampere) {
        this.ampere = ampere;
    }
    /**
     * Sets the {@link #plug} value
     * @param plug A {@code boolean}
     */
    public void setPlug(boolean plug) {
        this.plug = plug;
    }
    /**
     * Sets the {@link #plugInTime} value
     * @param plugInTime A {@code Date}
     */
    private void setPlugInTime(Date plugInTime) {
        this.plugInTime = plugInTime;
    }

    /**
     * Creates a {@code SmartPlug}.
     * @param name A {@code String} that is name of the device.
     * @param status A {@code boolean} that is On/Off status of the device.
     * @param ampere A {@code Float} that represents ampere value of the device that plugged in.
     */
    public SmartPlug(String name, Boolean status,Float ampere) {
        super(name, SmartPlug.class);
        if(status){
            on(SmartDeviceController.time);
        }
        if (ampere !=0 ){
            plugIn(ampere,SmartDeviceController.time);
        }
    }

    /**
     * Plugs in a device to the plug
     * @param ampere A {@code Float} that represents ampere value of the device that plugged in.
     * @param plugInTime A {@code Date} that represents the time when the device plugs in
     */
    public void plugIn(float ampere, Date plugInTime){
        setPlugInTime(plugInTime);
        setPlug(true);
        setAmpere(ampere);
    }

    /**
     * Plugs out the device that plugged in from the plug
     * @param plugOutTime A {@code Date} that represents the time when the device plugs out.
     */
    public void plugOut(Date plugOutTime){
        if (status){
            updateEnergy(this.plugInTime,plugOutTime);
        }
        setPlug(false);
        setAmpere(0);
        setPlugInTime(null);
    }

    /**
     * Gets information about the {@code SmartPlug}.
     * @return A {@code String} that keeps information about the device.
     */
    public String getDeviceInfo(){
        return "Smart Plug "+ this.name+ " is " + getStatusText() + " and consumed " + String.format("%.2f",this.totalEnergy) +"W "+
                "so far (excluding current device), and its time to switch its status is " +getSwitchTimeText()+".";
    }

    /**
     * Turns on the {@code SmartPlug}.
     * @param startTime A {@code Date} object that represents the time of turned on.
     */
    @Override
    public void on(Date startTime){
        setStatus(true);
        setStartTime(startTime);
        if (plug){
            setPlugInTime(startTime);
        }
    }
    /**
     * Turns off the {@code SmartPlug} and updates filled storage.
     * @param stopTime A {@code Date} represents the time of turned off.
     */
    @Override
    public void off(Date stopTime){
        setStatus(false);
        updateEnergy(this.plugInTime,stopTime);
        if (plug){
            setPlugInTime(stopTime);
        }
        setStartTime(null);
    }

    /**
     * Updates the total consumed energy.
     * @param startTime A {@code Date} that represents the time when the device starts drawing amps.
     * @param stopTime A {@code Date} that represents the time when the device stops drawing amps
     * @see #totalEnergy
     */
    private void updateEnergy(Date startTime, Date stopTime){
        if(!Validations.isNull(startTime) & !Validations.isNull(stopTime)){
            long diff = stopTime.getTime()-startTime.getTime();
            Float hour = Utilities.millisecondToHour(diff);
            this.totalEnergy += this.voltage*this.ampere *hour;
        }
    }

    /**
     * Creates a {@code SmartPlug} with values.
     * @param values An {@code ArrayList<String>} of the parameters for create a {@code SmartPlug}.
     * @return A {@code SmartPlug} object creates with parameters given.
     * @throws DeviceExceptions When there is an invalid value.
     */
    public static SmartPlug create(ArrayList<String> values) throws DeviceExceptions {
        validate(values);
        String name = values.get(0);
        Boolean status = Validations.isIndex(values,1) ? Utilities.getStatusFromText(values.get(1)):false;
        Float ampere = Validations.isIndex(values,2) ? Float.parseFloat(values.get(2)) : 0;
        return new SmartPlug(name, status, ampere);
    }
    /**
     * <p>
     *     Validates the parameters that given for {@code SmartPlug}.
     * </p>
     *
     * @param values An {@code ArrayList<String>} represents the parameters for create a {@code SmartPlug}.
     * @throws DeviceExceptions If there is a wrong parameter, throws a {@code DeviceException}.
     * @see Validations#validateName(String)
     * @see Validations#validateStatus(String)
     * @see Validations#validateAmpere(String) 
     * @see Validations#checkDeviceNonExist(String)
     */
    private static void validate(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(1,3,values.size());
        String nameStr = Validations.isIndex(values, 0)? values.get(0): null;
        String statusStr = Validations.isIndex(values,1) ? values.get(1): null;
        String ampereStr = Validations.isIndex(values,2)? values.get(2): null ;
        Validations.validateName(nameStr);
        Validations.validateStatus(statusStr);
        Validations.validateAmpere(ampereStr);
        Validations.checkDeviceNonExist(nameStr);
    }
}
