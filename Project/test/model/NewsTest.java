package model;

import model.News;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
///import org.junit.jupiter.api.Test;

public class NewsTest {

    @Test
    public void testFullConstructor() {
        Date now = new Date();
        News news = new News(1, "Title", "Content", "img.jpg", now, 5);
        assertEquals(1, news.getId());
        assertEquals("Title", news.getTitle());
        assertEquals("Content", news.getContent());
        assertEquals("img.jpg", news.getImage());
        assertEquals(now, news.getCreatedAt());
       assertEquals(Integer.valueOf(5), news.getStoreId());
    }

    @Test
    public void testSettersAndGetters() {
        News news = new News();
        Date now = new Date();
        news.setId(10);
        news.setTitle("Big Sale");
        news.setContent("Details here");
        news.setImage("sale.png");
        news.setCreatedAt(now);
        news.setStoreId(null);
        news.setStoreName("System");

        assertEquals(10, news.getId());
        assertEquals("Big Sale", news.getTitle());
        assertEquals("Details here", news.getContent());
        assertEquals("sale.png", news.getImage());
        assertEquals(now, news.getCreatedAt());
        assertNull(news.getStoreId());
        assertEquals("System", news.getStoreName());
    }
}
