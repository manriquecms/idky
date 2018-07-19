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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        mafiaService.clearFamily();
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
        mafiaService.clearFamily();
        mafiaService.generateRandomFamily(4, 5, true);
        var members = mafiaService.getMembers();
        Assert.assertNotNull("Generated family is not null", members);
        Assert.assertTrue("There are 781 members in the family", members.size() == 781);
        var special = mafiaService.findAllSpecialSurveillance();
        Assert.assertNotNull("Special Surveillance members is not null", members);
        Assert.assertTrue("There are 6 in special surveillance", special.size() == 6);
    }

    @Test
    public void memberToJail() {
        mafiaService.clearFamily();
        //|          m1       |          |         m1      |
        //|  m2   m3   m4   m5|    =>    |   m3    m4    m5|
        //|m6 m7  m8          |          |m6 m7 m8         |
        var m1 = Member.randomMember();
        var m2 = Member.randomMember(m1.getId());
        var m3 = Member.randomMember(m1.getId());
        var m4 = Member.randomMember(m1.getId());
        var m5 = Member.randomMember(m1.getId());
        var m6 = Member.randomMember(m2.getId());
        var m7 = Member.randomMember(m2.getId());
        var m8 = Member.randomMember(m3.getId());
        List.of(m1,m2,m3,m4,m5,m6,m7,m8).forEach(member -> mafiaService.addMember(member));

        mafiaService.memberToJail(m2.getId());
        Assert.assertNotEquals("m6's boss has changed",m6.getIdBoss(),m2.getId());
        Assert.assertEquals("m6 and m7 has the same new boss",m6.getIdBoss(),m7.getIdBoss());
        var newBoss = mafiaService.getMember(m6.getIdBoss());

        Assert.assertTrue("the new boss was the oldest subordinate",newBoss.getId().equals(mafiaService.getOldestSubordinate(newBoss.getIdBoss()).getId()));


    }

    @Test
    public void memberToJailNoSameLevel() {
        mafiaService.clearFamily();
        //|   m1   |          |  m1 |
        //|   m2   |    =>    |  m3 |
        //|m3 m4 m5|          |m4 m5|
        var m1 = Member.randomMember();
        var m2 = Member.randomMember(m1.getId());
        var m3 = Member.randomMember(m2.getId());
        var m4 = Member.randomMember(m2.getId());
        var m5 = Member.randomMember(m2.getId());
        List.of(m1,m2,m3,m4,m5).forEach(member -> mafiaService.addMember(member));

        mafiaService.memberToJail(m2.getId());
        Assert.assertNotEquals("m4's boss has changed",m4.getIdBoss(),m2.getId());
        Assert.assertEquals("m4 and m5 has the same new boss",m4.getIdBoss(),m5.getIdBoss());
        var newBoss = mafiaService.getMember(m4.getIdBoss());

        Assert.assertTrue("the new boss was the oldest subordinate",newBoss.getJoinDate().compareTo(mafiaService.getOldestSubordinate(newBoss.getId()).getJoinDate()) <= 0);
    }
}
