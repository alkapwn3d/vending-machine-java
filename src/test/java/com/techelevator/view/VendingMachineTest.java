package com.techelevator.view;

import com.techelevator.VendingMachine;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;



import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VendingMachineTest {
    private VendingMachine vendingMachine;
    private Path tempDir;
    private Path salesReportPath;
    private Path logPath;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        vendingMachine = new VendingMachine();

        // Using reflection to set private field
        Field logFilePathField = VendingMachine.class.getDeclaredField("logFilePath");
        logFilePathField.setAccessible(true);
        logFilePathField.set(vendingMachine, tempDir.resolve("transaction.log").toString());

        // Continue with setting up the inventory as before
        vendingMachine.loadInventory("src/test/resources/inventory_test.txt");
    }

    @BeforeAll
    void setup() throws IOException {
        tempDir = Files.createTempDirectory("vendingMachineTest");
        salesReportPath = tempDir.resolve("sales_report.txt");
        logPath = tempDir.resolve("transaction_log.txt");

        vendingMachine = new VendingMachine();
        vendingMachine.loadInventory("src/test/resources/inventory_test.txt");
        // Assuming there is a method to set the log file path
      //  vendingMachine.setLogFilePath(logPath.toString());
    }


    @AfterEach
    void tearDownEach() throws IOException {
        // Delete test files after each test if they exist
        Files.deleteIfExists(salesReportPath);
        Files.deleteIfExists(logPath);
    }

    @AfterAll
    void tearDownAll() throws IOException {
        // Cleanup the temporary directory
        Files.deleteIfExists(tempDir);
    }

    @Test
    void test_should_append_data_to_log_file() throws IOException {
        // Set up
        String productCode = "A1";
        int quantity = 1;
        String expectedLogEntry = String.format("Purchase: %s Quantity: %d", productCode, quantity);

        // Action
        vendingMachine.processPurchase(productCode, quantity);

        // Assume that the log file is created in the default location that VendingMachine uses.
        Path defaultLogPath = Paths.get("default_log_location.log"); // Replace with actual default log path used by VendingMachine
        assertTrue(Files.exists(defaultLogPath), "Log file should exist");

        // Verification
        String logContents = new String(Files.readAllBytes(defaultLogPath), StandardCharsets.UTF_8);
        assertTrue(logContents.contains(expectedLogEntry), "Log file should contain the expected log entry");
    }

    @Test
    void test_should_calculate_total_sales() {
        // Presuming you have a method to calculate total sales
        double expectedTotalSales = 10.0; // Expected total sales value after some operations

        double actualTotalSales = vendingMachine.calculateTotalSales();

        assertEquals(expectedTotalSales, actualTotalSales, 0.01, "Total sales should match expected value.");
    }

    @Test
    void test_should_generate_sales_report() throws IOException {
        // Presuming you have a method that generates a sales report
        vendingMachine.generateSalesReport();

        assertTrue(Files.exists(salesReportPath), "Sales report file should exist.");

        // Further checks could compare the expected contents of the report
        // to the actual contents, as long as you know what the contents should be.
        String expectedReportContent = "Expected content";
        String actualReportContent = new String(Files.readAllBytes(salesReportPath));

        assertEquals(expectedReportContent.trim(), actualReportContent.trim(), "Sales report content should match expected content.");
    }
}