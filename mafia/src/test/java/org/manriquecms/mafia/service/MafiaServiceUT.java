package org.manriquecms.mafia.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manriquecms.mafia.model.Member;
import static org.manriquecms.mafia.util.RandomGenerator.*;

import org.manriquecms.mafia.service.impl.MafiaServiceImpl;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

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
    public void OneBossWithSpecialSurveillance(){
        int maxMembers = 100;
        var padrino = Member.randomMember();
        mafiaService.addMember(padrino);
        for ( int i = 2; i<=maxMembers ; i++) {
            mafiaService.addMember(Member.randomMember(padrino.getId()));
        }

        Assert.assertTrue("Only one boss", mafiaService.findAllSpecialSurveillance().size() == 1);
    }

    @Test
    public void generateRandomFamily(){
        mafiaService.generateRandomFamily(5,5);
        var members = mafiaService.getMembers();
        var a = 2;
        //Assert.assertTrue("Generated family", mafiaService.findAllSpecialSurveillance().size() == 1);
    }
}
