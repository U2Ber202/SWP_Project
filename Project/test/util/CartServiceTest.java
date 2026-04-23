package util;

import java.util.LinkedHashMap;
import java.util.Map;
import model.Cart;
import model.Product;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import util.CartService;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;

public class CartServiceTest {

    @Test
    public void testCountItems_Empty() {
        Map<Integer, Cart> carts = new LinkedHashMap<>();
        assertEquals(0, CartService.countItems(carts));
    }

    @Test
    public void testCountItems_Multiple() {
        Map<Integer, Cart> carts = new LinkedHashMap<>();
        carts.put(1, new Cart(new Product(), 2));
        carts.put(2, new Cart(new Product(), 3));
        assertEquals(5, CartService.countItems(carts));
    }

    @Test
    public void testCountItems_ZeroQuantity() {
        Map<Integer, Cart> carts = new LinkedHashMap<>();
        carts.put(1, new Cart(new Product(), 0));
        assertEquals(0, CartService.countItems(carts));
    }
}
