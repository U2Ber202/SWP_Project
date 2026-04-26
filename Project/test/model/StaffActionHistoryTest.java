package model;

import model.StaffActionHistory;
import java.sql.Timestamp;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;

public class StaffActionHistoryTest {

    @Test
    public void testGettersAndSetters() {
        StaffActionHistory history = new StaffActionHistory();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        history.setId(1);
        history.setOwnerId(10);
        history.setStaffId(20);
        history.setActionType("UPDATE");
        history.setDetails("Changed price");
        history.setActionAt(now);
        history.setStaffName("Staff A");
        history.setStaffRole("Shipper");

        assertEquals(1, history.getId());
        assertEquals(10, history.getOwnerId());
        assertEquals(20, history.getStaffId());
        assertEquals("UPDATE", history.getActionType());
        assertEquals("Changed price", history.getDetails());
        assertEquals(now, history.getActionAt());
        assertEquals("Staff A", history.getStaffName());
        assertEquals("Shipper", history.getStaffRole());
    }
}
