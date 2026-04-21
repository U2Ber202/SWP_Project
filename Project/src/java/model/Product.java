package model;


public class Product {
   int id;
     String name;
     String imageUrl;
     int price;
     String tiltle;
     String description;
     int sell_ID;
     int categoryId;
     int storeId;
     int quantity;
     String storeName;
     String manufacturer;

    public Product() {
    }

    public Product(int id, String name, String imageUrl, int price, String tiltle, String description, int sell_ID, int categoryId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.tiltle = tiltle;
        this.description = description;
        this.sell_ID = sell_ID;
        this.categoryId = categoryId;
    }

    public Product(int id, String name, String imageUrl, int price, String tiltle, String description, int sell_ID, int categoryId, int storeId) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.tiltle = tiltle;
        this.description = description;
        this.sell_ID = sell_ID;
        this.categoryId = categoryId;
        this.storeId = storeId;
    }

    public Product(int id, String name, String imageUrl, int price, String tiltle, String description, int sell_ID, int categoryId, int storeId, int quantity, String storeName) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.tiltle = tiltle;
        this.description = description;
        this.sell_ID = sell_ID;
        this.categoryId = categoryId;
        this.storeId = storeId;
        this.quantity = quantity;
        this.storeName = storeName;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTiltle() {
        return tiltle;
    }

    public void setTiltle(String tiltle) {
        this.tiltle = tiltle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSell_ID() {
        return sell_ID;
    }

    public void setSell_ID(int sell_ID) {
        this.sell_ID = sell_ID;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
