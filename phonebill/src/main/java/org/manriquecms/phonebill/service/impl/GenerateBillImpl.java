package org.manriquecms.phonebill.service.impl;

import org.manriquecms.phonebill.model.Call;
import org.manriquecms.phonebill.model.Phonebill;
import org.manriquecms.phonebill.service.GenerateBill;
import org.manriquecms.phonebill.util.StaticUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenerateBillImpl implements GenerateBill {

    @Override
    public void generateBill(Phonebill phonebill) {
        applyTarifa(phonebill);
        applyOfferOneFree(phonebill);
    }

    private void applyTarifa(Phonebill phonebill){
        phonebill.getCalls().forEach((number, calls) -> calls.stream().forEach(call -> {
            call.setCost(call.getDuration()<300?
                    call.getDuration()*3:
                    StaticUtils.getSecondsToMinutes(call.getDuration())*150);
        }));

    }

    private void applyOfferOneFree(Phonebill phonebill){
        List<Call> durationPerNumber = new ArrayList<>();
        phonebill.getCalls().forEach((number, calls) -> {
            durationPerNumber.add(new Call(number,calls.stream().mapToLong(Call::getDuration).sum(),0));
        });

        String free = durationPerNumber.stream()
                .sorted((o1, o2) -> {
                    var diff = (int)(o2.getDuration() - o1.getDuration());
                    if (diff == 0) {
                        diff = o1.getNumber().compareTo(o2.getNumber());
                    }
                    return diff;
                })
                .map(c -> c.getNumber()).findFirst().orElse("");

        phonebill.getCalls().get(free).stream().forEach(c -> c.setCost(0));
    }

}
