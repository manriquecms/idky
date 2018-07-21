package org.manriquecms.phonebill.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manriquecms.phonebill.model.Phonebill;
import org.manriquecms.phonebill.service.GenerateBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class GenerateBillImplUT {
    @TestConfiguration
    static class GenerateBillImplUTContextConfiguration {

        @Bean
        public GenerateBill generateBillService() {
            return new GenerateBillImpl();
        }
    }

    @Autowired
    GenerateBill generateBillService;

    @Test
    public void GenerateBillTest() {
        String calls =
                "00:01:07,400-234-090\n" +
                "00:05:01,701-080-080\n" +
                "00:05:01,701-080-080\n" +
                "00:05:01,601-080-080\n" +
                "00:05:01,601-080-080\n" +
                "00:05:01,801-080-080\n" +
                "00:05:01,801-080-080\n" +
                "00:05:00,500-234-090";
        generateBillService.generateBill(calls);
        System.out.println(generateBillService.printBill());
        //System.out.println(generateBillService.calculateCost());
        //TODO Define WHEN and THEN
        Assert.assertTrue("", true);
    }

}
