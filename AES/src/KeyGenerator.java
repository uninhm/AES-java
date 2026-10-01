import java.util.Arrays;
import java.util.HexFormat;

public class KeyGenerator {
    public static int[] RC =
            { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36 };


    private final byte[] currentKey;
    private int keyCount = 0;

    /// Construct a key generator using 'key' as the base key.
    /// @param key byte array representation of the key
    public KeyGenerator(byte[] key) {
        this.currentKey = key.clone();
    }

    /// Construct a key generator from a hexadecimal representation of the key.
    /// @param key hexadecimal representation of the key
    public KeyGenerator(String key) {
        this.currentKey = HexFormat.of().parseHex(key);
    }

    public byte[] nextKey() {
        // This is the last word of the current key, meaning the last 4 bytes
        // It will be updated when new words are generated during the execution of this method.
        // This always corresponds to the last generated word
        byte[] lastWord = Arrays.copyOfRange(currentKey, currentKey.length - ByteOperations.WORD_SIZE, currentKey.length);

        for (int i = 0; i * ByteOperations.WORD_SIZE < this.currentKey.length; ++i) {
            // This corresponds to the document's i mod N_k = 0
            if (i == 0) {
                ByteOperations.rotWord(lastWord);
                ByteOperations.subBytes(lastWord);

                // Corresponds to Rcon[i/N_k] because keyCount = i/N_k - 1 and RC is 0-indexed
                byte[] rcon = { (byte) RC[keyCount], 0, 0, 0 };

                ByteOperations.plus(lastWord, rcon);
            }

            // We add lastWord (corresponding to temp in the document) to the corresponding word (w_i-N_k) in the key.
            // The key can be modified inplace because the old word won't be used again.
            ByteOperations.plusRange(this.currentKey, ByteOperations.WORD_SIZE*i, lastWord);

            // We update lastWord to the last generated word
            lastWord = Arrays.copyOfRange(currentKey, ByteOperations.WORD_SIZE*i, ByteOperations.WORD_SIZE*(i+1));
        }

        keyCount++;

        return currentKey.clone();
    }
}
