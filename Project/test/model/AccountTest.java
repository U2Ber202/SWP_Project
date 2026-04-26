package model;

import org.junit.Test;
import static org.junit.Assert.*;
import model.Account;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    public void testDefaultConstructor() {
        Account acc = new Account();
        assertNull(acc.getUser());
        assertEquals(0, acc.getUid());
        assertFalse(acc.isActive());
    }

    @Test
    public void testConstructorWithRole() {
        Account acc = new Account(1, "user", "pass", 1, true);
        assertEquals("user", acc.getUser());
        assertEquals(1, acc.getUid());
        assertTrue(acc.isActive());
        assertEquals(Account.ROLE_ADMIN, acc.getRole());
        assertTrue(acc.isAdmin());
    }

    @Test
    public void testSetIsAdmin_UpdatesRole() {
        Account acc = new Account();
        acc.setIsAdmin(1);
        assertEquals(Account.ROLE_ADMIN, acc.getRole());
        
        acc.setIsAdmin(0);
        assertEquals(Account.ROLE_CUSTOMER, acc.getRole());
    }

    @Test
    public void testSetRole_UpdatesIsAdmin() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_ADMIN);
        assertEquals(1, acc.getIsAdmin());
        
        acc.setRole(Account.ROLE_CUSTOMER);
        assertEquals(0, acc.getIsAdmin());
    }

    @Test
    public void testIsMethods() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_OWNER);
        assertTrue(acc.isOwner());
        assertFalse(acc.isAdmin());
        
        acc.setRole(Account.ROLE_SHIPPER);
        assertTrue(acc.isShipper());
        
        acc.setRole(Account.ROLE_WAREHOUSE_MANAGER);
        assertTrue(acc.isWarehouseManager());
    }

    @Test
    public void testGettersAndSetters() {
        Account acc = new Account();
        acc.setFullname("Full Name");
        acc.setEmail("test@email.com");
        acc.setPhone("0123456789");
        acc.setAddress("123 Street");
        acc.setToken("token123");

        assertEquals("Full Name", acc.getFullname());
        assertEquals("test@email.com", acc.getEmail());
        assertEquals("0123456789", acc.getPhone());
        assertEquals("123 Street", acc.getAddress());
        assertEquals("token123", acc.getToken());
    }
}
