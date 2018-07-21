package org.manriquecms.phonebill.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Call {
    private String number;
    private long duration;
    private long cost;
    private String comments;

    @Override
    public String toString(){
        return String.format("|%-20s|%10d|%10d|%-20s|", number,duration,cost,comments);
    }

    public static String getHeader(){
        return String.join("\n",
                StringUtils.repeat("-", 65),
                String.format("|%-20s|%10s|%10s|%-20s|", "Number", "Duration", "Cost", "Comments"),
                StringUtils.repeat("-", 65));
    }

    public static String getFooter(){
        return StringUtils.repeat("-", 65);
    }

    public static String getFooterTotal(long total){
        return String.format("|%42s|%20s|",String.join(" ","TOTAL:",Long.toString(total))," ");
    }
}
