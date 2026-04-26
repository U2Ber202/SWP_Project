package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OrderTest {

    @Test
    public void testDefaultConstructor() {
        Order order = new Order();
        assertEquals(0, order.getId());
        assertNull(order.getNote());
    }

    @Test
    public void testConstructorWithParams() {
        Order order = new Order(101, 50000, "Urgent", 1);
        assertEquals(101, order.getAccountId());
        assertEquals(50000, order.getTotalPrice());
        assertEquals("Urgent", order.getNote());
        assertEquals(1, order.getShippingId());
    }

    @Test
    public void testConstructorWithStoreId() {
        Order order = new Order(101, 50000, "Urgent", 1, 5);
        assertEquals(5, order.getStoreId());
    }

    @Test
    public void testFullConstructor() {
        Order order = new Order(1, 101, 50000, "Note", "2024-04-22", 1, 2, 5);
        assertEquals(1, order.getId());
        assertEquals("2024-04-22", order.getCreatedDate());
        assertEquals(2, order.getStatus(0));
        assertEquals(5, order.getStoreId());
    }

    @Test
    public void testSettersAndGetters() {
        Order order = new Order();
        order.setId(10);
        order.setAccountId(20);
        order.setTotalPrice(150000);
        order.setNote("New Note");
        order.setCreatedDate("2024-01-01");
        order.setShippingId(3);
        order.setStatus(1);
        order.setStoreId(7);

        assertEquals(10, order.getId());
        assertEquals(20, order.getAccountId());
        assertEquals(150000, order.getTotalPrice());
        assertEquals("New Note", order.getNote());
        assertEquals("2024-01-01", order.getCreatedDate());
        assertEquals(3, order.getShippingId());
        assertEquals(1, order.getStatus(0));
        assertEquals(7, order.getStoreId());
    }

    @Test
    public void testToString() {
        Order order = new Order(1, 101, 500, "Note", "2024", 1, 0);
        String str = order.toString();
        assertTrue(str.contains("id=1"));
        assertTrue(str.contains("totalPrice=500"));
    }
}
