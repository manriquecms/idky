package org.manriquecms.phonebill.service.impl;

import org.manriquecms.phonebill.model.Call;
import org.manriquecms.phonebill.model.Phonebill;
import org.manriquecms.phonebill.service.GenerateBill;
import org.manriquecms.phonebill.util.StaticUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenerateBillImpl implements GenerateBill {

    private Phonebill phonebill;

    @Override
    public void generateBill(String calls) {
        this.phonebill = Phonebill.fromCalls(calls);
        applyTarifa();
        applyOfferOneFree();
    }

    @Override
    public long calculateCost(){
        return phonebill.getCalls().values().stream()
                .map(calls -> calls.stream().mapToLong(Call::getCost).sum())
                .mapToLong(Long::longValue).sum();
    }

    @Override
    public String printBill(){
        return String.join("\n",
                Call.getHeader(),
                String.join("\n",
                        phonebill.getCalls().values().stream()
                                .flatMap(calls -> calls.stream().map(Call::toString))
                                .collect(Collectors.toList())),
                Call.getFooter(),
                Call.getFooterTotal(calculateCost()));
    }

    private void applyTarifa(){
        phonebill.getCalls().forEach((number, calls) -> calls.stream().forEach(call -> {
            call.setCost(call.getDuration()<300?
                    call.getDuration()*3:
                    StaticUtils.getSecondsToMinutes(call.getDuration())*150);
            call.setComments(call.getDuration()<300?
                    "3 cts/sec":
                    "150 cts/min");
        }));

    }

    private void applyOfferOneFree(){
        List<Call> durationPerNumber = new ArrayList<>();
        phonebill.getCalls().forEach((number, calls) -> {
            durationPerNumber.add(new Call(number,calls.stream().mapToLong(Call::getDuration).sum(),0, ""));
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

        phonebill.getCalls().get(free).stream().forEach(c -> {c.setCost(0); c.setComments("FREE PROMO");});
    }

}
