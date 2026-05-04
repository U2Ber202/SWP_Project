package model;

public class Store {
    private int storeId;
    private String storeName;
    private int ownerId;
    private boolean active;
    
    // Joined data
    private String ownerName;
    private int shipperId;
    private int warehouseManagerId;
    private int productCount;
    private double averageRating;

    public int getId() { return storeId; }
    public String getName() { return storeName; }

    public int getShipperId() { return shipperId; }
    public void setShipperId(int shipperId) { this.shipperId = shipperId; }

    public int getWarehouseManagerId() { return warehouseManagerId; }
    public void setWarehouseManagerId(int warehouseManagerId) { this.warehouseManagerId = warehouseManagerId; }

    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public Store() {
    }

    public Store(int storeId, String storeName, int ownerId, boolean active) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.ownerId = ownerId;
        this.active = active;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}