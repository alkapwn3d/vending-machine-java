package com.techelevator.view;


import com.techelevator.Product;
import com.techelevator.exceptions.ProductFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ProductTest {
    private Product product;
    @BeforeEach
    void setUp(){
        product = new Product();
    }
    @Test
    void test_should_create_a_new_product_object() throws ProductFormatException{
        Product product = Product.toProduct("A1|Potato Crisps|3.05|Chip");
        assertEquals("A1", product.getSlotLocation());
        assertEquals("Potato Crisps", product.getName());
        assertEquals(3.05, product.getPrice(), 0.00);
        assertEquals("Chip", product.getType());
    }
    @Test
    void test_should_decrease_quantity_by_one(){
        int expected = 4;
        int received = product.decrementQuantity();
        assertEquals(expected, received);
    }
    @Test
    void test_should_not_have_negative_quantity(){
        for (int i = 0; i < 10; i++) {
            product.decrementQuantity();
        }
        assertTrue(product.getQuantity() >= 0);
    }
    @Test
    void test_should_be_sold_out(){
        for (int i = 0; i < 5; i++) {
            product.decrementQuantity();
        }
        assertTrue(product.isSoldOut());
    }
}
