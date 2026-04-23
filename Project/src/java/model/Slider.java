package model;

public class Slider {
    private int id;
    private String title;
    private String imageUrl;
    private String backLink;
    private boolean status;
    private String description;

    public Slider() {
    }

    public Slider(int id, String title, String imageUrl, String backLink, boolean status, String description) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.backLink = backLink;
        this.status = status;
        this.description = description;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBackLink() {
        return backLink;
    }

    public void setBackLink(String backLink) {
        this.backLink = backLink;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
