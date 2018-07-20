package org.manriquecms.phonebill.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Call {
    private String number;
    private long duration;
    private long cost;

}
