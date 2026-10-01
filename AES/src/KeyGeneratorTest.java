import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @org.junit.jupiter.api.Test
    void getRoundKey128() {
        KeyGenerator keygen = new KeyGenerator("000102030405060708090a0b0c0d0e0f");

        String[] keys = {
                "000102030405060708090a0b0c0d0e0f",
                "d6aa74fdd2af72fadaa678f1d6ab76fe",
                "b692cf0b643dbdf1be9bc5006830b3fe",
                "b6ff744ed2c2c9bf6c590cbf0469bf41",
                "47f7f7bc95353e03f96c32bcfd058dfd",
                "3caaa3e8a99f9deb50f3af57adf622aa",
                "5e390f7df7a69296a7553dc10aa31f6b",
                "14f9701ae35fe28c440adf4d4ea9c026",
                "47438735a41c65b9e016baf4aebf7ad2",
                "549932d1f08557681093ed9cbe2c974e",
                "13111d7fe3944a17f307a78b4d2b30c5"
        };

        for (int i = 0; i < keys.length; ++i) {
            assertArrayEquals(
                    HexFormat.of().parseHex(keys[i]),
                    keygen.getRoundKey(i)
            );
            System.out.printf("Round key %d correct%n", i);
        }
    }

    @org.junit.jupiter.api.Test
    void getRoundKey192() {
        KeyGenerator keygen = new KeyGenerator("000102030405060708090a0b0c0d0e0f1011121314151617");

        String[] keys = {
                "000102030405060708090a0b0c0d0e0f",
                "10111213141516175846f2f95c43f4fe",
                "544afef55847f0fa4856e2e95c43f4fe",
                "40f949b31cbabd4d48f043b810b7b342",
                "58e151ab04a2a5557effb5416245080c",
                "2ab54bb43a02f8f662e3a95d66410c08",
                "f501857297448d7ebdf1c6ca87f33e3c",
                "e510976183519b6934157c9ea351f1e0",
                "1ea0372a995309167c439e77ff12051e",
                "dd7e0e887e2fff68608fc842f9dcc154",
                "859f5f237a8d5a3dc0c02952beefd63a",
                "de601e7827bcdf2ca223800fd8aeda32",
                "a4970a331a78dc09c418c271e3a41d5d",
        };

        for (int i = 0; i < keys.length; ++i) {
            assertArrayEquals(
                    HexFormat.of().parseHex(keys[i]),
                    keygen.getRoundKey(i)
            );
            System.out.printf("Round key %d correct%n", i);
        }
    }

    @org.junit.jupiter.api.Test
    void getRoundKey256() {
        KeyGenerator keygen = new KeyGenerator("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f");

        String[] keys = {
                "000102030405060708090a0b0c0d0e0f",
                "101112131415161718191a1b1c1d1e1f",
                "a573c29fa176c498a97fce93a572c09c",
                "1651a8cd0244beda1a5da4c10640bade",
                "ae87dff00ff11b68a68ed5fb03fc1567",
                "6de1f1486fa54f9275f8eb5373b8518d",
                "c656827fc9a799176f294cec6cd5598b",
                "3de23a75524775e727bf9eb45407cf39",
                "0bdc905fc27b0948ad5245a4c1871c2f",
                "45f5a66017b2d387300d4d33640a820a",
                "7ccff71cbeb4fe5413e6bbf0d261a7df",
                "f01afafee7a82979d7a5644ab3afe640",
                "2541fe719bf500258813bbd55a721c0a",
                "4e5a6699a9f24fe07e572baacdf8cdea",
                "24fc79ccbf0979e9371ac23c6d68de36",
        };

        for (int i = 0; i < keys.length; ++i) {
            assertArrayEquals(
                    HexFormat.of().parseHex(keys[i]),
                    keygen.getRoundKey(i)
            );
            System.out.printf("Round key %d correct%n", i);
        }
    }
}