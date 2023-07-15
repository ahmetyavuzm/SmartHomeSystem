import java.lang.reflect.Array;
import java.util.*;

/**
 * The {@code SmartDeviceController} class is contains all methods for control all {@code SmartDevices}
 * @see SmartDevice
 */
public class SmartDeviceController {

    /**
     * A reference time for control devices.
     */
    protected static Date time = null;
    /**
     * Contains workloads that will execute according to their executing times.
     * <br>
     * <i>{@code Key -}</i> An {@code Date} that represents executing time.
     * <br>
     *<i>{@code Value -}</i> A {@code ArrayList<Workload>} that represents workloads.
     * @see Workload
     */
    public static Dictionary<Date, ArrayList<Workload>> workloads = new Hashtable<>();

    /**
     * Mapping dictionary for converting user {@code Action} names to method names.
     * @see Action
     */
    public final static Dictionary<String, String> methodMapper = new Hashtable<>();

    static {
        methodMapper.put("Add","add");
        methodMapper.put("Remove","remove");
        methodMapper.put("SkipMinutes","skipMinutes");
        methodMapper.put("SetTime","setTime");
        methodMapper.put("SetInitialTime","setInitialTime");
        methodMapper.put("Switch","switchStatus");
        methodMapper.put("ChangeName","changeName");
        methodMapper.put("SetSwitchTime","setSwitchTime");
        methodMapper.put("PlugIn","plugIn");
        methodMapper.put("PlugOut","plugOut");
        methodMapper.put("SetKelvin","setKelvin");
        methodMapper.put("SetBrightness","setBrightness");
        methodMapper.put("SetColor","setColor");
        methodMapper.put("SetColorCode","setColorCode");
        methodMapper.put("SetWhite","setWhite");
        methodMapper.put("ZReport","zReport");
        methodMapper.put("Nop","nop");
    }

    /**
     * Converts the {@code Action} to {@code MethodClass} and invokes the method.
     * @param action An {@code Action} that provides from user.
     * @return An {@code Object} that contains response of the invoked method.
     * @throws DeviceExceptions If there is an illegal {@code Action}, it throws an DeviceException.
     * @see Action
     * @see MethodClass
     */
    public static Object actionHandler(Action action) throws DeviceExceptions{
        String responseText = null;
        try{
            if (Validations.isEmptyCommand(action.commandText)) {
                return null;
            }
            responseText = "COMMAND: " + action.commandText +"\n";
            if (Validations.isNull(time) & (!action.actionName.equals("SetInitialTime") || action.values.size() == 0)){
                throw new DeviceExceptions.InitialTimeException("ERROR: First command must be set initial time! Program is going to terminate!\n");
            }
            Validations.validateAction(action);
            MethodClass method = new MethodClass(
                    SmartDeviceController.class,
                    SmartDeviceController.methodMapper.get(action.actionName),
                    ArrayList.class);
            if (Validations.isNull(method)){
                throw new DeviceExceptions.InvalidCommandException("ERROR: Erroneous command!");
            }
            Object methodResponse = method.invokeMethod(null,action.values);
            if (!Validations.isNull(methodResponse)){
                responseText += (String) methodResponse;
            }
            return responseText;
        }catch (DeviceExceptions.InitialTimeException e){
            responseText += e.getMessage();
            throw new DeviceExceptions.InitialTimeException(responseText);
        } catch (DeviceExceptions e){
            responseText +=  e.getMessage() + "\n";
            return responseText;
        }

    }

    /**
     * Creates the report of all devices and returns it.
     * @param values  An {@code ArrayList<String>} that contains nothing for executing.
     * @return A {@code String} that is the report of devices.
     * @throws DeviceExceptions If there is an invalid command.
     * @see Utilities
     */
    public static String zReport(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(0,0,values.size());
        ArrayList<Integer> orderedDeviceIDs = DeviceLogger.getOrderedDeviceIDs();
        String reportText = "";
        reportText += "Time is:\t" + Utilities.convertDateToString(time)+"\n";
        for (Integer deviceID : orderedDeviceIDs){
            SmartDevice device = SmartDevice.getDeviceById(deviceID);
            reportText += device.getDeviceInfo() +"\n";
        }
        return reportText;
    }

