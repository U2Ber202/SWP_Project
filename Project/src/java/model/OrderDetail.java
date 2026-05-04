package model;

public class OrderDetail {

    private int id;
    private int orderId;
    private int variantId;
    private int productPrice;
    private int quantity;
    private ProductVariant variant;
    
    // Convenience fields for display
    private String productName;
    private String productImage;
    private String colorName;
    private String size;

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    public String getColorName() { return colorName; }
    public void setColorName(String colorName) { this.colorName = colorName; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public OrderDetail() {
    }

    public OrderDetail(int id, int orderId, int variantId, int productPrice, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.variantId = variantId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "id=" + id + ", orderId=" + orderId + ", variantId=" + variantId + ", productPrice=" + productPrice + ", quantity=" + quantity + '}';
    }
    
}
