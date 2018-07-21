package org.manriquecms.phonebill.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.manriquecms.phonebill.util.StaticUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phonebill {
    //Number + List<Callduration>
    HashMap<String,List<Call>> calls;

    public static Phonebill fromCalls(@NotNull String callStr) {
        Phonebill p = new Phonebill();
        p.readCalls(callStr);
        return p;
    }

    private void readCalls(@NotNull String callsStr){
        calls = Optional.ofNullable(calls).orElse(new HashMap<>());
        Arrays.stream(callsStr.split(System.lineSeparator())).forEach(line -> {
            var lineSplit = line.split(",");
            var number = Optional.ofNullable(lineSplit[1]).orElse("discarded");
            var time = Optional.ofNullable(lineSplit[0]).orElse("00:00:00");
            var currentList = Optional.ofNullable(calls.get(number)).orElse(new ArrayList<>());

            try {
                currentList.add(new Call(number,StaticUtils.getSecondsFromTime(time),0,""));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calls.put(number,currentList);
        });
    }

}
