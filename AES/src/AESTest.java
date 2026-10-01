import org.junit.jupiter.api.Test;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {
    @Test
    void encrypt() {
        AES aes = new AES("000102030405060708090a0b0c0d0e0f");

        assertArrayEquals(
                HexFormat.of().parseHex("3b61ec0f4acd3b5eb2425bdc221a0949"),
                aes.encrypt("Hola")
                );

        assertArrayEquals(
                HexFormat.of().parseHex("3b61ec0f4acd3b5eb2425bdc221a0949"),
                aes.encrypt("Hola")
        );
    }
}