package model;

import model.Feedback;
import java.sql.Timestamp;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;

public class FeedbackTest {

    @Test
    public void testGettersAndSetters() {
        Feedback fb = new Feedback();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        fb.setId(1);
        fb.setAccountId(10);
        fb.setProductId(20);
        fb.setStoreId(5);
        fb.setRating(5);
        fb.setContent("Great product!");
        fb.setCreateDate(now);
        fb.setUserName("John Doe");
        fb.setProductName("Sneaker");
        fb.setStoreName("Shop A");

        assertEquals(1, fb.getId());
        assertEquals(10, fb.getAccountId());
        assertEquals(20, fb.getProductId());
        assertEquals(5, fb.getStoreId());
        assertEquals(5, fb.getRating());
        assertEquals("Great product!", fb.getContent());
        assertEquals(now, fb.getCreateDate());
        assertEquals("John Doe", fb.getUserName());
        assertEquals("Sneaker", fb.getProductName());
        assertEquals("Shop A", fb.getStoreName());
    }

    @Test
    public void testRatingBounds() {
        Feedback fb = new Feedback();
        fb.setRating(1);
        assertEquals(1, fb.getRating());
        fb.setRating(5);
        assertEquals(5, fb.getRating());
    }
}
