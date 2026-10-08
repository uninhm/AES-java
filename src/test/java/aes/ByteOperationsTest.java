package aes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

class ByteOperationsTest {
    Random rnd = new Random();

    /**
     * Test the rotWord method.
     */
    @Test
    void rotWord() {
        byte[] word = {0x01, 0x02, 0x03, 0x04};

        ByteOperations.rotWord(word);

        assertArrayEquals(new byte[] {0x02, 0x03, 0x04, 0x01}, word);
    }

    /**
     * Test the rotBytes method.
     */
    @Test
    void rotBytes() {
        byte[] bytes = {1, 2, 3, 4, 5, 6};

        ByteOperations.rotBytes(bytes, 0, 3, 1, 2);

        assertArrayEquals(
                new byte[] {3, 2, 5, 4, 1, 6},
                bytes
        );
    }

    /**
     * Test the coherence between the subBytes and the subBytesInv methods.
     * i.e. subBytesInv(subBytes(x)) = x
     */
    @Test
    void subBytesAndInverse() {
        byte[] input = new byte[256];
        for (int i = 0; i < 256; ++i)
            input[i] = (byte) i;

        byte[] original = input.clone();

        ByteOperations.subBytes(input);
        ByteOperations.subBytesInv(input);

        assertArrayEquals(original, input);
    }

    /**
     * Test the xtimes method.
     * Notice how subsequent tests will also test xtimes because other functions depend on it.
     */
    @Test
    void xtimes() {
        assertEquals((byte) 0xAE, ByteOperations.xtimes((byte) 0x57));
        assertEquals((byte) 0x1B, ByteOperations.xtimes((byte) 0x80));
    }

    /**
     * Test the plus method.
     */
    @Test
    void plus() {
        byte[] a = {0x0F, 0x00, (byte) 0xFF, 0x10};
        byte[] b = {0x0F, (byte) 0xF0, 0x00, 0x10};

        ByteOperations.plus(a, b);

        assertArrayEquals(new byte[] {0x00, (byte) 0xF0, (byte) 0xFF, 0x00}, a);
    }

    /**
     * Test the plusRange method.
     */
    @Test
    void plusRange() {
        byte[] a = {0x00, (byte) 0xFF, 0x00, 0x00};
        byte[] b = {0x0F, 0x0F};

        ByteOperations.plusRange(a, 1, b);

        assertArrayEquals(new byte[] {0x00, (byte) 0xF0, 0x0F, 0x00}, a);
    }

    /**
     * Test the mult method.
     */
    @Test
    void mult() {
        assertEquals((byte) 0xAE, ByteOperations.mult((byte) 0x57, 2));
        assertEquals((byte) 0xF9, ByteOperations.mult((byte) 0x57, 3));
        assertEquals((byte) 0x47, ByteOperations.mult((byte) 0x57, 4));
        assertEquals((byte) 0xD9, ByteOperations.mult((byte) 0x57, 9));
        assertEquals((byte) 0x77, ByteOperations.mult((byte) 0x57, 11));
        assertEquals((byte) 0x9E, ByteOperations.mult((byte) 0x57, 13));
        assertEquals((byte) 0x67, ByteOperations.mult((byte) 0x57, 14));
    }

    /**
     * Check that 0 * x = 0 and x * 1 = x.
     */
    @Test
    void multByZeroAndOne() {
        for (int i = 0; i < 256; ++i) {
            byte a = (byte) i;
            assertEquals((byte) 0, ByteOperations.mult(a, 0));
            assertEquals(a, ByteOperations.mult(a, 1));
        }
    }

    /**
     * Test the matrix multiplication.
     */
    @Test
    void matrixMult() {
        int[][] M = {
                {2, 3, 1, 1},
                {1, 2, 3, 1},
                {1, 1, 2, 3},
                {3, 1, 1, 2}
        };

        assertArrayEquals(
                new byte[] { 0x08, 0x5b, 0x6d, 0x16 },
                ByteOperations.matrixMult(
                        M,
                        new byte[] { 0x52, 0x6b, 0x67, 0x76 }
                )
        );
    }

    /**
     * Check that M * M^(-1) * x = x and M^(-1) * M * x = x for random x's.
     */
    @Test
    void matrixMultCoherence() {
        final int[][] M = {
                {2, 3, 1, 1},
                {1, 2, 3, 1},
                {1, 1, 2, 3},
                {3, 1, 1, 2}
        };

        final int[][] M_INV = {
                { 14, 11, 13, 9 },
                { 9, 14, 11, 13 },
                { 13, 9, 14, 11 },
                { 11, 13, 9, 14 }
        };

        for (int i = 0; i < 256; ++i) {
            byte[] vec = new byte[4];
            rnd.nextBytes(vec);

            assertArrayEquals(
                    vec,
                    ByteOperations.matrixMult(M, ByteOperations.matrixMult(M_INV, vec))
            );

            assertArrayEquals(
                    vec,
                    ByteOperations.matrixMult(M_INV, ByteOperations.matrixMult(M, vec))
            );
        }
    }
}