package aes;

import java.util.Arrays;

/**
 * Zero padding. This adds zeros to the end of the message up to the next multiple of the block size.
 * Notice how this is not reversible if the message had trailing zeros.
 * That is why it is deprecated.
 */
@Deprecated
public class PaddingZeros extends Padding {
    @Override
    public byte[] pad(byte[] data, int blockSize) {
        // The resulting length is the multiple of the block size after (or equal to) the message length
        byte[] result = new byte[(data.length+blockSize-1)/blockSize*blockSize];

        // Copy the bytes to the result
        System.arraycopy(data, 0, result, 0, data.length);

        // Notice that java arrays are automatically filled with zeros

        return result;
    }

    @Override
    public byte[] unpad(byte[] data, int blockSize) {
        if (data.length % blockSize != 0)
            throw new IllegalArgumentException("The data is not padded correctly");

        int actualLength = data.length;

        while (data[actualLength-1] == 0) {
            actualLength--;
        }

        return Arrays.copyOfRange(data, 0, actualLength);
    }
}
