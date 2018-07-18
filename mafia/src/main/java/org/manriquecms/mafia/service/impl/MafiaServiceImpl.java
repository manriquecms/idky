package org.manriquecms.mafia.service.impl;

import lombok.extern.log4j.Log4j2;
import org.manriquecms.mafia.model.Member;
import org.manriquecms.mafia.service.MafiaService;
import static org.manriquecms.mafia.util.StaticUtils.random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Configurable
public class MafiaServiceImpl implements MafiaService {

    private int familyCounter = 1;
    private static final int MAX_SUBORDINATES = 50;
    private HashMap<UUID,Member> mafiaJailIndex;
    private HashMap<UUID,Member> mafiaIndex;
    private Member padrino;

    public MafiaServiceImpl() {
        mafiaJailIndex = new HashMap<>();
        mafiaIndex = new HashMap<>();
        padrino = null;
    }


    @Override
    public void addMember(Member member) {
        if (padrino == null) {
            member.setIdBoss(new UUID(0,0));
            padrino = member;
        } else {
            mafiaIndex.get(member.getIdBoss()).addSubordinate(member.getId());
        }
        mafiaIndex.put(member.getId(),member);

    }

    @Override
    public void memberToJail(UUID idToJail) {
        removeMember(idToJail);
        Member memberToJail = mafiaIndex.remove(idToJail);
        mafiaJailIndex.put(memberToJail.getId(), memberToJail);
    }

    @Override
    public void freeMemberFromJail(UUID id) {

    }

    @Override
    public Set<Member> findAllSpecialSurveillance() {
        return mafiaIndex.keySet().stream()
                .filter(id -> needSpecialSurveillance(id))
                .map(id -> mafiaIndex.get(id))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean needSpecialSurveillance(UUID id) {
        return getCountSubordinates(mafiaIndex.get(id).getSubordinates()) >= MAX_SUBORDINATES;
    }

    @Override
    public void changeBossToMember(UUID id, UUID idBoss) {
        // Remove the member for the current boss
        Member member = mafiaIndex.get(id);
        mafiaIndex.get(member.getIdBoss()).removeSubordinate(id);
        // Add to the new boss the subordinate
        // and
        // Change the boss for the member
        mafiaIndex.get(idBoss).addSubordinate(id);
        member.setIdBoss(idBoss);
    }

    @Override
    public void murderMember(UUID idMurdered) {
        removeMember(idMurdered);
        mafiaIndex.remove(idMurdered);
    }

    @Override
    public List<Member> getMembers() {
        return mafiaIndex.values().stream().collect(Collectors.toList());
    }

    @Override
    public void generateRandomFamily(int levels, int maxSubordinates) {
        resetFamily();
        var padrino = Member.randomMember();
        addMember(padrino);
        generateLevel(padrino.getId(), levels, maxSubordinates);
    }

    private void generateLevel(UUID idBoss, int levels, int maxSubordinates) {
        if (levels > 0) {
            var randomSubordinates = random.nextInt(maxSubordinates);
            for (int i = 0; i < randomSubordinates; i++) {
                var member = Member.randomMember(idBoss);
                addMember(member);
                generateLevel(member.getId(),levels--,maxSubordinates);
            }
        }
    }

    private void resetFamily(){
        mafiaJailIndex = new HashMap<>();
        mafiaIndex = new HashMap<>();
        padrino = null;
    }

    private int getCountSubordinates(HashSet<UUID> listSubordinates) {
        if (CollectionUtils.isEmpty(listSubordinates)) {
            return 0;
        } else {
            return listSubordinates.size() + listSubordinates.stream().map(id -> {
                return getCountSubordinates(mafiaIndex.get(id).getSubordinates());
            }).reduce((i, j) -> i+j).get();
        }
    }

    private void removeMember(UUID idToRemove){
        Member memberRemoved = mafiaIndex.get(idToRemove);
        Member memberRemovedBoss = mafiaIndex.get(memberRemoved.getIdBoss());
        // Remove the murdered for the boss
        memberRemovedBoss.removeSubordinate(idToRemove);
        // Find the oldest same level member
        Member newBossForSubordinates= memberRemovedBoss.getSubordinates().stream()
                .map(id -> mafiaIndex.get(id))
                .sorted(Comparator.comparing(Member::getJoinDate)).findFirst().orElseGet(null);
        // Set the new boss for the murdered member subordinates
        // and
        // Add the murdered member subordinates to the new boss
        memberRemovedBoss.getSubordinates().stream().forEach(id -> {
            mafiaIndex.get(id).setIdBoss(newBossForSubordinates.getId());
            newBossForSubordinates.addSubordinate(id);
        });
    }
}
