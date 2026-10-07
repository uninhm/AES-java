package aes;

import java.util.Arrays;

public class PaddingPKCS7 extends Padding {
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

    @Override
    public byte[] unpad(byte[] data) {
        // Since the last byte is the padding length, we can simply remove it
        return Arrays.copyOfRange(data, 0, data.length-data[data.length-1]);
    }
}
