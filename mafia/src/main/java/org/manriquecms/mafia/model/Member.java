package org.manriquecms.mafia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.manriquecms.mafia.util.StaticUtils.faker;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private final UUID id = UUID.randomUUID();
    private String name;
    private Date joinDate;
    private UUID idBoss;
    private HashSet<UUID> subordinates;

    public boolean addSubordinate(UUID id){
        return subordinates.add(id);
    }

    public boolean removeSubordinate(UUID id){
        return subordinates.remove(id);
    }

    public static Member randomMember(UUID idBoss) {
        return new Member(faker.name().fullName(),faker.date().past(365,TimeUnit.DAYS),idBoss,new HashSet<>());
    }
    public static Member randomMember() {
        return randomMember(new UUID(0,0));
    }
}
