import java.util.Arrays;

public class AES {
    private static int BLOCK_SIZE = 128;

    /// Represente l'etat actuel. C'est sur ce tableau qu'on effectue les operations.
    private byte[] state = new byte[BLOCK_SIZE];

    private int keySize;
    private int numberOfIterations;

    /// Corresponds to the index of RC that will be used when generating keys starting with 0;
    /// i.e. RC[0] = 0x01
    private int keyCount = 0;
    private byte[] baseKey, currentKey;

    public AES(int keySize, byte[] key) {
        this.keySize = keySize;
        this.baseKey = key.clone();

        this.setNumberOfIterations();
    }

    public byte[] encrypt(char[] message) {
        this.currentKey = this.baseKey.clone();

        return new byte[0];
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


}
