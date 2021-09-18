package org.anystub;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class KeysSupplierCashedTest {


    @Test
    public void get() {

        int[] i = new int[]{0};

        KeysSupplier keysSupplier = new KeysSupplierCashed(() -> {
            i[0] = i[0] + 1;
            return new String[]{String.valueOf(i[0])};
        });


        assertArrayEquals(new String[]{"1"}, keysSupplier.get());
        assertArrayEquals(new String[]{"1"}, keysSupplier.get());

    }
}