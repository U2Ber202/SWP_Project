package util;

import model.Account;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoleHelperTest {

    @Test
    public void testIsAdmin_True() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_ADMIN);
        assertTrue(RoleHelper.isAdmin(acc));
    }

    @Test
    public void testIsAdmin_False() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_CUSTOMER);
        assertFalse(RoleHelper.isAdmin(acc));
    }

    @Test
    public void testIsAdmin_Null() {
        assertFalse(RoleHelper.isAdmin(null));
    }

    @Test
    public void testIsOwner_True() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_OWNER);
        assertTrue(RoleHelper.isOwner(acc));
    }

    @Test
    public void testIsCustomer_True() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_CUSTOMER);
        assertTrue(RoleHelper.isCustomer(acc));
    }

    @Test
    public void testIsShipper_True() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_SHIPPER);
        assertTrue(RoleHelper.isShipper(acc));
    }

    @Test
    public void testIsWarehouseManager_True() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_WAREHOUSE_MANAGER);
        assertTrue(RoleHelper.isWarehouseManager(acc));
    }

    @Test
    public void testCanManageShipping_Owner() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_OWNER);
        assertTrue(RoleHelper.canManageShipping(acc));
    }

    @Test
    public void testCanManageShipping_Shipper() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_SHIPPER);
        assertTrue(RoleHelper.canManageShipping(acc));
    }

    @Test
    public void testCanManageShipping_Customer() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_CUSTOMER);
        assertFalse(RoleHelper.canManageShipping(acc));
    }

    @Test
    public void testCanManageInventory_Manager() {
        Account acc = new Account();
        acc.setRole(Account.ROLE_WAREHOUSE_MANAGER);
        assertTrue(RoleHelper.canManageInventory(acc));
    }
}
