package model;

import model.StockImport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;

public class StockImportTest {

    @Test
    public void testGettersAndSetters() {
        StockImport stock = new StockImport();
        stock.setId(1);
        stock.setProductId(101);
        stock.setStoreId(5);
        stock.setProductName("Nike Air");
        stock.setImportQuantity(50);
        stock.setNote("First batch");
        stock.setCreatedAt("2024-04-22 10:00:00");
        stock.setCreatedDate("2024-04-22");
        stock.setCreatedTime("10:00:00");
        stock.setCreatedByName("Manager A");

        assertEquals(1, stock.getId());
        assertEquals(101, stock.getProductId());
        assertEquals(5, stock.getStoreId());
        assertEquals("Nike Air", stock.getProductName());
        assertEquals(50, stock.getImportQuantity());
        assertEquals("First batch", stock.getNote());
        assertEquals("2024-04-22 10:00:00", stock.getCreatedAt());
        assertEquals("2024-04-22", stock.getCreatedDate());
        assertEquals("10:00:00", stock.getCreatedTime());
        assertEquals("Manager A", stock.getCreatedByName());
    }
}