    /**
     * <p>
     *    Skips forwards the time to the next workload time and
     *    runs all workloads that has this executing time if there is.
     * </p>
     *
     * @param values An {@code ArrayList<String>} that contains nothing for executing.
     * @throws DeviceExceptions It throws exception if there is no workload.
     * @see Workload
     * @see Validations
     */
    public static void nop(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(0,0,values.size());
        Date lowestDate = findMostRecentDate();
        if (Validations.isNull(lowestDate)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: There is nothing to switch!");
        }
        time = lowestDate;
        runPassedWorkloads();

    }

    // Time Methods

    /**
     * Sets the {@link SmartDeviceController#time} for once.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Time value as {@code String}.
     * </p>
     * @return A String that is a success message if success. Otherwise, it returns null.
     * @throws DeviceExceptions If there is illegal argument or
     * {@link SmartDeviceController#time} is setted before, it gives an error.
     * @see Validations
     * @see Utilities
     */
    public static String setInitialTime(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(1,1,values.size());
        String newTimeStr = values.get(0);
        if (!Validations.isNull(time)){
            throw new DeviceExceptions.InvalidCommandException("ERROR: Erroneous command!");
        }

        if (!Validations.isTrueDateFormat(newTimeStr)) {
            throw new DeviceExceptions.InitialTimeException("ERROR: Format of the initial date is wrong! Program is going to terminate!\n");
        }
        time = Utilities.convertStringToDate(newTimeStr);
        return "SUCCESS: Time has been set to "+Utilities.convertDateToString(time)+"!\n";
    }

    /**
     * Sets the {@link SmartDeviceController#time}.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -New time value as {@code String}.
     * </p>
     * @throws DeviceExceptions If the new time is not valid, it gives an error.
     * @see Validations
     * @see Utilities
     */
    public static void setTime(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(1,1,values.size());
        String timeStr = values.get(0);
        if (!Validations.isTrueDateFormat(timeStr)) {
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Time format is not correct!");
        }
        Date newTime = Utilities.convertStringToDate(timeStr);
        if (Validations.isSameTime(newTime,time)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: There is nothing to change!");
        }
        if (Validations.isPassedTime(newTime, time)) {
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Time cannot be reversed!");
        }
        time = newTime;
        runPassedWorkloads();
    }

    /**
     * Skips the minutes.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Minute value as {@code Integer}.
     * </p>
     * @throws DeviceExceptions If the minute is not positive integer, it gives an error.
     * @see Validations
     */
    public static void skipMinutes(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(1,1,values.size());
        String minuteStr = values.get(0);

        if (!Validations.isCanInt(minuteStr)) {
            throw new DeviceExceptions.InvalidCommandException("ERROR: Erroneous command!");
        }
        int minute =Integer.parseInt(minuteStr);

        if(Validations.isNegative(minute)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Time cannot be reversed!");
        }else if (Validations.isZero(minute)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: There is nothing to skip!");
        }

        time = Utilities.addMinutes(time,minute);
        runPassedWorkloads();
    }

    // Smart Device Methods

    /**
     * Creates a {@code SmartDevice} with given values.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Different values for each different {@code SmartDevice}.
     * </p>
     * @throws DeviceExceptions If there is illegal argument, it gives an error.
     */
    public static void add(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkMissingArgs(values,0);
        String deviceType = values.get(0);
        MethodClass method = new MethodClass(deviceType, "create",ArrayList.class);
        if (Validations.isNull(deviceType)){
            throw new DeviceExceptions.InvalidCommandException("ERROR: Erroneous command!");
        }
        ArrayList<String> newValues = new ArrayList<>(values.subList(1, values.size()));
        SmartDevice device = (SmartDevice) method.invokeMethod(null,newValues);

        if (device.status){
            device.setStartTime(time);
        }
        DeviceLogger.addNonOperation(device);

    }

