package model;

import model.Shipping;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;

public class ShippingTest {

    @Test
    public void testConstructorWithBasicParams() {
        Shipping s = new Shipping("Receiver", "0123456789", "123 Main St");
        assertEquals("Receiver", s.getName());
        assertEquals("0123456789", s.getPhone());
        assertEquals("123 Main St", s.getAddress());
    }

    @Test
    public void testFullConstructor() {
        Shipping s = new Shipping(1, "Receiver", "0123456789", "Address", "DELIVERED", 5);
        assertEquals(1, s.getId());
        assertEquals("DELIVERED", s.getStatus());
        assertEquals(5, s.getStoreId());
    }

    @Test
    public void testSettersAndGetters() {
        Shipping s = new Shipping();
        s.setId(10);
        s.setName("Test Name");
        s.setPhone("0999888777");
        s.setAddress("Street 1");
        s.setStatus("PENDING");
        s.setStoreId(2);
        s.setShipperId(5);
        s.setShipperName("Shipper Man");
        s.setShippedDate("2024-05-01");

        assertEquals(10, s.getId());
        assertEquals("Test Name", s.getName());
        assertEquals("0999888777", s.getPhone());
        assertEquals("Street 1", s.getAddress());
        assertEquals("PENDING", s.getStatus());
        assertEquals(2, s.getStoreId());
        assertEquals(5, s.getShipperId());
        assertEquals("Shipper Man", s.getShipperName());
        assertEquals("2024-05-01", s.getShippedDate());
    }

    @Test
    public void testToString() {
        Shipping s = new Shipping(1, "Name", "123", "Addr", "Stat");
        String str = s.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("name=Name"));
    }
}
