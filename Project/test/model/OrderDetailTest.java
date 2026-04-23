package model;

import model.OrderDetail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;

public class OrderDetailTest {

    @Test
    public void testDefaultConstructor() {
        OrderDetail detail = new OrderDetail();
        assertEquals(0, detail.getId());
    }

    @Test
    public void testConstructorWithParams() {
        OrderDetail detail = new OrderDetail(1, 10, "Laptop", "laptop.jpg", 1500, 2);
        assertEquals(1, detail.getId());
        assertEquals(10, detail.getOrderId());
        assertEquals("Laptop", detail.getProductName());
        assertEquals("laptop.jpg", detail.getProductImage());
        assertEquals(1500, detail.getProductPrice());
        assertEquals(2, detail.getQuantity());
    }

    @Test
    public void testSettersAndGetters() {
        OrderDetail detail = new OrderDetail();
        detail.setId(5);
        detail.setOrderId(20);
        detail.setProductName("Mouse");
        detail.setProductImage("mouse.png");
        detail.setProductPrice(50);
        detail.setQuantity(3);

        assertEquals(5, detail.getId());
        assertEquals(20, detail.getOrderId());
        assertEquals("Mouse", detail.getProductName());
        assertEquals("mouse.png", detail.getProductImage());
        assertEquals(50, detail.getProductPrice());
        assertEquals(3, detail.getQuantity());
    }

    @Test
    public void testToString() {
        OrderDetail detail = new OrderDetail(1, 10, "Laptop", "laptop.jpg", 1500, 2);
        String str = detail.toString();
        assertTrue(str.contains("productName=Laptop"));
        assertTrue(str.contains("quantity=2"));
    }
}
