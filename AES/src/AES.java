import java.util.Arrays;

public class AES {
    public static int BLOCK_SIZE = 128;

    /// Represente l'etat actuel. C'est sur ce tableau qu'on effectue les operations.
    private byte[] state = new byte[BLOCK_SIZE];

    private int keySize;
    private byte[] baseKey, currentKey;

    private int numberOfIterations;

    public AES(int keySize) {
        this.keySize = keySize;

        this.setNumberOfIterations();
    }

    public byte[] encrypt(char[] message, byte[] key) {
        this.baseKey = key;
        this.currentKey = key.clone();
    }

    private void setNumberOfIterations() {
        switch (this.keySize) {
            case 128:
                this.numberOfIterations = 10;
                break;
            case 192:
                this.numberOfIterations = 12;
                break;
            case 256:
                this.numberOfIterations = 14;
                break;
            default:
                throw new InvalidKeySizeException(this.keySize);
        }
    }

    private void nextKey() {
        byte[] newKey = new byte[keySize];

        // This is the last word of the current key, meaning the last 4 bytes
        // It will be updated when new words are generated during the execution of this method.
        byte[] lastWord = Arrays.copyOfRange(currentKey, currentKey.length - 4, currentKey.length);

        for (int i = 0; 32*i < keySize; ++i) {
            continue; // TODO: Implement
        }
    }
}
