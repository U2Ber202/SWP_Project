package model;

import org.junit.Assert;
import model.Voucher;
import org.junit.Test;


public class VoucherTest {

    @Test
    public void testGettersAndSetters() {
        Voucher v = new Voucher();
        v.setId(1);
        v.setCode("SUMMER2024");
        v.setDiscountPercent(20);
        v.setMaxDiscount(500);
        v.setMinOrderValue(1000);
        v.setExpiryDate("2024-12-31");
        v.setStoreId(5);

        Assert.assertEquals(1, v.getId());
        Assert.assertEquals("SUMMER2024", v.getCode());
        Assert.assertEquals(20, v.getDiscountPercent());
        Assert.assertEquals(Integer.valueOf(500), v.getMaxDiscount());
        Assert.assertEquals(Integer.valueOf(1000), v.getMinOrderValue());
        Assert.assertEquals("2024-12-31", v.getExpiryDate());
        Assert.assertEquals(5, v.getStoreId());
    }
}
