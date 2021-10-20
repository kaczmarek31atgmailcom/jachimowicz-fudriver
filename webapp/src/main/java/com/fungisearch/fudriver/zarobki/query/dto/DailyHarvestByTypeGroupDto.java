package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.Date;

/**
 * Zbiory wg grup asortymentu w dniach. Stworzone na potrzeby dziennego raportu cykli
 */
public class DailyHarvestByTypeGroupDto {
    public Date date;
    public int groupId;
    public String groupName;
    public int totalG;

    public Date getDate() {
        return date;
    }
}
