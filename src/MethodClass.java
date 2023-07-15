import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 *   The {@code MethodClass} class contains a method
 *   with parameters and necessary methods to get a specific method for invoke it.
 * </p>
 */
public class MethodClass {
    /**
     * Class of the {@link MethodClass#method}.
     */
    private final Class<?> cls;
    /**
     * A Method object.
     * @see java.lang.reflect.Method
     */
    private final Method method;
    /**
     * A Class array contains each parameter of the {@link MethodClass#method}.
     */
    private final Class<?>[] paramTypes;

    /**
     * Finds the specified method with parameters and Creates a MethodClass object.
     * @param cls Class of the method ({@code Class})
     * @param methodName Name of the method. ({@code String})
     * @param paramTypes Classes of the parameters of method. ({@code Class...})
     * @see MethodClass#findMethod(Class, String, Class[])
     */
    public MethodClass(Class cls, String methodName, Class<?>... paramTypes) {
        this.cls = cls;
        this.paramTypes = paramTypes;
        this.method = findMethod(this.cls,methodName, this.paramTypes);
    }
    /**
     * Finds the specified method with parameters and Creates a MethodClass object.
     * @param className Class name of the method  ({@code String})
     * @param methodName Name of the method. ({@code String})
     * @param paramTypes Classes of the parameters of method. ({@code Class...})
     * @see MethodClass#findMethod(Class, String, Class[])
     * @see MethodClass#findClass(String)
     */
    public MethodClass(String className, String methodName, Class<?>... paramTypes) {
        this.cls = findClass(className);
        this.paramTypes = paramTypes;
        this.method = findMethod(this.cls,methodName, this.paramTypes);
    }

    /**
     * Returns class({@link MethodClass#cls}) of the method.
     * @return A Class that represents the class of the method.
     */
    public Class<?> getCls() {
        return cls;
    }

    /**
     * Returns the {@link MethodClass#method}.
     * @return A Class that represents the class of the method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Returns a Class array that contains parameter's classes of the method.
     * @return A Class array that represents classes of parameters of the method
     */
    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    /**
     * Invokes the {@link MethodClass#method} with parameters.
     * @param obj Instance of method class.
     * @param params Method parameters.
     * @return Method
     * @throws DeviceExceptions When the error occur while invoking the method, It throws {@code DeviceException}
     *
     */
    public Object invokeMethod(Object obj,Object... params) throws DeviceExceptions {
        try {
            return this.method.invoke(obj,params);
        } catch (InvocationTargetException e) {
            try{
                throw e.getCause();
            }catch (DeviceExceptions err){
                throw err;
            } catch (Throwable err) {
            }
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    /**
     * Finds Class with specified class name.
     * @param className A string represents class name.
     * @return The Class that suits to information of given.
     */
    private static Class<?> findClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    /**
     * Finds the method according to method information.
     * @param cls Class of the method.
     * @param methodName Name of the method
     * @param paramTypes Class array of method parameters.
     * @return The Method that suits to information of given.
     */
    private static Method findMethod(Class<?> cls, String methodName, Class<?>... paramTypes){
        try {
            return cls.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
        }
        return null;
    }
}
