import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

public class AES {
    private static int BLOCK_SIZE = 16;

    /// Represente l'etat actuel. C'est sur ce tableau qu'on effectue les operations.
    private byte[] state = new byte[BLOCK_SIZE];
    private static int ROWS = 4;
    private static int COLUMNS = 4;

    private int keySize;

    private int round;
    private byte[] baseKey;
    private KeyGenerator keyGenerator;

    public AES(byte[] key) {
        this.keySize = key.length * 8;
        this.baseKey = key.clone();

        this.keyGenerator = new KeyGenerator(this.baseKey);
    }

    public AES(String key) {
        this(HexFormat.of().parseHex(key));
    }

    public byte[] encrypt(String message) {
        return encrypt(message.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] encrypt(byte[] message) {
        Arrays.fill(this.state, (byte) 0);
        System.arraycopy(message, 0, state, 0, Math.min(state.length, message.length));

        round = 0;
        this.addRoundKey();
        round++;

        for (; round <= this.getNumberOfIterations()-1; ++round) {
            this.subBytes();
            this.shiftRows();
            this.mixColumns();
            this.addRoundKey();
        }

        this.subBytes();
        this.shiftRows();
        this.addRoundKey();

        return state.clone();
    }

    private int getNumberOfIterations() {
        switch (this.keySize) {
            case 128:
                return 10;
            case 192:
                return 12;
            case 256:
                return 14;
            default:
                throw new InvalidKeySizeException(this.keySize);
        }
    }

    private void addRoundKey() {
        ByteOperations.plus(this.state, this.keyGenerator.getRoundKey(round));
    }

    private void subBytes() {
        ByteOperations.subBytes(this.state);
    }

    private void shiftRows() {
        for (int j = 1; j < ROWS; ++j) {
            ByteOperations.rotBytes(this.state, j, 4, j, 4);
        }
    }

    private void mixColumns() {
        int[][] M = {
                {2, 3, 1, 1},
                {1, 2, 3, 1},
                {1, 1, 2, 3},
                {3, 1, 1, 2}
        };

        for (int i = 0; i < COLUMNS; ++i) {
            // Copy the column to a vector called col
            byte[] col = new byte[ROWS];
            for (int j = 0; j < ROWS; ++j)
                col[j] = state[i*ROWS + j];

            // Calculate the matrix-vector product
            byte[] res = ByteOperations.matrixMult(M, col);

            // Copy the result back to the column
            for (int j = 0; j < ROWS; ++j)
                state[i*ROWS + j] = res[j];
        }
    }
}
