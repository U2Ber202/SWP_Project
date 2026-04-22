package model;

import java.sql.Date;

public class DailyRevenue {
    private Date date;
    private long revenue;

    public DailyRevenue() {
    }

    public DailyRevenue(Date date, long revenue) {
        this.date = date;
        this.revenue = revenue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
}
