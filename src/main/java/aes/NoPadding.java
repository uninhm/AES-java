package aes;

/**
 * This padding doesn't change the length of the message.
 * It throws an exception if the message is not a multiple of the block size.
 */
public class NoPadding extends Padding {
    /**
     * Returns a copy of the data.
     * It throws an exception if the array length is not a multiple of the block size.
     * @param data the data to be padded
     * @return a copy of the data
     */
    @Override
    public byte[] pad(byte[] data, int blockSize) {
        if (data.length % blockSize != 0)
            throw new IllegalArgumentException("The length of the message must be a multiple of the block size");

        return data.clone();
    }

    /**
     * Returns a copy of the data.
     * It throws an exception if the array length is not a multiple of the block size.
     * @param data the data to be unpadded
     * @param blockSize the block size
     * @return a copy of the data
     */
    @Override
    public byte[] unpad(byte[] data, int blockSize) {
        if (data.length % blockSize != 0)
            throw new IllegalArgumentException("The length of the message must be a multiple of the block size");

        return data.clone();
    }
}
