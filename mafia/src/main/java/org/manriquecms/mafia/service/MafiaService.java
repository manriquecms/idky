package org.manriquecms.mafia.service;

import org.manriquecms.mafia.model.Member;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Configurable
public interface MafiaService {
    List<Member> getMembers();
    void addMember(Member member);
    void memberToJail(UUID id);
    void freeMemberFromJail(UUID id);
    void changeBossToMember(UUID id, UUID idBoss);
    void murderMember(UUID id);
    boolean needSpecialSurveillance(UUID id);
    Set<Member> findAllSpecialSurveillance();
    void generateRandomFamily(int levels, int maxSubordinates, boolean alwaysMax);
}
