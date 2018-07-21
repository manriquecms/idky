package org.manriquecms.phonebill.service;

import org.manriquecms.phonebill.model.Phonebill;

public interface GenerateBill {
    public void generateBill(String calls);
    public long calculateCost();
    public String printBill();

}
