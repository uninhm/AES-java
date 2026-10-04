package aes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteOperationsTest {

    @Test
    void rotWord() {
        byte[] word = {0x01, 0x02, 0x03, 0x04};

        ByteOperations.rotWord(word);

        assertArrayEquals(new byte[] {0x02, 0x03, 0x04, 0x01}, word);
    }

    @Test
    void rotBytes() {
        byte[] bytes = {1, 2, 3, 4, 5, 6};

        ByteOperations.rotBytes(bytes, 0, 3, 1, 2);

        assertArrayEquals(
                new byte[] {3, 2, 5, 4, 1, 6},
                bytes
        );
    }

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

    @Test
    void xtimes() {
        Assertions.assertEquals((byte) 0xAE, ByteOperations.xtimes((byte) 0x57));
        Assertions.assertEquals((byte) 0x1B, ByteOperations.xtimes((byte) 0x80));
    }

    @Test
    void plus() {
        byte[] a = {0x0F, 0x00, (byte) 0xFF, 0x10};
        byte[] b = {0x0F, (byte) 0xF0, 0x00, 0x10};

        ByteOperations.plus(a, b);

        assertArrayEquals(new byte[] {0x00, (byte) 0xF0, (byte) 0xFF, 0x00}, a);
    }

    @Test
    void plusRange() {
        byte[] a = {0x00, (byte) 0xFF, 0x00, 0x00};
        byte[] b = {0x0F, 0x0F};

        ByteOperations.plusRange(a, 1, b);

        assertArrayEquals(new byte[] {0x00, (byte) 0xF0, 0x0F, 0x00}, a);
    }

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

    @Test
    void multByZeroAndOne() {
        for (int i = 0; i < 256; ++i) {
            byte a = (byte) i;
            assertEquals((byte) 0, ByteOperations.mult(a, 0));
            assertEquals(a, ByteOperations.mult(a, 1));
        }
    }

    @Test
    void matrixMult() {
        int[][] M = {
                {2, 3, 1, 1},
                {1, 2, 3, 1},
                {1, 1, 2, 3},
                {3, 1, 1, 2}
        };

        Assertions.assertArrayEquals(
                new byte[] { 0x08, 0x5b, 0x6d, 0x16 },
                ByteOperations.matrixMult(
                        M,
                        new byte[] { 0x52, 0x6b, 0x67, 0x76 }
                )
        );
    }
}