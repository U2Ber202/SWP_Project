package model;

import java.sql.Timestamp;

public class StaffActionHistory {
    private int id;
    private int ownerId;
    private int staffId;
    private String actionType;
    private String details;
    private Timestamp actionAt;
    
    // Extra fields for display
    private String staffName;
    private String staffRole;

    public StaffActionHistory() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Timestamp getActionAt() { return actionAt; }
    public void setActionAt(Timestamp actionAt) { this.actionAt = actionAt; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getStaffRole() { return staffRole; }
    public void setStaffRole(String staffRole) { this.staffRole = staffRole; }
}