    /**
     * Removes a {@code SmartDevice} with using device name.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> Name of the device as {@code String}.
     * </p>
     * @return A String that is a success message if success.
     * @throws DeviceExceptions If there is no device that name is given name, it gives an error.
     * @see Validations
     */
    public static String remove(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(1,1,values.size());
        String deviceName = values.get(0);
        if (!Validations.isLegalKey(SmartDevice.deviceNames, deviceName)) {
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Erroneous command!");
        }
        SmartDevice device  = SmartDevice.removeDeviceByName(deviceName);
        DeviceLogger.delete(device.getId());
        if (device.status){
            device.off(time);
        }
        String deviceInfo = device.getDeviceInfo();
        if (!Validations.isNull(device.switchTimeWorkloadId)){
            deleteWorkload(device.switchTimeWorkloadId);
        }
        return "SUCCESS: Information about removed smart device is as follows:\n"+deviceInfo+"\n";
    }

    /**
     * Switches the {@link SmartDevice#status} of the {@code SmartDevice} with using device name.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -New status of the device as {@code String} (On/Off)
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * new status is old status of device, it gives an error.
     * @see Validations
     * @see Utilities
     */
    public static void switchStatus(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(2,2,values.size());
        String deviceName = values.get(0);
        String newStatusStr = values.get(1);


        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.validateStatus(newStatusStr);
        Boolean newStatus = Utilities.getStatusFromText(newStatusStr);

        if (newStatus.equals(device.status)) {
            throw new DeviceExceptions.IllegalArgumentException("ERROR: This device is already switched "+ newStatusStr.toLowerCase()+"!");
        }
        device.switchStatus(time, newStatus);

    }

    /**
     * It creates a workload for switch the status of {@code SmartDevice}.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -Switch time of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * switch time is not valid, it gives an error.
     * @see Validations
     * @see Utilities
     */
    public static void setSwitchTime(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(2,2,values.size());
        String deviceName = values.get(0);
        String timeStr = values.get(1);

        Validations.checkDeviceExist(deviceName);

        if (!Validations.isTrueDateFormat(timeStr)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Time format is not correct!");
        }

        Date newTime = Utilities.convertStringToDate(timeStr);

        if (Validations.isPassedTime(newTime, time)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Switch time cannot be in the past!");
        }
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        MethodClass method = new MethodClass(
                device.cls,
                "switchStatus",
                Date.class, boolean.class);

        Object[] workloadParam =new Object[]{newTime,!device.status};

        Workload workload = new Workload(
                device,
                newTime,
                method,
                workloadParam);

        if (!Validations.isNull(device.switchTimeWorkloadId)){
            deleteWorkload(device.switchTimeWorkloadId);
        }

        device.switchTimeWorkloadId = workload.getId();
        addWorkload(workload);
        DeviceLogger.addNonExecuted(device);
        runPassedWorkloads();

    }

    /**
     * Changes the device name to new name that given.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -New name of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * there is a device that name is new name or old name and new name are the same, it gives an error.
     * @see Validations
     */
    public static void changeName(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(2,2,values.size());

        String deviceName = values.get(0);
        String newName  = values.get(1);

        if (deviceName.equals(newName)){
            throw new DeviceExceptions.IllegalArgumentException("ERROR: Both of the names are the same, nothing changed!");
        }

        Validations.checkDeviceExist(deviceName);

        Validations.checkDeviceNonExist(newName);

        SmartDevice.changeDeviceName(deviceName,newName);
    }

    // Smart Lamp Methods

