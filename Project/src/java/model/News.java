package model;

import java.util.Date;
import java.sql.Timestamp;

public class News {
    private int id;
    private String title;
    private String content;
    private String image;
    private Date createdAt;
    private Integer storeId; // Null for system news
    private String storeName;
    private boolean isVisible;

    public News() {
        this.isVisible = true; // Default to visible
    }

    public News(int id, String title, String content, String image, Date createdAt, Integer storeId, boolean isVisible) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.storeId = storeId;
        this.isVisible = isVisible;
    }

    public boolean isIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
