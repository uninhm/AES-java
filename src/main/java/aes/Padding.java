package aes;

/**
 * Abstract class for padding modes.
 */
public abstract class Padding {
    /** Pad the data to a multiple of the block size. */
    public abstract byte[] pad(byte[] data, int blockSize);

    /** Remove the padding from the data. */
    public abstract byte[] unpad(byte[] data);
}
