package aes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

class PaddingPKCS7Test {
    Random rnd = new Random();

    /**
     * Test the padding with manual examples.
     */
    @Test
    void pad() {
        PaddingPKCS7 padding = new PaddingPKCS7();

        assertArrayEquals(
                new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8},
                padding.pad(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, 16)
        );

        assertArrayEquals(
                new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16},
                padding.pad(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}, 16)
        );
    }

    /**
     * Test the unpadding with manual examples.
     */
    @Test
    void unpad() {
        PaddingPKCS7 padding = new PaddingPKCS7();

        assertArrayEquals(
                new byte[] {1, 2, 3, 4},
                padding.unpad(new byte[] {1, 2, 3, 4, 4, 4, 4, 4}, 8)
        );

        assertArrayEquals(
                new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16},
                padding.unpad(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16}, 16)
        );
    }

    /**
     * Check that the padding is coherent with the unpadding.
     * i.e. unpad(pad(x)) = x
     */
    @Test
    void randomCoherenceTest() {
        PaddingPKCS7 padding = new PaddingPKCS7();

        for (int i = 0; i < 100; ++i) {
            byte[] original = new byte[i];
            rnd.nextBytes(original);
            byte[] padded = padding.pad(original, 16);
            byte[] unpadded = padding.unpad(padded, 16);

            assertArrayEquals(original, unpadded);
        }
    }

    /**
     * Check that the length is correct after padding.
     */
    @Test
    void lengthCheck() {
        PaddingPKCS7 padding = new PaddingPKCS7();

        assertEquals(16, padding.pad(new byte[0] , 16).length);
        assertEquals(16, padding.pad(new byte[1] , 16).length);
        assertEquals(16, padding.pad(new byte[15], 16).length);
        assertEquals(32, padding.pad(new byte[16], 16).length);
        assertEquals(32, padding.pad(new byte[17], 16).length);
        assertEquals(32, padding.pad(new byte[31], 16).length);
        assertEquals(48, padding.pad(new byte[32], 16).length);
        assertEquals(48, padding.pad(new byte[33], 16).length);
    }

    /**
     * Check an exception is thrown when the given data is not correctly padded.
     */
    @Test
    void invalidUnpad() {
        PaddingPKCS7 padding = new PaddingPKCS7();

        // Empty array
        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {}, 16)
        );

        // Array of the wrong length
        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {1, 2, 3, 4}, 16)
        );

        // Missing padding
        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, 8)
        );

        // Wrong padding, bigger than the block size
        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {1, 1, 1, 5, 5, 5, 5, 5}, 4)
        );

        // Missing padding. Correct padding cannot end with 0.
        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {1, 2, 3, 4, 5, 6, 7, 0}, 8)
        );
    }
}