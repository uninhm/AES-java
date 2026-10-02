import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

public class AES {
    public static int BLOCK_SIZE = 16;

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

    public byte[] decrypt(byte[] cypher) {
        Arrays.fill(this.state, (byte) 0);
        System.arraycopy(cypher, 0, state, 0, Math.min(state.length, cypher.length));

        round = this.getNumberOfIterations();

        this.addRoundKey();
        this.shiftRowsInv();
        this.subBytesInv();

        round--;

        for (; round > 0; --round) {
            this.addRoundKey();
            this.mixColumnsInv();
            this.shiftRowsInv();
            this.subBytesInv();
        }

        this.addRoundKey();

        return this.state.clone();
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

    private void subBytesInv() {
        ByteOperations.subBytesInv(this.state);
    }

    private void shiftRows() {
        for (int j = 1; j < ROWS; ++j) {
            ByteOperations.rotBytes(this.state, j, 4, j, 4);
        }
    }

    private void shiftRowsInv() {
        for (int j = 1; j < ROWS; ++j) {
            ByteOperations.rotBytes(this.state, j, 4, 4-j, 4);
        }
    }

    private void mixColumns() {
        final int[][] M = {
                {2, 3, 1, 1},
                {1, 2, 3, 1},
                {1, 1, 2, 3},
                {3, 1, 1, 2}
        };

        for (int i = 0; i < COLUMNS; ++i) {
            // Copy the column to a vector called col
            byte[] col = new byte[ROWS];
            System.arraycopy(state, i*ROWS, col, 0, ROWS);

            // Calculate the matrix-vector product
            byte[] res = ByteOperations.matrixMult(M, col);

            // Copy the result back to the column
            System.arraycopy(res, 0, state, i*ROWS, ROWS);
        }
    }

    private void mixColumnsInv() {
        final int[][] M_INV = {
                { 14, 11, 13, 9 },
                { 9, 14, 11, 13 },
                { 13, 9, 14, 11 },
                { 11, 13, 9, 14 }
        };

        for (int i = 0; i < COLUMNS; ++i) {
            // Copy the column to a vector called col
            byte[] col = new byte[ROWS];
            System.arraycopy(state, i*ROWS, col, 0, ROWS);

            // Calculate the matrix-vector product
            byte[] res = ByteOperations.matrixMult(M_INV, col);

            // Copy the result back to the column
            System.arraycopy(res, 0, state, i*ROWS, ROWS);
        }
    }
}
