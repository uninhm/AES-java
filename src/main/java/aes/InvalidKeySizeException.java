package aes;

/** Exception thrown when the key size is not valid. */
public class InvalidKeySizeException extends RuntimeException {
    /** Construct an exception with the message passed as a parameter.
     * @param message the error message
     */
    public InvalidKeySizeException(String message) {
        super(message);
    }

    /** Construct an exception with the message 'Invalid key size : {keySize}'.
     * Where {keySize} is replaced by the corresponding parameter.
     * @param keySize The size of the key to be displayed in the message
     */
    public InvalidKeySizeException(int keySize) {
        this(String.format("Invalid key size: %d", keySize));
    }
}
