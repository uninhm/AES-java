package aes;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PaddingPKCS7Test {
    Random rnd = new Random();

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

    @Test
    void invalidUnpad() {
        PaddingPKCS7 padding = new PaddingPKCS7();

        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {}, 16)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {1, 2, 3, 4}, 16)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {1, 2, 3, 4, 5, 6, 7, 8}, 8)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> padding.unpad(new byte[] {1, 2, 3, 4, 5, 6, 7, 9}, 8)
        );
    }
}