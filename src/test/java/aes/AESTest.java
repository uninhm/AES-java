package aes;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Random;

class AESTest {
    final Random rnd = new Random();

    @Test
    void encrypt128() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f", new NoPadding());

        assertArrayEquals(
                HexFormat.of().parseHex("3b61ec0f4acd3b5eb2425bdc221a0949"),
                aes.encrypt("Hola\0\0\0\0\0\0\0\0\0\0\0\0")
        );

        assertArrayEquals(
                HexFormat.of().parseHex("69c4e0d86a7b0430d8cdb78070b4c55a"),
                aes.encrypt(HexFormat.of().parseHex("00112233445566778899aabbccddeeff"))
        );
    }

    @Test
    void encrypt192() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f1011121314151617", new NoPadding());

        assertArrayEquals(
                HexFormat.of().parseHex("a875c99f5ea3af568786a5d1193872c8"),
                aes.encrypt("Hola como estas ")
        );

        assertArrayEquals(
                HexFormat.of().parseHex("dda97ca4864cdfe06eaf70a0ec0d7191"),
                aes.encrypt(HexFormat.of().parseHex("00112233445566778899aabbccddeeff"))
        );
    }

    @Test
    void encrypt256() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", new NoPadding());

        assertArrayEquals(
                HexFormat.of().parseHex("cfcddbbe55865f859935c9452a3dff32"),
                aes.encrypt("Hola\0\0\0\0\0\0\0\0\0\0\0\0")
        );

        assertArrayEquals(
                HexFormat.of().parseHex("8ea2b7ca516745bfeafc49904b496089"),
                aes.encrypt(HexFormat.of().parseHex("00112233445566778899aabbccddeeff"))
        );
    }

    @Test
    void decrypt128() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f", new NoPadding());

        assertArrayEquals(
                HexFormat.of().parseHex("486f6c61000000000000000000000000"),
                aes.decrypt(HexFormat.of().parseHex("3b61ec0f4acd3b5eb2425bdc221a0949"))
        );

        assertArrayEquals(
                HexFormat.of().parseHex("00112233445566778899aabbccddeeff"),
                aes.decrypt(HexFormat.of().parseHex("69c4e0d86a7b0430d8cdb78070b4c55a"))
        );
    }

    @Test
    void decrypt192() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f1011121314151617", new NoPadding());

        assertArrayEquals(
                "Hola como estas ".getBytes(),
                aes.decrypt(HexFormat.of().parseHex("a875c99f5ea3af568786a5d1193872c8"))
        );

        assertArrayEquals(
                HexFormat.of().parseHex("00112233445566778899aabbccddeeff"),
                aes.decrypt(HexFormat.of().parseHex("dda97ca4864cdfe06eaf70a0ec0d7191"))
        );
    }

    @Test
    void decrypt256() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", new NoPadding());

        assertArrayEquals(
                HexFormat.of().parseHex("486f6c61000000000000000000000000"),
                aes.decrypt(HexFormat.of().parseHex("cfcddbbe55865f859935c9452a3dff32"))
        );

        assertArrayEquals(
                HexFormat.of().parseHex("00112233445566778899aabbccddeeff"),
                aes.decrypt(HexFormat.of().parseHex("8ea2b7ca516745bfeafc49904b496089"))
        );
    }

    /// Test an algorithm with a random key and plaintext given the keyLength in bytes
    /// @param keyLength number of bytes in the desired key
    void randomEncryptDecrypt(int keyLength) {
        // Generate a random key of length 'keyLength'
        byte[] key = new byte[keyLength];
        rnd.nextBytes(key);

        // Generate a random plaintext of length between 10 and 11 blocks
        byte[] text = new byte[AES.BLOCK_SIZE * 10 + rnd.nextInt(0, AES.BLOCK_SIZE)];
        rnd.nextBytes(text);

        // Default padding is PKCS7
        AES aes = new AES(key);

        assertArrayEquals(
                text,
                aes.decrypt(aes.encrypt(text))
        );
    }

    @Test
    void randomEncryptDecrypt128() {
        for (int i = 0; i < 100; ++i)
            randomEncryptDecrypt(16);
    }

    @Test
    void randomEncryptDecrypt192() {
        for (int i = 0; i < 100; ++i)
            randomEncryptDecrypt(24);
    }

    @Test
    void randomEncryptDecrypt256() {
        for (int i = 0; i < 100; ++i)
            randomEncryptDecrypt(32);
    }

    @Test
    void plaintextLengths() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");

        assertEquals(16, aes.encrypt(new byte[0]).length);
        assertEquals(16, aes.encrypt(new byte[1]).length);
        assertEquals(16, aes.encrypt(new byte[15]).length);
        assertEquals(32, aes.encrypt(new byte[16]).length);
        assertEquals(32, aes.encrypt(new byte[17]).length);
        assertEquals(32, aes.encrypt(new byte[31]).length);
        assertEquals(48, aes.encrypt(new byte[32]).length);
        assertEquals(48, aes.encrypt(new byte[33]).length);
    }

    @Test
    void decryptInvalidLength() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");

        assertThrows(
                IllegalArgumentException.class,
                () -> aes.decrypt(new byte[15])
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> aes.decrypt(new byte[17])
        );
    }
    @Test
    void repeatedUse() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");

        byte[] a = new byte[16];
        rnd.nextBytes(a);
        byte[] b = new byte[16];
        rnd.nextBytes(b);

        byte[] encryptedA = aes.encrypt(a);
        byte[] encryptedB = aes.encrypt(b);

        assertArrayEquals(a, aes.decrypt(encryptedA));
        assertArrayEquals(b, aes.decrypt(encryptedB));
    }

    /**
     * Test the encryption and decryption of non-ASCII characters.
     */
    @Test
    void nonASCIICharacters() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");

        String[] texts = {
                "私はJavaが好きじゃない。",
                "Δεν μου αρέσει η Java.",
                "我不喜欢Java。",
                "Мне не нравится Java.",
                "મને જાવા પસંદ નથી.",
                "ჯავა არ მომწონს.",
                "Ես չեմ սիրում Java-ն։",
                "من از جاوا خوشم نمیاد.",
        };

        for (String text : texts) {
            // Test text with UTF-8 encoding
            assertEquals(
                    text,
                    aes.decrypt(aes.encrypt(text, StandardCharsets.UTF_8), StandardCharsets.UTF_8)
            );

            // Test text with UTF-16 encoding
            assertEquals(
                    text,
                    aes.decrypt(aes.encrypt(text, StandardCharsets.UTF_16), StandardCharsets.UTF_16)
            );
        }
    }
}