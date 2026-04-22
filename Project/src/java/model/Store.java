package model;

public class Store {
    private int id;
    private String name;
    private int ownerId;
    private int shipperId;
    private int warehouseManagerId;
    private int productCount;
    private double averageRating;
    private boolean active;

    public Store() {
    }

    public Store(int id, String name, int ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.active = true;
    }

    public Store(int id, String name, int ownerId, int shipperId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.shipperId = shipperId;
        this.active = true;
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getWarehouseManagerId() {
        return warehouseManagerId;
    }

    public void setWarehouseManagerId(int warehouseManagerId) {
        this.warehouseManagerId = warehouseManagerId;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}