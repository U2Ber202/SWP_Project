package model;

import model.Contact;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;

public class ContactTest {

    @Test
    public void testFullConstructor() {
        Date now = new Date();
        Contact contact = new Contact(1, 101, 50, 5, "Hello", now, "OPEN");
        assertEquals(1, contact.getId());
        assertEquals(101, contact.getAccountId());
        assertEquals(50, contact.getOrderId());
        assertEquals(5, contact.getStoreId());
        assertEquals("Hello", contact.getMessage());
        assertEquals(now, contact.getCreatedAt());
        assertEquals("OPEN", contact.getStatus());
    }

    @Test
    public void testSettersAndGetters() {
        Contact contact = new Contact();
        Date now = new Date();
        Date resp = new Date(now.getTime() + 1000);
        
        contact.setId(10);
        contact.setAccountId(20);
        contact.setOrderId(30);
        contact.setStoreId(40);
        contact.setMessage("Issue");
        contact.setCreatedAt(now);
        contact.setStatus("RESOLVED");
        contact.setAccountName("User A");
        contact.setStoreName("Store B");
        contact.setResponseMessage("Fixed");
        contact.setRespondedAt(resp);

        assertEquals(10, contact.getId());
        assertEquals(20, contact.getAccountId());
        assertEquals(30, contact.getOrderId());
        assertEquals(40, contact.getStoreId());
        assertEquals("Issue", contact.getMessage());
        assertEquals(now, contact.getCreatedAt());
        assertEquals("RESOLVED", contact.getStatus());
        assertEquals("User A", contact.getAccountName());
        assertEquals("Store B", contact.getStoreName());
        assertEquals("Fixed", contact.getResponseMessage());
        assertEquals(resp, contact.getRespondedAt());
    }
}
