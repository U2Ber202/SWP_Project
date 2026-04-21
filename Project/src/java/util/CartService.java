package util;

import dal.ProductDAO;
import jakarta.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Cart;

public final class CartService {

    public static final String CARTS_SESSION_KEY = "carts";
    public static final String CART_MESSAGE_SESSION_KEY = "cartMessage";
    public static final String PENDING_VNPAY_TXN_REF = "pendingVnpayTxnRef";
    public static final String PENDING_VNPAY_AMOUNT = "pendingVnpayAmount";
    public static final String PENDING_VNPAY_ACCOUNT_ID = "pendingVnpayAccountId";

    private CartService() {
    }

    @SuppressWarnings("unchecked")
    public static Map<Integer, Cart> getCartMap(HttpSession session) {
        Map<Integer, Cart> carts = (Map<Integer, Cart>) session.getAttribute(CARTS_SESSION_KEY);
        if (carts == null) {
            carts = new LinkedHashMap<>();
            session.setAttribute(CARTS_SESSION_KEY, carts);
        }
        return carts;
    }

    public static Map<Integer, Cart> getExistingCartMap(HttpSession session) {
        return (Map<Integer, Cart>) session.getAttribute(CARTS_SESSION_KEY);
    }

    public static int expireCartItems(HttpSession session) {
        Map<Integer, Cart> carts = getExistingCartMap(session);
        if (carts == null || carts.isEmpty()) {
            session.removeAttribute(CARTS_SESSION_KEY);
            return 0;
        }

        int expiredCount = releaseExpiredItems(carts, new ProductDAO());
        if (expiredCount > 0) {
            session.setAttribute(CART_MESSAGE_SESSION_KEY, "Có sản phẩm trong giỏ đã hết thời gian giữ chỗ và được trả lại kho.");
        }
        if (carts.isEmpty()) {
            session.removeAttribute(CARTS_SESSION_KEY);
        } else {
            session.setAttribute(CARTS_SESSION_KEY, carts);
        }
        return expiredCount;
    }

    public static int releaseAllCartItems(HttpSession session) {
        Map<Integer, Cart> carts = getExistingCartMap(session);
        if (carts == null || carts.isEmpty()) {
            clearPendingVnpay(session);
            return 0;
        }

        int releasedCount = 0;
        ProductDAO productDAO = new ProductDAO();
        for (Map.Entry<Integer, Cart> entry : carts.entrySet()) {
            Cart cart = entry.getValue();
            if (cart != null && cart.getQuantity() > 0) {
                productDAO.releaseStock(entry.getKey(), cart.getQuantity());
                releasedCount++;
            }
        }
        session.removeAttribute(CARTS_SESSION_KEY);
        clearPendingVnpay(session);
        return releasedCount;
    }

    private static int releaseExpiredItems(Map<Integer, Cart> carts, ProductDAO productDAO) {
        int expiredCount = 0;
        Iterator<Map.Entry<Integer, Cart>> iterator = carts.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Cart> entry = iterator.next();
            Cart cart = entry.getValue();
            if (cart != null && cart.isExpired()) {
                productDAO.releaseStock(entry.getKey(), cart.getQuantity());
                iterator.remove();
                expiredCount++;
            }
        }
        return expiredCount;
    }

    public static int countItems(Map<Integer, Cart> carts) {
        int total = 0;
        for (Cart cart : carts.values()) {
            total += cart.getQuantity();
        }
        return total;
    }

    public static void clearPendingVnpay(HttpSession session) {
        session.removeAttribute(PENDING_VNPAY_TXN_REF);
        session.removeAttribute(PENDING_VNPAY_AMOUNT);
        session.removeAttribute(PENDING_VNPAY_ACCOUNT_ID);
        session.removeAttribute("pendingShipping");
        session.removeAttribute("pendingNote");
    }
}
