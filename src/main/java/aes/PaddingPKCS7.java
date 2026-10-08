package aes;

import java.util.Arrays;

/**
 * Implementation of the PKCS7 padding scheme.
 */
public class PaddingPKCS7 extends Padding {
    /**
     * Pad the data to match the multiple of blockSize strictly after the length
     * using the PKCS7 padding.
     * @param data the data to be padded
     * @param blockSize the block size
     * @return a new array containing the padded data
     */
    @Override
    public byte[] pad(byte[] data, int blockSize) {
        // The resulting length is the multiple of the block size strictly after the message length
        byte[] result = new byte[(data.length/blockSize+1)*blockSize];

        // Copy the bytes to the result
        System.arraycopy(data, 0, result, 0, data.length);

        // Add the padding as specified by PKCS7
        Arrays.fill(result, data.length, result.length, (byte) (result.length-data.length));

        return result;
    }

    /**
     * Unpad a message that was padded using the PKCS7 padding.
     * If the data is not correctly padded, an IllegalArgumentException is thrown.
     * @param data the padded data
     * @return a new array containing the unpadded data
     */
    @Override
    public byte[] unpad(byte[] data, int blockSize) {
        if (data.length == 0
                || data.length%blockSize != 0
                || data[data.length-1] > blockSize
                || data[data.length-1] == 0)
            throw new IllegalArgumentException("The data is not padded correctly");

        int originalLength = data.length-data[data.length-1];

        // Check that all the bytes from the padding are equal to the length of the padding
        for (int i = originalLength; i < data.length-1; ++i)
            if (data[i] != data[data.length-1])
                throw new IllegalArgumentException("The data is not padded correctly");

        return Arrays.copyOfRange(data, 0, originalLength);
    }
}
