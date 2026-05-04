package model;

import java.util.List;

public class Product {
    private int id;
    private String name;
    private String description;
    private int categoryId;
    private int storeId;
    private int manufacturerId;
    
    // Joined data
    private String storeName;
    private String categoryName;
    private String manufacturerName;
    private List<ProductVariant> variants;
    
    // Legacy fields for JSP compatibility
    private String imageUrl;
    private int price;
    private int quantity;
    private String tiltle; // Legacy sizes string

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getTiltle() { return tiltle; }
    public void setTiltle(String tiltle) { this.tiltle = tiltle; }

    public Product() {
    }

    public Product(int id, String name, String description, int categoryId, int storeId, int manufacturerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.storeId = storeId;
        this.manufacturerId = manufacturerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }
    
    // Helper to get representative price (e.g. from first variant)
    public int getMinPrice() {
        if (variants == null || variants.isEmpty()) return 0;
        int min = variants.get(0).getPrice();
        for (ProductVariant v : variants) {
            if (v.getPrice() < min) min = v.getPrice();
        }
        return min;
    }
}
