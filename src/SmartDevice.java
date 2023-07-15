import java.util.*;

/**
 * <p>
 *   The {@code SmartDevice} class is the base class of all smart devices.
 *   It contains basic device control methods and properties.
 * </p>
 */
public abstract class SmartDevice {
    private static int idCounter = 0;
    /**
     * <p>
     *     Contains all smart devices according to their id values.
     * </p>
     * <i>{@code Key -}</i> An {@code Integer} represents device id.
     * <br>
     * <i>{@code Value -}</i> A {@code SmartDevice} represents device.
     */
    private static Dictionary<Integer,SmartDevice> devices = new Hashtable<>();
    /**
     * <p>
     *     Contains all smart device's ids according to their names.
     * </p>
     * <i>{@code Key -}</i> A {@code String} value represents device name.
     * <br>
     * <i>{@code Value -}</i> An {@code Integer} value represents device id.
     */
    public static Dictionary<String,Integer> deviceNames= new Hashtable<>();
    private int id;
    public String name;
    /**
     * Class value of this object.
     */
    protected Class cls;
    /**
     * A {@code boolean} value represents On/Off status of device.
     */
    public boolean status = false;
    /**
     * A {@code Date} object represents when device turns on.
     */
    protected Date startTime;
    /**
     * A {@code Date} object represents when device turns off.
     */
    protected Date stopTime;
    /**
     * An {@code Integer} value represents the workload id that controls {@link SmartDevice#status}.
     * @see Workload
     */
    public Integer switchTimeWorkloadId = null;

    /**
     * Base constructor of the all smart devices.
     * @param name A {@code String} represents the device name.
     * @param cls A {@code Class} represents class value of the device.
     */
    public SmartDevice(String name, Class cls){
        setName(name);
        setId();
        setCls(cls);
        devices.put(this.id, this);
        deviceNames.put(this.name, this.id);
    }

    /**
     * Gets {@code SmartDevice} {@link SmartDevice#id}.
     * @return An {@code int} value that represents device id.
     */
    public int getId() {
        return this.id;
    }
    /**
     * Gets {@code SmartDevice} {@link  SmartDevice#cls}.
     * @return An {@code Class} value that represents device class.
     */
    public Class getCls() {
        return cls;
    }
    /**
     * Sets the {@link SmartDevice#id} property of {@code SmartDevice}.
     */
    private void setId() {
        this.id = idCounter;
        idCounter +=1;
    }
    /**
     * Sets the {@link SmartDevice#status} property of {@code SmartDevice}.
     * @param status A {@code Boolean} object.
     */
    public void setStatus(Boolean status){
        this.status = status;
    }
    /**
     * Sets the {@link SmartDevice#name} property of {@code SmartDevice}.
     * @param name A {@code String} object.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the {@link SmartDevice#cls} property of {@code SmartDevice}.
     * @param cls A {@code Class} object.
     */
    private void setCls(Class cls){
        this.cls = cls;
    }
    /**
     * Sets the {@link SmartDevice#startTime} property of {@code SmartDevice}.
     * @param startTime A {@code Date} object that represents the time of turned on.
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    /**
     * @param stopTime A {@code Date} represents the time of turned off.
     * Sets the {@link SmartDevice#stopTime} property of {@code SmartDevice} .
     */
    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    /**
     * Gets information about the {@code SmartDevice}.
     * @return A {@code String} that keeps information about the device.
     */
    abstract String getDeviceInfo();

    /**
     * Switches the status with new status.
     * @param time A {@code Date} that represents switch time
     * @param newStatus A {@code boolean} that represents new status value.
     */
    public void switchStatus(Date time, boolean newStatus){
        this.switchTimeWorkloadId = null;
        if (newStatus){
            on(time);
            return;
        }
        off(time);
    }
    /**
     * Turns on the {@code SmartDevice}.
     * @param startTime A {@code Date} object that represents the time of turned on.
     * @see Date
     */
    public void on(Date startTime){
        setStatus(true);
        setStartTime(startTime);
    }

    /**
     * Turns off the {@code SmartDevice}.
     * @param stopTime A {@code Date} represents the time of turned off.
     * @see Date
     */
    public void off(Date stopTime){
        setStatus(false);
        setStopTime(stopTime);
    }
    /**
     * Gets suitable on/off text according to {@link SmartDevice#status} value.
     * @return A String that can be "on" or "off".
     */
    protected String getStatusText(){
        return this.status ? "on": "off";
    }

    /**
     * Gets the {@code SmartDevice}'s time of switch time workload as string.
     * @return A String that represents the device switch time.
     * @see Workload
     */
    protected String getSwitchTimeText(){
        if (Validations.isNull(this.switchTimeWorkloadId)){
            return "null";
        }
        Workload workload = Workload.getWorkload(this.switchTimeWorkloadId);
        return Utilities.convertDateToString(workload.time);
    }

    /**
     * Changes the name of the {@code SmartDevice}.
     * @param deviceName A {@code String} that represents name of the device that will change.
     * @param newName A {@code String} that represents new name of the device.
     */
    public static void changeDeviceName(String deviceName, String newName){
        Integer id = deviceNames.get(deviceName);
        deviceNames.remove(deviceName);
        deviceNames.put(newName,id);
        SmartDevice device = getDeviceById(id);
        device.name = newName;
    }

    /**
     * Gets the {@code SmartDevice} with using its name.
     * @param name A {@code String} that represents the name of device.
     * @return An {@code SmartDevice}.
     */
    public static SmartDevice getDeviceByName(String name){
        Integer id = deviceNames.get(name);
        return getDeviceById(id);
    }
    /**
     * Gets the {@code SmartDevice} with using its id.
     * @param id A {@code Integer} that represents the id of device.
     * @return An {@code SmartDevice}.
     */
    public static SmartDevice getDeviceById(Integer id){
        return devices.get(id);
    }
    /**
     * Removes the {@code SmartDevice} with using its name.
     * @param name A {@code String} that represents the name of device.
     * @return An {@code SmartDevice} is the removed device.
     */
    public static SmartDevice removeDeviceByName(String name){
        Integer id = deviceNames.get(name);
        return  removeDeviceById(id);
    }
    /**
     * Removes the {@code SmartDevice} with using its id.
     * @param id A {@code Integer} that represents the id of device.
     * @return An {@code SmartDevice} is the removed device.
     */
    public static SmartDevice removeDeviceById(Integer id){
        SmartDevice device = getDeviceById(id);
        deviceNames.remove(device.name);
        devices.remove(device.id);
        return device;
    }
}