    /**
     * Changes the kelvin value of the {@code SmartLamp} to new kelvin value.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -New kelvin value of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * new kelvin value is not valid, it gives an error.
     * @see Validations
     */
    public static void setKelvin(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(2,2,values.size());

        String deviceName = values.get(0);
        String kelvinStr = values.get(1);

        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.checkSmartLamp(device);
        SmartLamp lamp = (SmartLamp) device;

        Validations.validateKelvin(kelvinStr);
        lamp.setKelvin(Integer.parseInt(kelvinStr));
    }

    /**
     * Changes the brightness value of the {@code SmartLamp} to new brightness value.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -New brightness value of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * new brightness value is not valid, it gives an error.
     */
    public static void setBrightness(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(2,2,values.size());

        String deviceName = values.get(0);
        String brightnessStr = values.get(1);

        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.checkSmartLamp(device);
        SmartLamp lamp = (SmartLamp) device;

        Validations.validateBrightness(brightnessStr);
        lamp.setBrightness(Integer.parseInt(brightnessStr));
    }
    /**
     * Changes the brightness and kelvin values of the {@code SmartLamp} to new values.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -New kelvin value of the device as {@code String}.
     *        <br> -New brightness value of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * new values are not valid, it gives an error.
     * @see SmartLamp
     */
    public static void setWhite(ArrayList<String> values) throws DeviceExceptions{
        Validations.checkRange(3,3,values.size());

        String deviceName = values.get(0);
        String kelvinStr = values.get(1);
        String brightnessStr = values.get(2);

        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.checkSmartLamp(device);
        SmartLamp lamp = (SmartLamp) device;

        Validations.validateKelvin(kelvinStr);
        Validations.validateBrightness(brightnessStr);


        lamp.setWhite(
            Integer.parseInt(kelvinStr),
            Integer.parseInt(brightnessStr)
        );
    }
    /**
     * Changes the color code value of the {@code SmartColorLamp} to new value.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -New color code value of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * new color code value is not valid, it gives an error.
     * @see SmartColorLamp
     */
    public static void setColorCode(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(2,2,values.size());

        String deviceName = values.get(0);
        String colorCode = values.get(1);

        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.checkSmartColorLamp(device);
        SmartColorLamp lamp = (SmartColorLamp) device;

        Validations.validateColorCode(colorCode);
        lamp.setColorCode(colorCode);
    }
    /**
     * Changes the color code and brightness values of the {@code SmartColorLamp} to new values.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -New color code value of the device as {@code String}.
     *        <br> -New brightness value of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * new values are not valid, it gives an error.
     * @see SmartColorLamp
     */
    public static void setColor(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(3,3,values.size());

        String deviceName = values.get(0);
        String colorCode = values.get(1);
        String brightnessStr = values.get(2);

        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.checkSmartColorLamp(device);
        SmartColorLamp lamp = (SmartColorLamp) device;

        Validations.validateColorCode(colorCode);
        Validations.validateBrightness(brightnessStr);
        lamp.setColor(colorCode, Integer.parseInt(brightnessStr));
    }

    // Smart Plug Methods

    /**
     * Plugs in a device to the {@code SmartPlug}.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     *        <br> -Ampere value of the device that plugged in as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * new ampere value is not positive or there is a device that already plugged in
     * to this {code SmartPlug}, it gives an error.
     * @see SmartPlug
     */
    public static void plugIn(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(2,2,values.size());

        String deviceName = values.get(0);
        String ampereStr = values.get(1);

        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.checkSmartPlug(device);
        SmartPlug plug = (SmartPlug) device;

        Validations.validateAmpere(ampereStr);

        if (plug.plug){
            throw new DeviceExceptions.InvalidCommandException("ERROR: There is already an item plugged in to that plug!");
        }
        plug.plugIn(Float.parseFloat(ampereStr), time);
    }

