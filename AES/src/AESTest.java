import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.util.HexFormat;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {
    Random rnd = new Random();

    @Test
    void encrypt128() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");

        assertArrayEquals(
                HexFormat.of().parseHex("3b61ec0f4acd3b5eb2425bdc221a0949"),
                aes.encrypt("Hola")
        );

        assertArrayEquals(
                HexFormat.of().parseHex("182d6b2a0050b60dd03cf74f5388a12d"),
                aes.encrypt("Chau")
        );
    }

    @Test
    void encrypt192() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f1011121314151617");

        assertArrayEquals(
                HexFormat.of().parseHex("a875c99f5ea3af568786a5d1193872c8"),
                aes.encrypt("Hola como estas ")
        );
    }

    @Test
    void encrypt256() {
        AES aes256 = new AES("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");

        assertArrayEquals(
                HexFormat.of().parseHex("cfcddbbe55865f859935c9452a3dff32"),
                aes256.encrypt("Hola")
        );
    }

    @Test
    void decrypt128() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");

        assertArrayEquals(
                HexFormat.of().parseHex("486f6c61000000000000000000000000"),
                aes.decrypt(HexFormat.of().parseHex("3b61ec0f4acd3b5eb2425bdc221a0949"))
        );
    }

    @Test
    void decrypt192() {
        AES aes256 = new AES("000102030405060708090a0b0c0d0e0f1011121314151617");

        assertArrayEquals(
                "Hola como estas ".getBytes(),
                aes256.decrypt(HexFormat.of().parseHex("a875c99f5ea3af568786a5d1193872c8"))
        );
    }

    @Test
    void decrypt256() {
        AES aes256 = new AES("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");

        assertArrayEquals(
                HexFormat.of().parseHex("486f6c61000000000000000000000000"),
                aes256.decrypt(HexFormat.of().parseHex("cfcddbbe55865f859935c9452a3dff32"))
        );
    }

    /// Test an algorithm with random key and plaintext given the keyLength in bytes
    /// @param keyLength number of bytes in the desired key
    void randomEncryptDecrypt(int keyLength) {
        byte[] key = new byte[keyLength];
        rnd.nextBytes(key);

        byte[] text = new byte[AES.BLOCK_SIZE];
        rnd.nextBytes(key);

        AES aes = new AES(key);

        assertArrayEquals(
                text,
                aes.decrypt(aes.encrypt(text))
        );
    }

    @Test
    void randomEncryptDecrypt128() {
        for (int i = 0; i < 4; ++i)
            randomEncryptDecrypt(16);
    }

    @Test
    void randomEncryptDecrypt192() {
        for (int i = 0; i < 4; ++i)
            randomEncryptDecrypt(24);
    }

    @Test
    void randomEncryptDecrypt256() {
        for (int i = 0; i < 4; ++i)
            randomEncryptDecrypt(32);
    }
}