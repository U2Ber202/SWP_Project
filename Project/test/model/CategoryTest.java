package model;

import model.Category;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void testConstructorWithParams() {
        Category cat = new Category(1, "Sneakers");
        assertEquals(1, cat.getCid());
        assertEquals("Sneakers", cat.getCname());
    }

    @Test
    public void testFullConstructor() {
        Category cat = new Category(1, "Nike", 5);
        assertEquals(5, cat.getStoreId());
    }

    @Test
    public void testSettersAndGetters() {
        Category cat = new Category();
        cat.setCid(10);
        cat.setCname("Boots");
        cat.setStoreId(2);
        cat.setManufacturer("Timberland");

        assertEquals(10, cat.getCid());
        assertEquals("Boots", cat.getCname());
        assertEquals(2, cat.getStoreId());
        assertEquals("Timberland", cat.getManufacturer());
    }
}
