package org.manriquecms.mafia.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manriquecms.mafia.model.Member;
import org.manriquecms.mafia.service.impl.MafiaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class MafiaServiceUT {

    @TestConfiguration
    static class MafiaServiceUTContextConfiguration {

        @Bean
        public MafiaService mafiaService() {
            return new MafiaServiceImpl();
        }
    }

    @Autowired
    MafiaService mafiaService;

    @Test
    public void OneBossWithSpecialSurveillance() {
        int maxMembers = 100;
        var padrino = Member.randomMember();
        mafiaService.addMember(padrino);
        for (int i = 2; i <= maxMembers; i++) {
            mafiaService.addMember(Member.randomMember(padrino.getId()));
        }

        Assert.assertTrue("Only one boss", mafiaService.findAllSpecialSurveillance().size() == 1);
    }

    @Test
    public void generateRandomFamily() {
        mafiaService.generateRandomFamily(4, 5, true);
        var members = mafiaService.getMembers();
        Assert.assertNotNull("Generated family is not null", members);
        Assert.assertTrue("There are 781 members in the family", members.size() == 781);
        var special = mafiaService.findAllSpecialSurveillance();
        Assert.assertNotNull("Special Surveillance members is not null", members);
        Assert.assertTrue("There are 6 in special surveillance", special.size() == 6);
    }
}
