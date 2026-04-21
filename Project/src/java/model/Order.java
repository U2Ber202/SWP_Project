
package model;




public class Order {
    
    private int id;
    private int accountId;
    private int totalPrice;
    private String note;
    private String createdDate;
    private int shippingId;
    private int status;
    private int storeId;

    public Order() {
    }

    public Order( int accountId, int totalPrice, String note, int shippingId) {
        this.accountId = accountId;
        this.totalPrice = totalPrice;
        this.note = note;
        this.shippingId = shippingId;
       
    }

    public Order( int accountId, int totalPrice, String note, int shippingId, int storeId) {
        this.accountId = accountId;
        this.totalPrice = totalPrice;
        this.note = note;
        this.shippingId = shippingId;
        this.storeId = storeId;
    }

    public Order(int id, int accountId, int totalPrice, String note, String createdDate, int shippingId, int status) {
        this.id = id;
        this.accountId = accountId;
        this.totalPrice = totalPrice;
        this.note = note;
        this.createdDate = createdDate;
        this.shippingId = shippingId;
        this.status = status;
    }

    public Order(int id, int accountId, int totalPrice, String note, String createdDate, int shippingId, int status, int storeId) {
        this.id = id;
        this.accountId = accountId;
        this.totalPrice = totalPrice;
        this.note = note;
        this.createdDate = createdDate;
        this.shippingId = shippingId;
        this.status = status;
        this.storeId = storeId;
    }
    

    public int getStatus(int aInt) {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", accountId=" + accountId + ", totalPrice=" + totalPrice + ", note=" + note + ", createdDate=" + createdDate + ", shippingId=" + shippingId + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }
    
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}
