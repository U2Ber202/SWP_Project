package model;

public class Account {
    public static final String ROLE_CUSTOMER = "customer";
    public static final String ROLE_OWNER = "owner";
    public static final String ROLE_SHIPPER = "shipper";
    public static final String ROLE_WAREHOUSE_MANAGER = "warehouse_manager";
    public static final String ROLE_ADMIN = "admin";

    private int uid;
    private String user;
    private String pass;
    private int isAdmin;
    private boolean active;
    private String role;

    private String fullname;
    private String phone;
    private String email;
    private String address;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Account() {
    }

    public Account(int uid, String user, String pass, int isAdmin, boolean active) {
        this.uid = uid;
        this.user = user;
        this.pass = pass;
        this.isAdmin = isAdmin;
        this.active = active;
        this.role = isAdmin == 1 ? ROLE_ADMIN : ROLE_CUSTOMER;
    }

    public Account(int uid, String user, String pass, int isAdmin, boolean active, String fullname, String phone, String email, String address) {
        this.uid = uid;
        this.user = user;
        this.pass = pass;
        this.isAdmin = isAdmin;
        this.active = active;
        this.role = isAdmin == 1 ? ROLE_ADMIN : ROLE_CUSTOMER;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getIsAdmin() {
        if (isAdmin == 1 || isAdmin()) {
            return 1;
        }
        return 0;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
        if (isAdmin == 1) {
            this.role = ROLE_ADMIN;
        } else if (this.role == null || this.role.trim().isEmpty() || ROLE_ADMIN.equalsIgnoreCase(this.role)) {
            this.role = ROLE_CUSTOMER;
        }
    }

    public String getRole() {
        if (role == null || role.trim().isEmpty()) {
            return isAdmin == 1 ? ROLE_ADMIN : ROLE_CUSTOMER;
        }
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        this.isAdmin = ROLE_ADMIN.equalsIgnoreCase(role) ? 1 : 0;
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equalsIgnoreCase(getRole());
    }

    public boolean isOwner() {
        return ROLE_OWNER.equalsIgnoreCase(getRole());
    }

    public boolean isCustomer() {
        return ROLE_CUSTOMER.equalsIgnoreCase(getRole());
    }

    public boolean isShipper() {
        return ROLE_SHIPPER.equalsIgnoreCase(getRole());
    }

    public boolean isWarehouseManager() {
        return ROLE_WAREHOUSE_MANAGER.equalsIgnoreCase(getRole());
    }
}