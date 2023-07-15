import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * The {@code DeviceLogger} class sorts {@code SmartDevice}'s ids and keeps orders for reporting.
 *
 * @see SmartDeviceController#zReport(ArrayList)
 */
public class DeviceLogger {

    /**
     * <p>
     * Represents the list that {@code SmartDevice}'s id in which list
     * ({@link DeviceLogger#nonExecuted}, {@link DeviceLogger#executed},{@link DeviceLogger#nonOperation})
     * </p>
     *
     *<b>Key</b>:  id of {@code SmartDevice} ({@code Integer})
     *<br>
     *<b>Value</b>: list of {@code SmartDevice}'s ids ({@code ArrayList<Integer>})
     * @see SmartDevice
     */
    private static Dictionary<Integer, ArrayList<Integer>> deviceMap = new Hashtable<>();
    /**
     * Contains the list of {@code SmartDevice}'s ids that workload does not execute yet.
     * @see Workload
     * @see SmartDevice
     */
    public static ArrayList<Integer> nonExecuted = new ArrayList<>();
    /**
     * Contains the list of {@code SmartDevice}'s ids that workload was executed.
     * @see Workload
     * @see SmartDevice
     */
    public static ArrayList<Integer> executed = new ArrayList<>();
    /**
     * Contains the list of {@code SmartDevice}'s ids that does not have any workload.
     * @see Workload
     * @see SmartDevice
     */
    public static ArrayList<Integer> nonOperation = new ArrayList<>();

    /**
     * Gets ordered list of {@code SmartDevice}'s ids.
     * @return An {@code ArrayList<Integer>} ordered list of {@code SmartDevice}'s ids;
     * @see SmartDevice
     */
    public static ArrayList<Integer> getOrderedDeviceIDs(){
        ArrayList<Integer> orderedList = new ArrayList<>();
        orderedList.addAll(nonExecuted);
        orderedList.addAll(executed);
        orderedList.addAll(nonOperation);
        return orderedList;
    }
    /**
     * Add a {@code SmartDevice}'s id to executed list to specified index.
     * @param device A {@code SmartDevice} that will add.
     * @param index A {@code int} represents the index will add.
     * @see SmartDevice
     * @see DeviceLogger#executed
     */
    public static void addExecuted(SmartDevice device, int index){
        delete(device.getId());
        executed.add(index,device.getId());
        deviceMap.put(device.getId(), executed);
    }

    /**
     * Add a {@code SmartDevice}'s id to nonExecuted list to true index.
     * @param device A {@code SmartDevice} that will add.
     * @see SmartDevice
     * @see DeviceLogger#nonExecuted
     * @see DeviceLogger#trueDeviceIndex(ArrayList, SmartDevice)
     */
    public static void addNonExecuted(SmartDevice device){
        delete(device.getId());
        int index = trueDeviceIndex(nonExecuted,device);
        nonExecuted.add(index,device.getId());
        deviceMap.put(device.getId(), nonExecuted);
    }
    /**
     * Add a {@code SmartDevice}'s id to nonOperation list to last index.
     * @param device A {@code SmartDevice} that will add.
     * @see SmartDevice
     * @see DeviceLogger#nonOperation
     */
    public static void addNonOperation(SmartDevice device){
        delete(device.getId());
        nonOperation.add(device.getId());
        deviceMap.put(device.getId(), nonOperation);
    }

    /**
     * Deletes a {@code SmartDevice}'s id in true list and {@code deviceMap} .
     * @param id : {@code SmartDevice}'s id.
     * @see SmartDevice
     * @see DeviceLogger#deviceMap
     */
    protected static void delete(Integer id){
        ArrayList<Integer> list = deviceMap.get(id);
        if(!Validations.isNull(list)){
            list.remove(id);
        }
    }

    /**
     * Finds the true index of {@code SmartDevice}'s id in deviceIDs list.
     * @param deviceIDs {@code SmartDevice}'s ids list ({@code ArrayList<Integer>}).
     * @param device A {@code SmartDevice}.
     * @return true index value for device id.
     * @see SmartDevice
     * @see Utilities#getDeviceIndexBySwitchTime(ArrayList, SmartDevice)
     */
    private static int trueDeviceIndex(ArrayList<Integer> deviceIDs, SmartDevice device) {
        ArrayList<SmartDevice> devices = getDevicesByIDs(deviceIDs);
        return Utilities.getDeviceIndexBySwitchTime(devices,device);
    }

    /**
     * Get {@code SmartDevice}s from deviceIDs.
     * @param deviceIDs {@code SmartDevice}'s ids list ({@code ArrayList<Integer>}).
     * @return An {@code ArrayList<SmartDevice>} contains {@code SmartDevice}s.
     * @see SmartDevice
     */
    private static ArrayList<SmartDevice> getDevicesByIDs(ArrayList<Integer> deviceIDs){
        ArrayList<SmartDevice> devices  = new ArrayList<>();
        for(Integer deviceID: deviceIDs){
            SmartDevice device = SmartDevice.getDeviceById(deviceID);
            devices.add(device);
        }
        return devices;
    }
}
