package org.manriquecms.mafia.service.impl;

import lombok.extern.log4j.Log4j2;
import org.manriquecms.mafia.model.Member;
import org.manriquecms.mafia.service.MafiaService;
import static org.manriquecms.core.util.StaticUtils.random;
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
    public void clearFamily() {
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
        // Include again in the current family
        var memberReleased = mafiaJailIndex.remove(id);
        mafiaIndex.put(memberReleased.getId(),memberReleased);
        // We put him as a subordinate again to the ex-boss
        mafiaIndex.get(memberReleased.getIdBoss()).addSubordinate(id);
        // For each ex-subordinates we change the boss for their to the released member
        memberReleased.getSubordinates().forEach(idSub -> changeBossToMember(idSub, memberReleased.getId()));
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
    public Member getMember(UUID id) {
        return mafiaIndex.get(id);
    }

    @Override
    public Member getOldestSubordinate(UUID id) {
        return !CollectionUtils.isEmpty(mafiaIndex.get(id).getSubordinates()) ?
            mafiaIndex.get(id).getSubordinates().stream()
                .map(subId -> mafiaIndex.get(subId))
                .sorted(Comparator.comparing(Member::getJoinDate)).findFirst().orElseGet(null):null;
    }

    @Override
    public void generateRandomFamily(int levels, int maxSubordinates, boolean alwaysMax) {
        resetFamily();
        var padrino = Member.randomMember();
        addMember(padrino);
        generateLevel(padrino.getId(), levels, maxSubordinates, alwaysMax);
    }

    private void generateLevel(UUID idBoss, int levels, int maxSubordinates, boolean alwaysMax) {
        if (levels-- > 0) {
            var randomSubordinates = alwaysMax ? maxSubordinates : random.nextInt(maxSubordinates);
            for (int i = 0; i < randomSubordinates; i++) {
                log.debug("level "+levels+" boss "+idBoss+" sub "+i);
                var member = Member.randomMember(idBoss);
                addMember(member);
                generateLevel(member.getId(),levels,maxSubordinates, alwaysMax);
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
        // If there's no other subordinate for the boss of the removed member
        // we promote the oldest subordinate of the removed member
        Member newBossForSubordinates =
                Optional.ofNullable(getOldestSubordinate(memberRemovedBoss.getId()))
                        .orElse(getOldestSubordinate(memberRemoved.getId()));
        if(newBossForSubordinates != null){
            // Set the new boss for the murdered member subordinates
            // and
            // Add the murdered member subordinates to the new boss
            memberRemoved.getSubordinates().stream().forEach(id -> {
                if(!id.equals(newBossForSubordinates.getId())) {
                    mafiaIndex.get(id).setIdBoss(newBossForSubordinates.getId());
                    newBossForSubordinates.addSubordinate(id);
                }
            });
            // If the new boss was a subordinate, assign the boss to the member removed boss
            if (memberRemoved.getSubordinates().contains(newBossForSubordinates.getId())) {
                memberRemovedBoss.addSubordinate(newBossForSubordinates.getId());
                newBossForSubordinates.setIdBoss(memberRemovedBoss.getId());
            }
        }

    }
}
