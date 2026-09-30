public class InvalidKeySizeException extends RuntimeException {
    /// Construct an exception with the message passed as a parameter.
    /// @param message
    public InvalidKeySizeException(String message) {
        super(message);
    }

    /// Construct an exception with the message 'Invalid key size : {keySize}'.
    /// Where {keySize} is replaced by the corresponding parameter.
    /// @param keySize
    public InvalidKeySizeException(int keySize) {
        super(String.format("Invalid key size: %d", keySize));
    }
}
