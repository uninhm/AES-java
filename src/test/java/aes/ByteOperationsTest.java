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
    void subBytes() {
        byte[] input = {0x00, 0x01, (byte) 0x80, (byte) 0xFF};

        ByteOperations.subBytes(input);

        assertArrayEquals(new byte[] {(byte) 0x63, (byte) 0x7C, (byte) 0xCD, (byte) 0x16}, input);
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