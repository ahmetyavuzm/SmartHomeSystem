import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The {@code Utilities} class contains the commonly used methods.
 */
public class Utilities {
    /**
     * Converts a {@code String} object to a {@code Date} object.
     * @param str A {@code String} object that suits a specific format.
     * @return A {@code Date} object.
     */
    public static Date convertStringToDate(String str){
        SimpleDateFormat formatter = new SimpleDateFormat("y-M-d_H:m:s");
        Date date = null;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * Converts A {@code Date} object to a {@code String} object in specific format.
     * @param date A {@code Date} object.
     * @return A {@code String} in specific format.
     */
    public static String convertDateToString(Date date){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String dateStr = formatter.format(date);
        return dateStr;
    }

    /**
     * Gets an index for a {@code SmartDevice} according to its
     * workload's time in a sorted {@code SmartDevices} list.
     * @param devices An {@code ArrayList<SmartDevice>} that is contains ordered {@code SmartDevices}
     * @param device A {@code SmartDevice} object that has a workload.
     * @return An {@code Integer} that represent an index.
     * @see Workload
     * @see SmartDevice
     */
    public static Integer getDeviceIndexBySwitchTime(ArrayList<SmartDevice> devices,SmartDevice device){
        int length = devices.size();
        if(length == 0) {
            return 0;
        }

        int half = length/2;
        SmartDevice halfDevice = devices.get(half);
        Date halfSwitchTime = Workload.getWorkload(halfDevice.switchTimeWorkloadId).time;
        Date deviceSwitchTime = Workload.getWorkload(device.switchTimeWorkloadId).time;

        if (length == 1){
            return halfSwitchTime.getTime() > deviceSwitchTime.getTime() ? 0:1;
        }else if (halfSwitchTime.getTime() > deviceSwitchTime.getTime() ){
            ArrayList<SmartDevice> newDevices = new ArrayList<>(devices.subList(0,half));
            return getDeviceIndexBySwitchTime(newDevices,device);
        }else{
            ArrayList<SmartDevice> newDevices = new ArrayList<>(devices.subList(half,length));
            return half + getDeviceIndexBySwitchTime(newDevices,device);
        }
    }

    /**
     * Converts a {@code String} value to a {@code Boolean}.
     * @param str A String object. ("On" or "Off")
     * @return A {@code Boolean} that represents a {@code SmartDevice}'s status property.
     * @see SmartDevice#status
     */
    public static Boolean getStatusFromText(String str){
        return str.equals("On")? true: str.equals("Off")?false:null;
    }
    /**
     * Converts a {@code Boolean} value to a {@code String}.
     * @param status A {@code Boolean} that represents a {@code SmartDevice}'s status property.
     * @return A String object. ("On" or "Off")
     * @see SmartDevice#status
     */
    public static String convertStatusToString(boolean status){
        return status?"On":"Off";
    }

    /**
     * Adds minute value to a {@code Date} object and returns new {@code Date}.
     * @param date A {@code Date} object.
     * @param minutes An positive {@code Integer}.
     * @return A {@code Date} that minutes added.
     */
    public static Date addMinutes(Date date, int minutes){
        return new Date(date.getTime() + (long) 1000*60*minutes);
    }

    /**
     * Converts a millisecond to minute.
     * @param millisecond A {@code long} value.
     * @return A {@code float} value.
     */
    public static float millisecondToMinute(long millisecond){
        return ((float) millisecond/60000);
    }
    /**
     * Converts a millisecond to hour.
     * @param millisecond A {@code long} value.
     * @return A {@code float} value.
     */
    public static Float millisecondToHour(long millisecond){
        return Float.valueOf((float) millisecond/3600000);
    }
}
