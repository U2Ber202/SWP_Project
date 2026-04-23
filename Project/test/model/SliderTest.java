package model;

import model.Slider;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.jupiter.api.Test;

public class SliderTest {

    @Test
    public void testFullConstructor() {
        Slider slider = new Slider(1, "Promo", "img.jpg", "/link", true, "Desc");
        assertEquals(1, slider.getId());
        assertEquals("Promo", slider.getTitle());
        assertEquals("img.jpg", slider.getImageUrl());
        assertEquals("/link", slider.getBackLink());
        assertTrue(slider.isStatus());
        assertEquals("Desc", slider.getDescription());
    }

    @Test
    public void testSettersAndGetters() {
        Slider slider = new Slider();
        slider.setId(10);
        slider.setTitle("Summer Sale");
        slider.setImageUrl("summer.jpg");
        slider.setBackLink("/summer");
        slider.setStatus(false);
        slider.setDescription("Sale details");

        assertEquals(10, slider.getId());
        assertEquals("Summer Sale", slider.getTitle());
        assertEquals("summer.jpg", slider.getImageUrl());
        assertEquals("/summer", slider.getBackLink());
        assertFalse(slider.isStatus());
        assertEquals("Sale details", slider.getDescription());
    }
}
