import java.util.Arrays;
import java.util.HexFormat;

public class KeyGenerator {
    public static int[] RC =
            { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36 };


    // This is the maximum when generating 15 round keys on AES-256
    // Because we generate 32 bytes each time (one key length) it gets rounded to 256.
    private static int MAX_GENERATED_BYTES = 256;
    private final byte[] words = new byte[MAX_GENERATED_BYTES];
    private int wordsLength;
    private int keyCount = 0;

    /// Key length in bytes
    private final int keyLength;

    /// Construct a key generator using 'key' as the base key.
    /// @param key byte array representation of the key
    public KeyGenerator(byte[] key) {
        switch (key.length) {
            case 16:
            case 24:
            case 32:
                break;
            default:
                throw new InvalidKeySizeException(key.length*8);
        }

        System.arraycopy(key, 0, this.words, 0, key.length);
        this.wordsLength = key.length;
        this.keyLength = key.length;
    }

    /// Construct a key generator from a hexadecimal representation of the key.
    /// @param key hexadecimal representation of the key
    public KeyGenerator(String key) {
        this(HexFormat.of().parseHex(key));
    }

    /// This method generates the next round of words (i.e. of the same length as the base key).
    /// For example, if using a 192-bit key, 6 words (24 bytes) will be generated.
    private void nextKey() {
        // This is the last word of the current key, meaning the last 4 bytes
        // It will be updated when new words are generated during the execution of this method.
        // This always corresponds to the last generated word
        byte[] lastWord = Arrays.copyOfRange(words, wordsLength - ByteOperations.WORD_SIZE, wordsLength);

        // We copy the last key and add it to the end of our word array
        // [key1, key2, key3, 0, 0...] becomes [key1, key2, key3, key3, 0, 0...]
        // where each keyN represents multiple bytes, of course.
        System.arraycopy(words, wordsLength - keyLength, words, wordsLength, keyLength);
        // We don't update the wordsLength yet because these aren't the final generated words

        for (int i = 0; i * ByteOperations.WORD_SIZE < keyLength; ++i) {
            // This corresponds to the document's i mod N_k = 0
            if (i == 0) {
                ByteOperations.rotWord(lastWord);
                ByteOperations.subBytes(lastWord);

                // Corresponds to Rcon[i/N_k] because keyCount = i/N_k - 1 and RC is 0-indexed
                byte[] rcon = { (byte) RC[keyCount], 0, 0, 0 };

                ByteOperations.plus(lastWord, rcon);
            } else if (keyLength == 32 && i == 4) { // Only for AES-256
                ByteOperations.subBytes(lastWord);
            }

            // We add lastWord (corresponding to temp in the document) to the corresponding word (w_i-N_k) in the key.
            // which we conveniently already copied.
            ByteOperations.plusRange(this.words, wordsLength, lastWord);
            wordsLength += ByteOperations.WORD_SIZE; // We update because we added a new word

            // We update lastWord to the last generated word
            lastWord = Arrays.copyOfRange(words, wordsLength - ByteOperations.WORD_SIZE, wordsLength);
        }

        keyCount++;
    }

    public byte[] getRoundKey(int i) {
        // Each round key corresponds to 16 bytes or equivalently 4 words.
        int ROUND_KEY_LENGTH = 16;

        // If we haven't generated that part already
        while (wordsLength < ROUND_KEY_LENGTH*(i+1))
            nextKey();

        return Arrays.copyOfRange(this.words, ROUND_KEY_LENGTH*i, ROUND_KEY_LENGTH*(i+1));
    }
}
