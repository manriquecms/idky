package org.manriquecms.phonebill.util;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

public class StaticUtilsTest {

    @Test
    public void testReadTime(){
        try {
            Assert.assertEquals(StaticUtils.getSecondsFromTime("01:00:00"),3600);
            Assert.assertEquals(StaticUtils.getSecondsFromTime("00:10:10"),610);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSecondsToMinutes(){
        Assert.assertTrue(StaticUtils.getSecondsToMinutes(300) == 5);
        Assert.assertTrue(StaticUtils.getSecondsToMinutes(301) == 6);
    }
}
