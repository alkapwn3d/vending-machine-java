package com.techelevator.view;

import com.techelevator.CustomerAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;

public class CustomerAccountTest {
    private CustomerAccount account;

    @BeforeEach
    public void setUp() {
        account = new CustomerAccount();
    }

    @Test
    void test_should_start_with_balance_of_zero() {
        double expected = 0.0;
        double received = account.getBalance();
        assertEquals(expected, received, 0.0);
    }

    @Test
    void test_addToBalance_should_increase_balance_by_5() {
        double expected = account.getBalance() + 5.0;
        double received = account.addToBalance(5.0);
        assertEquals(expected, received, 0.0);
    }

    @Test
    void test_subtractFromBalance_should_decrease_balance_by_5() {
        account.addToBalance(10.0);
        double expected = account.getBalance() - 5.0;
        double received = account.subtractFromBalance(5.0);
        assertEquals(expected, received, 0.0);
    }

    @Test
    void test_should_give_correct_change() {
        account.addToBalance(2.5);
        double expected = 2.5;
        double received = account.giveChange();
        assertEquals(expected, received, 0.0);
    }

    @Test
    void test_should_have_zero_balance_after_give_change() {
        account.addToBalance(2.5);
        account.giveChange();
        double expected = 0;
        double received = account.getBalance();
        assertEquals(expected, received, 0.0);
    }
}
