import java.util.*;

/**
 * The {@code Workload} class holds a transaction for a {@code SmartDevice} and when that transaction will be performed.
 * To do it later.
 * @see SmartDevice
 */
public class Workload {
    private static int idCounter = 0;
    /**
     * Non duplicated id for keep tracking.
     */
    private final int id;
    /**
     * The device of the workload.
     * @see SmartDevice
     */
    public SmartDevice device;
    /**
     * Shows when the workload will execute.
     */
    public Date time;
    /**
     * Transaction of the workload.
     * @see MethodClass
     */
    public MethodClass method;
    /**
     * Parameters array of the method for invoke it.
     */
    public Object[] params;
    /**
     * Contains workloads according to their ids.
     * <br>
     * <i>{@code Key -}</i> An {@code Integer} that represents workload's id.
     * <br>
     *<i>{@code Value -}</i> A {@code Workload} that id is same with id that is key.
     */
    public static Dictionary<Integer,Workload> workloads = new Hashtable<>();

    /**
     * Creates a Workload with values.
     * @param device A {@code SmartDevice}.
     * @param time A {@code Date} that represents when workload will execute.
     * @param method A {@code MethodClass} that represents which transaction will execute.
     * @param params A {@code Object...} that represents parameters of the method that will execute.
     */
    public Workload(SmartDevice device, Date time, MethodClass method,Object... params) {
        this.device = device;
        this.time = time;
        this.method = method;
        this.params = Arrays.stream(params).toArray();
        this.id = idCounter;
        workloads.put(this.id,this);
        idCounter++;
    }

    /**
     * Gets Workload's id
     * @return {@link #id}
     */
    public int getId() {
        return id;
    }

    /**
     * Executes the transaction.
     * @return A {@code Object} that represents return value of the method that invoked.
     */
    public Object run(){
        try {
            Object result =  this.method.invokeMethod(this.device, this.params);
            return result;
        } catch (DeviceExceptions e) {
        }
        return null;
    }

    /**
     * Gets Workloads according to their ids.
     * @param workloadIDs An {@code Integer...} varargs that represents Workloads ids.
     * @return An {@code ArrayList<Workload>} that contains Workloads.
     */
    public static ArrayList<Workload> getWorkloads(Integer... workloadIDs){
        ArrayList<Workload> foundedWorkloads = new ArrayList<>();
        for (int id: workloadIDs){
            foundedWorkloads.add(getWorkload(id));
        }
        return foundedWorkloads;
    }

    /**
     * Gets Workload according to their id.
     * @param workloadID An {@code Integer}  that represents Workload's id.
     * @return A {@code Workload} that founded.
     */
    public static Workload getWorkload(Integer workloadID){
        return workloads.get(workloadID);
    }
}
