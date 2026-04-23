package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class CartTest {

    @Test
    public void testCartConstructor() {
        Product p = new Product();
        p.setId(1);
        Cart cart = new Cart(p, 5);
        
        assertEquals(p, cart.getProduct());
        assertEquals(5, cart.getQuantity());
        assertTrue(cart.getReservedAt() > 0);
        assertTrue(cart.getExpiresAt() > cart.getReservedAt());
    }

    @Test
    public void testIsExpired_NewCart() {
        Cart cart = new Cart(new Product(), 1);
        assertFalse(cart.isExpired());
    }

    @Test
    public void testIsExpired_OldCart() {
        Cart cart = new Cart();
        cart.setExpiresAt(System.currentTimeMillis() - 1000); // 1 second ago
        assertTrue(cart.isExpired());
    }

    @Test
    public void testRefreshTimeout() {
        Cart cart = new Cart();
        long oldExpires = cart.getExpiresAt();
        cart.refreshTimeout(10000); // 10 seconds
        assertTrue(cart.getExpiresAt() > oldExpires);
        assertFalse(cart.isExpired());
    }

    @Test
    public void testGetRemainingMillis() {
        Cart cart = new Cart();
        cart.refreshTimeout(5000); // 5 seconds
        long remaining = cart.getRemainingMillis();
        assertTrue(remaining > 0 && remaining <= 5000);
    }

    @Test
    public void testGetRemainingMillis_Expired() {
        Cart cart = new Cart();
        cart.setExpiresAt(System.currentTimeMillis() - 1000);
        assertEquals(0, cart.getRemainingMillis());
    }

    @Test
    public void testSettersAndGetters() {
        Cart cart = new Cart();
        Product p = new Product();
        cart.setProduct(p);
        cart.setQuantity(10);
        cart.setReservedAt(12345L);
        cart.setExpiresAt(67890L);

        assertEquals(p, cart.getProduct());
        assertEquals(10, cart.getQuantity());
        assertEquals(12345L, cart.getReservedAt());
        assertEquals(67890L, cart.getExpiresAt());
    }
}
