package model;

import model.Store;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;

public class StoreTest {

    @Test
    public void testConstructorWithParams() {
        Store store = new Store(1, "Nike Store", 101);
        assertEquals(1, store.getId());
        assertEquals("Nike Store", store.getName());
        assertEquals(101, store.getOwnerId());
        assertTrue(store.isActive());
    }

    @Test
    public void testConstructorWithShipper() {
        Store store = new Store(1, "Adidas Store", 101, 202);
        assertEquals(202, store.getShipperId());
    }

    @Test
    public void testSettersAndGetters() {
        Store store = new Store();
        store.setId(10);
        store.setName("Puma Store");
        store.setOwnerId(50);
        store.setShipperId(60);
        store.setWarehouseManagerId(70);
        store.setProductCount(100);
        store.setAverageRating(4.5);
        store.setActive(false);

        assertEquals(10, store.getId());
        assertEquals("Puma Store", store.getName());
        assertEquals(50, store.getOwnerId());
        assertEquals(60, store.getShipperId());
        assertEquals(70, store.getWarehouseManagerId());
        assertEquals(100, store.getProductCount());
        assertEquals(4.5, store.getAverageRating());
        assertFalse(store.isActive());
    }
}
