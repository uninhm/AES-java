package aes;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * AES implementation.
 */
public class AES {
    /** The size of the block in bytes */
    public static final int BLOCK_SIZE = 16;

    /// Number of rows of the matrix where we (theoretically) arrange the bytes
    /// of the state.
    private static final int ROWS = 4;

    /// Number of columns of the matrix where we (theoretically) arrange the bytes
    /// of the state.
    private static final int COLUMNS = 4;

    /// This array contains the block to be encrypted or decrypted
    /// It will be modified inplace by each operation
    private final byte[] state = new byte[BLOCK_SIZE];

    /// Base key size in bits
    private final int keySize;

    /// Number of the current round
    private int round;

    private final KeyGenerator keyGenerator;

    /** Construct an AES object with the corresponding key.
     * The array must be of length 16, 24 or 32 for AES 128, 192 and 256 respectively.
     * @param key an array containing the bytes of the key
     */
    public AES(byte[] key) {
        this.keySize = key.length * 8;
        this.keyGenerator = new KeyGenerator(key);
    }

    /** Construct an AES object with the corresponding key.
     * The number of bytes in the string must be 16, 24 or 32 for AES 128, 192 and 256 respectively.
     * @param key the hexadecimal representation of the key
     */
    public AES(String key) {
        this(HexFormat.of().parseHex(key));
    }

    /** Encrypt a message encoded with UTF_8.
     * @param message the message string
     * @return the encrypted message as an array of bytes
     */
    public byte[] encrypt(String message) {
        return encrypt(message.getBytes(StandardCharsets.UTF_8));
    }

    /** Encrypt an array of bytes.
     * If the length isn't a multiple of 16, 0s will be added as padding at the end.
     * @param message the message as an array of bytes
     * @return the encrypted message as an array of bytes
     */
    public byte[] encrypt(byte[] message) {
        // The resulting length is the next multiple of 16 after the message length
        byte[] result = new byte[(message.length+15)/16*16];

        for (int i = 0; i < message.length; i += BLOCK_SIZE) {
            // Copy the resulting block to the result
            System.arraycopy(this.encryptBlock(message, i), 0, result, i, BLOCK_SIZE);
        }

        return result;
    }

    /** Decrypt an array of bytes.
     * The length must be a multiple of 16.
     * @param cypher the cyphertext
     * @return the decrypted message as an array of bytes
     */
    public byte[] decrypt(byte[] cypher) {
        byte[] result = new byte[cypher.length];

        for (int i = 0; i < cypher.length; i += BLOCK_SIZE) {
            // Copy the resulting block to the result
            System.arraycopy(this.decryptBlock(cypher, i), 0, result, i, BLOCK_SIZE);
        }

        return result;
    }

    /// Encrypt a block given as an array of bytes. If there aren't
    /// enough bytes to make a block of 16, 0s will be added to the right.
    /// @param bytes the message byte array
    /// @param start the index where the block starts
    private byte[] encryptBlock(byte[] bytes, int start) {
        // If there isn't enough bytes use as much as available
        int actualLength = Math.min(bytes.length - start, BLOCK_SIZE);

        // Copy the block to the state
        System.arraycopy(bytes, start, this.state, 0, actualLength);

        // Add padding when needed
        for (int i = actualLength; i < BLOCK_SIZE; ++i)
            state[i] = 0;

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

    /// Decrypt the block starting at 'start' given an array of bytes.
    /// The length of the block will be exactly 16 bytes.
    /// @param bytes a byte array
    /// @param start the index where the block starts
    private byte[] decryptBlock(byte[] bytes, int start) {
        System.arraycopy(bytes, start, state, 0, BLOCK_SIZE);

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

    /// Returns the number of iterations according to the key size.
    /// If the key size is different of 128, 192 and 256 it throws an exception.
    private int getNumberOfIterations() {
        return switch (this.keySize) {
            case 128 -> 10;
            case 192 -> 12;
            case 256 -> 14;
            default -> throw new InvalidKeySizeException(this.keySize);
        };
    }

    /// AddRoundKey inplace operation
    private void addRoundKey() {
        ByteOperations.plus(this.state, this.keyGenerator.getRoundKey(round));
    }

    /// SubBytes inplace operation
    private void subBytes() {
        ByteOperations.subBytes(this.state);
    }

    /// SubBytes inverse inplace operation
    private void subBytesInv() {
        ByteOperations.subBytesInv(this.state);
    }

    /// ShiftRows inplace operation
    private void shiftRows() {
        for (int j = 1; j < ROWS; ++j) {
            ByteOperations.rotBytes(this.state, j, 4, j, 4);
        }
    }

    /// ShiftRows inverse inplace operation
    private void shiftRowsInv() {
        for (int j = 1; j < ROWS; ++j) {
            ByteOperations.rotBytes(this.state, j, 4, 4-j, 4);
        }
    }

    /// MixColumns inplace operation
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

    /// MixColumns inverse inplace operation
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
