
package model;






public class Cart {
    public static final long DEFAULT_TIMEOUT_MILLIS = 15L * 60L * 1000L;

    private Product product;
    private int quantity;
    private long reservedAt;
    private long expiresAt;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(long reservedAt) {
        this.reservedAt = reservedAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void refreshTimeout() {
        refreshTimeout(DEFAULT_TIMEOUT_MILLIS);
    }

    public void refreshTimeout(long timeoutMillis) {
        long now = System.currentTimeMillis();
        this.reservedAt = now;
        this.expiresAt = now + timeoutMillis;
    }

    public boolean isExpired() {
        return expiresAt > 0 && System.currentTimeMillis() > expiresAt;
    }

    public long getRemainingMillis() {
        return Math.max(0L, expiresAt - System.currentTimeMillis());
    }

    public Cart() {
    }

    public Cart(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        refreshTimeout();
    }
    
    
}