    /**
     * Plugs out the device that is already plugged in to the {@code SmartPlug}.
     * @param values <p>
     *        An {@code ArrayList<String>} that contains:
     *        <br> -Name of the device as {@code String}.
     * </p>
     * @throws DeviceExceptions If there is no device that name is given name or
     * there is not a device that already plugged in
     * to this {code SmartPlug}, it gives an error.
     * @see SmartPlug
     */
    public static void plugOut(ArrayList<String> values) throws DeviceExceptions {
        Validations.checkRange(1,1,values.size());

        String deviceName = values.get(0);

        Validations.checkDeviceExist(deviceName);
        SmartDevice device = SmartDevice.getDeviceByName(deviceName);

        Validations.checkSmartPlug(device);
        SmartPlug plug = (SmartPlug) device;

        if (!plug.plug){
            throw new DeviceExceptions.InvalidCommandException("ERROR: This plug has no item to plug out from that plug!");
        }
        plug.plugOut(time);
    }


    // Workload Methods

    /**
     * Adds a new workload in all workloads lists.
     * @param workload A {@code Workload} object.
     * @see Workload
     */
    private static void addWorkload(Workload workload){
        ArrayList<Workload> dateWorkloads =  workloads.get(workload.time);
        if (Validations.isNull(dateWorkloads)){
            dateWorkloads = new ArrayList<>();
        }
        dateWorkloads.add(workload);
        workloads.put(workload.time, dateWorkloads);
    }

    /**
     * Deletes workloads that id in given ids in all workloads lists.
     * @param workloadIds  An {@code Integer} varargs that represents {@code Workload}'s ids .
     * @see Workload
     */
    private static void deleteWorkload(Integer... workloadIds){
        for (int workloadId: workloadIds){
            Workload workload = Workload.workloads.get(workloadId);
            ArrayList<Workload> dateWorkloads = workloads.get(workload.time);
            dateWorkloads.remove(workload);
            if (dateWorkloads.isEmpty()){
                workloads.remove(workload.time);
            }
            Workload.workloads.remove(workload.getId());
        }
    }

    /**
     * Finds the most recent date in between {@link #workloads} dictionary keys.
     * @return A {@code Date} that represents most recent date.
     * @see Workload
     */
    private static Date findMostRecentDate(){
        Date lowestDate = null;
        Enumeration<Date> dates = workloads.keys();
        while(dates.hasMoreElements()){
            Date date = dates.nextElement();
            if (Validations.isNull(lowestDate)){
                lowestDate = date;
            } else if (date.getTime() < lowestDate.getTime()) {
                lowestDate = date;
            }
        }
        return lowestDate;
    }

    /**
     * Runs workloads that executing times are passed.
     * @see Workload
     * @see #runWorkloads(Date)
     */
    private static void runPassedWorkloads(){
        ArrayList<Date> dates = getPassedDates();
        for(Date date: dates){
            runWorkloads(date);
        }
    }

    /**
     * Gets passed dates according to {@link #time} in {@link #workloads}.
     * @return An {@code Arraylist<Code>} that contains passed dates.
     */
    private static ArrayList<Date> getPassedDates(){
        ArrayList<Date> passedDatesList = new ArrayList<>();
        Enumeration<Date> dates = workloads.keys();
        while(dates.hasMoreElements()){
            Date newDate = dates.nextElement();
            if (time.getTime() >= newDate.getTime()){
                passedDatesList.add(newDate);
            }
        }
        return passedDatesList;
    }

    /**
     * Runs all workloads that executing date is same with given date in {@link #workloads}.
     * @param date A Date object.
     */
    private static void runWorkloads(Date date){
        ArrayList<Workload> executedWorkloads = new ArrayList<>();
        ArrayList<Workload> dateWorkloads = workloads.get(date);
        for (Workload workload: dateWorkloads){
            workload.run();
            workload.device.switchTimeWorkloadId = null;
            executedWorkloads.add(0,workload);
        }
        for (Workload workload: executedWorkloads){
            DeviceLogger.addExecuted(workload.device,0);
            deleteWorkload(workload.getId());
        }
    }
}
