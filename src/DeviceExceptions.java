/**
 * The {@code DeviceExceptions} class is keeps exception types for using SmartDevices .
 *
 * @see SmartDevice
 * @see SmartDeviceController
 */
public class DeviceExceptions extends Exception{

    /**
     * Creates an {@code DeviceException} with an error message.
     * @param message: Error message.
     */
    public DeviceExceptions(String message) {
        super(message);
    }

    /**
     * The {@code InvalidCommandException} class is an error type for use if an invalid command is comes from executing from user.
     */
    public static class InvalidCommandException extends DeviceExceptions{
        public InvalidCommandException(String message) {
            super(message);
        }
    }
    /**
     * The {@code IllegalArgumentException} class is an error type for use if there is illegal arguments that provided from user for executing.
     */
    public static class IllegalArgumentException extends DeviceExceptions{
        public IllegalArgumentException(String message) {
            super(message);
        }
    }

    /**
     * The {@code MissingArgumentException} class is an error type for use if there is missing arguments that provided from user for executing.
     */
    public static class MissingArgumentException extends DeviceExceptions{
        public MissingArgumentException(String message) {
            super(message);
        }
    }

    /**
     * The {@code InitialTimeException} class is an error type for use if the first command form user is not SetInitialTime with legal arguments.
     * The program going to terminate if this error occurs.
     */
    public static  class InitialTimeException extends DeviceExceptions{
        public InitialTimeException(String message) {
            super(message);
        }
    }
}
