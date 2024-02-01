package com.techelevator.view;

import com.techelevator.Inventory;

import com.techelevator.exceptions.ProductFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class InventoryTest {
    private Inventory inventory;

    @BeforeEach
    void setUp()  throws IOException, ProductFormatException {
        inventory = new Inventory("src/test/resources/inventory_test.txt");
    }

    @Test
    void test_reads_inventory_file() {
        assertEquals(4, inventory.getSize());
    }
}
